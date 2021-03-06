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

package org.hisp.dhis.android.core.arch.repositories.filters;

import org.hisp.dhis.android.core.arch.repositories.collection.CollectionRepositoryFactory;
import org.hisp.dhis.android.core.arch.repositories.collection.ReadOnlyCollectionRepository;
import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScopeItem;

import java.util.List;

public class FilterConnectorFactory<R extends ReadOnlyCollectionRepository<?>> {

    private final List<RepositoryScopeItem> scope;
    private final CollectionRepositoryFactory<R> repositoryFactory;

    public FilterConnectorFactory(List<RepositoryScopeItem> scope,
                                  CollectionRepositoryFactory<R> repositoryFactory) {
        this.scope = scope;
        this.repositoryFactory = repositoryFactory;
    }

    public StringFilterConnector<R> string(String key) {
        return new StringFilterConnector<>(repositoryFactory, scope, key);
    }

    public DateFilterConnector<R> date(String key) {
        return new DateFilterConnector<>(repositoryFactory, scope, key);
    }

    public BooleanFilterConnector<R> bool(String key) {
        return new BooleanFilterConnector<>(repositoryFactory, scope, key);
    }

    public IntegerFilterConnector<R> integer(String key) {
        return new IntegerFilterConnector<>(repositoryFactory, scope, key);
    }

    public <E extends Enum<E>> EnumFilterConnector<R, E> enumC(String key) {
        return new EnumFilterConnector<>(repositoryFactory, scope, key);
    }
}
