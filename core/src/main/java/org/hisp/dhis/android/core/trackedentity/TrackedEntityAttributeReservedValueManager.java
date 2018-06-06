/*
 * Copyright (c) 2017, University of Oslo
 *
 * All rights reserved.
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

import org.hisp.dhis.android.core.common.D2CallExecutor;
import org.hisp.dhis.android.core.common.GenericCallData;
import org.hisp.dhis.android.core.common.IdentifiableObjectStore;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitModel;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitStore;
import org.hisp.dhis.android.core.systeminfo.SystemInfo;
import org.hisp.dhis.android.core.systeminfo.SystemInfoCall;

import java.util.Date;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import retrofit2.Response;
import retrofit2.Retrofit;

public final class TrackedEntityAttributeReservedValueManager {

    private static final int MIN_TO_TRY_FILL = 50;
    private static final int FILL_UP_TO = 100;

    private final TrackedEntityAttributeReservedValueStoreInterface store;
    private final IdentifiableObjectStore<OrganisationUnitModel> organisationUnitStore;
    private final DatabaseAdapter databaseAdapter;
    private final Retrofit retrofit;

    TrackedEntityAttributeReservedValueManager(DatabaseAdapter databaseAdapter,
                                               Retrofit retrofit,
                                               TrackedEntityAttributeReservedValueStoreInterface store,
                                               IdentifiableObjectStore<OrganisationUnitModel> organisationUnitStore) {
        this.databaseAdapter = databaseAdapter;
        this.retrofit = retrofit;
        this.store = store;
        this.organisationUnitStore = organisationUnitStore;
    }

    @SuppressFBWarnings("DE_MIGHT_IGNORE")
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public String getValue(String attribute, String organisationUnitUid) throws RuntimeException {
        try {
            syncReservedValues(attribute, organisationUnitUid);
        } catch (Exception e) {
            // Synchronization was not successful.
        }

        TrackedEntityAttributeReservedValueModel reservedValue = store.popOne(attribute, organisationUnitUid);

        if (reservedValue == null) {
            throw new RuntimeException("There are no reserved values.");
        } else {
            return reservedValue.value();
        }
    }

    private void syncReservedValues(String attribute, String organisationUnitUid) throws Exception {
        // TODO use server date
        store.deleteExpired(new Date());

        int remainingValues = store.count(attribute, organisationUnitUid);
        if (remainingValues <= MIN_TO_TRY_FILL) {
            fillReservedValues(attribute, organisationUnitUid, remainingValues);
        }
    }

    private void fillReservedValues(String attribute, String organisationUnitUid, Integer remainingValues)
            throws Exception {

        SystemInfo systemInfo = new D2CallExecutor().executeD2Call(
                SystemInfoCall.FACTORY.create(databaseAdapter, retrofit));

        GenericCallData genericCallData = GenericCallData.create(databaseAdapter, retrofit,
                systemInfo.serverDate());

        Integer numberToReserve = FILL_UP_TO - remainingValues;
        OrganisationUnitModel organisationUnitModel =
                this.organisationUnitStore.selectByUid(organisationUnitUid, OrganisationUnitModel.factory);

        Response reservedValueResponse = TrackedEntityAttributeReservedValueEndpointCall.FACTORY.create(
                genericCallData, attribute, numberToReserve, organisationUnitModel).call();
        if (!reservedValueResponse.isSuccessful()) {
            throw new RuntimeException("New reserved values call failed.");
        }
    }

    public static TrackedEntityAttributeReservedValueManager create(DatabaseAdapter databaseAdapter,
                                                                    Retrofit retrofit) {
        return new TrackedEntityAttributeReservedValueManager(
                databaseAdapter,
                retrofit,
                TrackedEntityAttributeReservedValueStore.create(databaseAdapter),
                OrganisationUnitStore.create(databaseAdapter));
    }
}