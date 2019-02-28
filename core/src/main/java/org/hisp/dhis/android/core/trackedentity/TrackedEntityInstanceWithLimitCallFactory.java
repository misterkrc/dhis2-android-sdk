/*
 * Copyright (c) 2004-2019, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.core.trackedentity;

import org.hisp.dhis.android.core.arch.api.executors.APICallExecutor;
import org.hisp.dhis.android.core.arch.repositories.collection.ReadOnlyWithDownloadObjectRepository;
import org.hisp.dhis.android.core.common.D2CallExecutor;
import org.hisp.dhis.android.core.common.Unit;
import org.hisp.dhis.android.core.data.api.OuMode;
import org.hisp.dhis.android.core.maintenance.D2Error;
import org.hisp.dhis.android.core.maintenance.ForeignKeyCleaner;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit;
import org.hisp.dhis.android.core.resource.Resource;
import org.hisp.dhis.android.core.resource.ResourceHandler;
import org.hisp.dhis.android.core.systeminfo.DHISVersionManager;
import org.hisp.dhis.android.core.systeminfo.SystemInfo;
import org.hisp.dhis.android.core.user.UserOrganisationUnitLink;
import org.hisp.dhis.android.core.user.UserOrganisationUnitLinkStore;
import org.hisp.dhis.android.core.utils.services.ApiPagingEngine;
import org.hisp.dhis.android.core.utils.services.Paging;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
public final class TrackedEntityInstanceWithLimitCallFactory {

    private final Resource.Type resourceType = Resource.Type.TRACKED_ENTITY_INSTANCE;

    private final APICallExecutor apiCallExecutor;
    private final D2CallExecutor d2CallExecutor;
    private final ResourceHandler resourceHandler;
    private final UserOrganisationUnitLinkStore userOrganisationUnitLinkStore;
    private final ForeignKeyCleaner foreignKeyCleaner;
    private final ReadOnlyWithDownloadObjectRepository<SystemInfo> systemInfoRepository;
    private final DHISVersionManager versionManager;


    private final TrackedEntityInstanceRelationshipDownloadAndPersistCallFactory downloadAndPersistCallFactory;
    private final TrackedEntityInstancePersistenceCallFactory persistenceCallFactory;
    private final TrackedEntityInstancesEndpointCallFactory endpointCallFactory;

    @Inject
    TrackedEntityInstanceWithLimitCallFactory(
            APICallExecutor apiCallExecutor,
            D2CallExecutor d2CallExecutor,
            ResourceHandler resourceHandler,
            UserOrganisationUnitLinkStore userOrganisationUnitLinkStore,
            ForeignKeyCleaner foreignKeyCleaner,
            ReadOnlyWithDownloadObjectRepository<SystemInfo> systemInfoRepository,
            TrackedEntityInstanceRelationshipDownloadAndPersistCallFactory downloadAndPersistCallFactory,
            TrackedEntityInstancePersistenceCallFactory persistenceCallFactory,
            DHISVersionManager versionManager, TrackedEntityInstancesEndpointCallFactory endpointCallFactory) {
        this.apiCallExecutor = apiCallExecutor;
        this.d2CallExecutor = d2CallExecutor;
        this.resourceHandler = resourceHandler;
        this.userOrganisationUnitLinkStore = userOrganisationUnitLinkStore;
        this.foreignKeyCleaner = foreignKeyCleaner;
        this.systemInfoRepository = systemInfoRepository;
        this.versionManager = versionManager;

        this.downloadAndPersistCallFactory = downloadAndPersistCallFactory;
        this.persistenceCallFactory = persistenceCallFactory;
        this.endpointCallFactory = endpointCallFactory;
    }

    public Callable<Unit> getCall(final int teiLimit, final boolean limitByOrgUnit) {
        return () -> getTrackedEntityInstances(teiLimit, limitByOrgUnit);
    }
    
    private Unit getTrackedEntityInstances(final int teiLimit, final boolean limitByOrgUnit) throws D2Error {
        return d2CallExecutor.executeD2CallTransactionally(() -> {
            Collection<String> organisationUnitUids;
            TeiQuery.Builder teiQueryBuilder = TeiQuery.builder();
            int pageSize = teiQueryBuilder.build().pageSize();
            List<Paging> pagingList = ApiPagingEngine.getPaginationList(pageSize, teiLimit);

            String lastUpdatedStartDate = resourceHandler.getLastUpdated(resourceType);
            teiQueryBuilder.lastUpdatedStartDate(lastUpdatedStartDate);

            systemInfoRepository.download().call();

            if (limitByOrgUnit) {
                organisationUnitUids = getOrgUnitUids();
                Set<String> orgUnitWrapper = new HashSet<>();
                for (String orgUnitUid : organisationUnitUids) {
                    orgUnitWrapper.clear();
                    orgUnitWrapper.add(orgUnitUid);
                    teiQueryBuilder.orgUnits(orgUnitWrapper);
                    getTrackedEntityInstancesWithPaging(teiQueryBuilder, pagingList);
                }
            } else {
                organisationUnitUids = userOrganisationUnitLinkStore.queryRootCaptureOrganisationUnitUids();
                teiQueryBuilder.orgUnits(organisationUnitUids).ouMode(OuMode.DESCENDANTS);
                getTrackedEntityInstancesWithPaging(teiQueryBuilder, pagingList);
            }

            if (!versionManager.is2_29()) {
                d2CallExecutor.executeD2Call(downloadAndPersistCallFactory.getCall());
            }

            foreignKeyCleaner.cleanForeignKeyErrors();

            return new Unit();
        });
    }

    private void getTrackedEntityInstancesWithPaging(TeiQuery.Builder teiQueryBuilder,
                                                     List<Paging> pagingList) throws Exception {
        boolean successfulSync = true;

        for (Paging paging : pagingList) {
            try {
                teiQueryBuilder.page(paging.page()).pageSize(paging.pageSize());
                List<TrackedEntityInstance> pageTrackedEntityInstances =
                        apiCallExecutor.executePayloadCall(endpointCallFactory.getCall(teiQueryBuilder.build()));

                if (paging.isLastPage() && pageTrackedEntityInstances.size() > paging.previousItemsToSkipCount()) {
                    int toIndex = pageTrackedEntityInstances.size() <
                            paging.pageSize() - paging.posteriorItemsToSkipCount() ?
                            pageTrackedEntityInstances.size() :
                            paging.pageSize() - paging.posteriorItemsToSkipCount();

                    persistenceCallFactory.getCall(
                            pageTrackedEntityInstances.subList(paging.previousItemsToSkipCount(), toIndex)).call();

                } else {
                    persistenceCallFactory.getCall(pageTrackedEntityInstances).call();
                }

                if (pageTrackedEntityInstances.size() < paging.pageSize()) {
                    break;
                }

            } catch (D2Error ignored) {
                successfulSync = false;
            }
        }

        if (successfulSync) {
            resourceHandler.handleResource(resourceType);
        }
    }

    private Set<String> getOrgUnitUids() {
        List<UserOrganisationUnitLink> userOrganisationUnitLinks = userOrganisationUnitLinkStore.selectAll();

        Set<String> organisationUnitUids = new HashSet<>();

        for (UserOrganisationUnitLink userOrganisationUnitLink : userOrganisationUnitLinks) {
            if (userOrganisationUnitLink.organisationUnitScope().equals(
                    OrganisationUnit.Scope.SCOPE_DATA_CAPTURE.name())) {
                organisationUnitUids.add(userOrganisationUnitLink.organisationUnit());
            }
        }

        return organisationUnitUids;
    }
}