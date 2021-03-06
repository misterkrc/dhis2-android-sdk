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

package org.hisp.dhis.android.core.trackedentity.search;

import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.arch.api.executors.APICallExecutor;
import org.hisp.dhis.android.core.data.api.OuMode;
import org.hisp.dhis.android.core.maintenance.D2Error;
import org.hisp.dhis.android.core.maintenance.D2ErrorCode;
import org.hisp.dhis.android.core.maintenance.D2ErrorComponent;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceService;
import org.hisp.dhis.android.core.utils.Utils;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import dagger.Reusable;
import retrofit2.Call;

@SuppressWarnings({"PMD.PreserveStackTrace"})
@Reusable
public final class TrackedEntityInstanceQueryCallFactory {

    private final TrackedEntityInstanceService service;
    private final SearchGridMapper mapper;
    private final APICallExecutor apiCallExecutor;

    @Inject
    TrackedEntityInstanceQueryCallFactory(
            @NonNull TrackedEntityInstanceService service,
            @NonNull SearchGridMapper mapper,
            APICallExecutor apiCallExecutor) {
        this.service = service;
        this.mapper = mapper;
        this.apiCallExecutor = apiCallExecutor;
    }

    public Callable<List<TrackedEntityInstance>> getCall(final TrackedEntityInstanceQuery query) {
        return () -> queryTrackedEntityInstances(query);
    }

    private List<TrackedEntityInstance> queryTrackedEntityInstances(TrackedEntityInstanceQuery query) throws D2Error {

        OuMode mode = query.orgUnitMode();
        String orgUnitModeStr = mode == null ? null : mode.toString();

        String orgUnits = Utils.joinCollectionWithSeparator(query.orgUnits(), ";");
        Call<SearchGrid> searchGridCall = service.query(orgUnits,
                orgUnitModeStr, query.program(), query.formattedProgramStartDate(), query.formattedProgramEndDate(),
                query.query(), query.attribute(), query.filter(), query.paging(), query.page(), query.pageSize());

        SearchGrid searchGrid;

        try {
            searchGrid = apiCallExecutor.executeObjectCall(searchGridCall);
        } catch (D2Error d2E) {
            if (d2E.httpErrorCode() != null && d2E.httpErrorCode() == HttpsURLConnection.HTTP_REQ_TOO_LONG) {
                throw D2Error.builder()
                        .errorCode(D2ErrorCode.TOO_MANY_ORG_UNITS)
                        .errorDescription("Too many org units were selected")
                        .errorComponent(D2ErrorComponent.SDK)
                        .httpErrorCode(d2E.httpErrorCode())
                        .build();
            } else {
                throw d2E;
            }
        }

        try {
            return mapper.transform(searchGrid);
        } catch (ParseException pe) {
            throw D2Error.builder()
                    .errorCode(D2ErrorCode.SEARCH_GRID_PARSE)
                    .errorComponent(D2ErrorComponent.SDK)
                    .errorDescription("Search Grid mapping exception")
                    .originalException(pe)
                    .build();
        }
    }
}
