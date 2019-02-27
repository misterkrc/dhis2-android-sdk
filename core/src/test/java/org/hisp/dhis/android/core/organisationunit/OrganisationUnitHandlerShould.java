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
package org.hisp.dhis.android.core.organisationunit;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.hisp.dhis.android.core.arch.handlers.LinkSyncHandler;
import org.hisp.dhis.android.core.arch.handlers.SyncHandler;
import org.hisp.dhis.android.core.common.CollectionCleaner;
import org.hisp.dhis.android.core.common.IdentifiableObjectStore;
import org.hisp.dhis.android.core.common.LinkModelHandler;
import org.hisp.dhis.android.core.common.LinkModelStore;
import org.hisp.dhis.android.core.common.ObjectWithUid;
import org.hisp.dhis.android.core.dataset.DataSetOrganisationUnitLink;
import org.hisp.dhis.android.core.program.Program;
import org.hisp.dhis.android.core.user.User;
import org.hisp.dhis.android.core.user.UserOrganisationUnitLinkModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OrganisationUnitHandlerShould {
    @Mock
    private IdentifiableObjectStore<OrganisationUnit> organisationUnitStore;

    @Mock
    private LinkModelStore<OrganisationUnitProgramLink> organisationUnitProgramLinkStore;

    @Mock
    private LinkSyncHandler<OrganisationUnitProgramLink> organisationUnitProgramLinkHandler;

    @Mock
    private LinkSyncHandler<DataSetOrganisationUnitLink> dataSetDataSetOrganisationUnitLinkHandler;

    @Mock
    private LinkModelHandler<OrganisationUnit, UserOrganisationUnitLinkModel> userOrganisationUnitLinkHandler;

    @Mock
    private SyncHandler<OrganisationUnitGroup> organisationUnitGroupHandler;

    @Mock
    private LinkSyncHandler<OrganisationUnitOrganisationUnitGroupLink> organisationUnitGroupLinkHandler;

    @Mock
    private CollectionCleaner<ObjectWithUid> programCollectionCleaner;

    @Mock
    private CollectionCleaner<ObjectWithUid> dataSetCollectionCleaner;

    @Mock
    private CollectionCleaner<ObjectWithUid> organisationUnitGroupCollectionCleaner;

    private OrganisationUnit organisationUnitWithoutGroups;

    private OrganisationUnit organisationUnitWithGroups;

    @Mock
    private OrganisationUnitGroup organisationUnitGroup;

    @Mock
    private User user;

    private Set<String> programUids;

    private Set<String> dataSetUids;

    @Mock
    private Program program;

    private OrganisationUnitHandler organisationUnitHandler;

    private List<OrganisationUnit> organisationUnits;

    private OrganisationUnit.Scope scope;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        scope = OrganisationUnit.Scope.SCOPE_DATA_CAPTURE;
        String programUid = "test_program_uid";
        programUids = Sets.newHashSet(Lists.newArrayList(programUid));
        String dataSetUid = "test_data_set_uid";
        dataSetUids = Sets.newHashSet(Lists.newArrayList(dataSetUid));

        organisationUnitHandler = new OrganisationUnitHandlerImpl(
                organisationUnitStore, userOrganisationUnitLinkHandler, organisationUnitProgramLinkHandler,
                dataSetDataSetOrganisationUnitLinkHandler, programCollectionCleaner, dataSetCollectionCleaner,
                organisationUnitGroupCollectionCleaner, organisationUnitGroupHandler, organisationUnitGroupLinkHandler);

        when(user.uid()).thenReturn("test_user_uid");
        when(program.uid()).thenReturn(programUid);

        when(organisationUnitGroup.uid()).thenReturn("test_organisation_unit_group_uid");
        List<OrganisationUnitGroup> organisationUnitGroups = Lists.newArrayList(organisationUnitGroup);

        OrganisationUnit.Builder builder = OrganisationUnit.builder()
                .uid("test_organisation_unit_uid")
                .programs(Collections.singletonList(program));

        organisationUnitWithoutGroups = builder
                .build();

        organisationUnitWithGroups = builder
                .organisationUnitGroups(organisationUnitGroups)
                .build();

        organisationUnits = Lists.newArrayList(organisationUnitWithGroups);
    }

    @Test
    public void persist_user_organisation_unit_link() {
        organisationUnitHandler.setData(programUids, dataSetUids, user);
        organisationUnitHandler.handleMany(organisationUnits, new OrganisationUnitDisplayPathTransformer());
    }

    @Test
    public void persist_program_organisation_unit_link_when_programs_uids() {
        organisationUnitHandler.setData(programUids, dataSetUids, user);
        organisationUnitHandler.handleMany(organisationUnits, new OrganisationUnitDisplayPathTransformer());
        verify(organisationUnitProgramLinkHandler).handleMany(anyString(), anyListOf(OrganisationUnitProgramLink.class));
    }

    @Test
    public void persist_program_organisation_unit_link_when_no_programs_uids() {
        organisationUnitHandler.setData(null, null, user);
        organisationUnitHandler.handleMany(organisationUnits, new OrganisationUnitDisplayPathTransformer());

        verifyNoMoreInteractions(organisationUnitProgramLinkStore);
    }

    @Test
    public void persist_organisation_unit_groups() {
        organisationUnitHandler.setData(programUids, dataSetUids, user);
        organisationUnitHandler.handleMany(organisationUnits, new OrganisationUnitDisplayPathTransformer());

        verify(organisationUnitGroupHandler).handleMany(anyListOf(OrganisationUnitGroup.class));
    }

    @Test
    public void persist_organisation_unit_organisation_unit_group_link() {
        organisationUnitHandler.setData(programUids, dataSetUids, user);
        organisationUnitHandler.handleMany(organisationUnits, new OrganisationUnitDisplayPathTransformer());

        verify(organisationUnitGroupLinkHandler).handleMany(anyString(),
                anyListOf(OrganisationUnitOrganisationUnitGroupLink.class));
    }

    @Test
    public void dont_persist_organisation_unit_organisation_unit_group_link_when_no_organisation_unit_groups() {
        organisationUnitHandler.setData(programUids, dataSetUids, user);

        organisationUnitHandler.handleMany(Lists.newArrayList(organisationUnitWithoutGroups), new OrganisationUnitDisplayPathTransformer());

        verify(organisationUnitGroupLinkHandler, never()).handleMany(anyString(),
                anyListOf(OrganisationUnitOrganisationUnitGroupLink.class));
    }

    @Test
    public void call_collection_cleaners() {
        organisationUnitHandler.setData(programUids, dataSetUids, user);
        organisationUnitHandler.handleMany(organisationUnits, new OrganisationUnitDisplayPathTransformer());

        verify(programCollectionCleaner).deleteNotPresent(anyCollectionOf(ObjectWithUid.class));
        verify(dataSetCollectionCleaner).deleteNotPresent(anyCollectionOf(ObjectWithUid.class));
        verify(organisationUnitGroupCollectionCleaner).deleteNotPresent(anyCollectionOf(ObjectWithUid.class));
    }
}