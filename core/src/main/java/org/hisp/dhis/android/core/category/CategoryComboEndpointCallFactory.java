package org.hisp.dhis.android.core.category;


import org.hisp.dhis.android.core.arch.api.executors.APICallExecutor;
import org.hisp.dhis.android.core.calls.factories.UidsCallFactoryImpl;
import org.hisp.dhis.android.core.calls.fetchers.CallFetcher;
import org.hisp.dhis.android.core.calls.fetchers.UidsNoResourceCallFetcher;
import org.hisp.dhis.android.core.calls.processors.CallProcessor;
import org.hisp.dhis.android.core.calls.processors.TransactionalNoResourceSyncCallProcessor;
import org.hisp.dhis.android.core.common.GenericCallData;
import org.hisp.dhis.android.core.common.Payload;
import org.hisp.dhis.android.core.common.UidsQuery;

import java.util.Set;

public final class CategoryComboEndpointCallFactory extends UidsCallFactoryImpl<CategoryCombo> {

    private static final int MAX_UID_LIST_SIZE = 130;

    public CategoryComboEndpointCallFactory(GenericCallData data, APICallExecutor apiCallExecutor) {
        super(data, apiCallExecutor);
    }

    @Override
    protected CallFetcher<CategoryCombo> fetcher(Set<String> uids) {
        final CategoryComboService service = data.retrofit().create(CategoryComboService.class);

        return new UidsNoResourceCallFetcher<CategoryCombo>(uids, MAX_UID_LIST_SIZE, apiCallExecutor) {

            @Override
            protected retrofit2.Call<Payload<CategoryCombo>> getCall(UidsQuery query) {
                return service.getCategoryCombos(
                        CategoryComboFields.allFields,
                        CategoryComboFields.uid.in(query.uids()),
                        Boolean.FALSE);
            }
        };
    }

    @Override
    protected CallProcessor<CategoryCombo> processor() {
        return new TransactionalNoResourceSyncCallProcessor<>(
                data.databaseAdapter(),
                CategoryComboHandler.create(data.databaseAdapter())
        );
    }
}