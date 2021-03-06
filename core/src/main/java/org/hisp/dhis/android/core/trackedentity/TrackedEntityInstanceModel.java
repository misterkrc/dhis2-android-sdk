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

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gabrielittner.auto.value.cursor.ColumnAdapter;
import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;

import org.hisp.dhis.android.core.common.BaseIdentifiableDataModel;
import org.hisp.dhis.android.core.data.database.DbFeatureTypeColumnAdapter;
import org.hisp.dhis.android.core.period.FeatureType;
import org.hisp.dhis.android.core.utils.Utils;

@AutoValue
public abstract class TrackedEntityInstanceModel extends BaseIdentifiableDataModel  {
    public static final String TABLE = "TrackedEntityInstance";

    public static class Columns extends BaseIdentifiableDataModel.Columns {
        public static final String UID = "uid";
        public static final String CREATED_AT_CLIENT = "createdAtClient";
        public static final String LAST_UPDATED_AT_CLIENT = "lastUpdatedAtClient";
        public static final String ORGANISATION_UNIT = "organisationUnit";
        public static final String TRACKED_ENTITY_TYPE = "trackedEntityType";
        public static final String COORDINATES = "coordinates";
        public static final String FEATURE_TYPE = "featureType";

        @Override
        public String[] all() {
            return Utils.appendInNewArray(super.all(),
                    UID, CREATED_AT_CLIENT, LAST_UPDATED_AT_CLIENT, ORGANISATION_UNIT, TRACKED_ENTITY_TYPE, CREATED,
                    LAST_UPDATED, STATE, COORDINATES, FEATURE_TYPE);
        }
    }

    @NonNull
    public static TrackedEntityInstanceModel.Builder builder() {
        return new $$AutoValue_TrackedEntityInstanceModel.Builder();
    }

    @NonNull
    public static TrackedEntityInstanceModel create(Cursor cursor) {
        return AutoValue_TrackedEntityInstanceModel.createFromCursor(cursor);
    }

    @Nullable
    @ColumnName(Columns.UID)
    public abstract String uid();

    @Nullable
    @ColumnName(Columns.CREATED_AT_CLIENT)
    public abstract String createdAtClient();

    @Nullable
    @ColumnName(Columns.LAST_UPDATED_AT_CLIENT)
    public abstract String lastUpdatedAtClient();

    @Nullable
    @ColumnName(Columns.ORGANISATION_UNIT)
    public abstract String organisationUnit();

    @Nullable
    @ColumnName(Columns.TRACKED_ENTITY_TYPE)
    public abstract String trackedEntityType();

    @Nullable
    @ColumnName(Columns.COORDINATES)
    public abstract String coordinates();

    @Nullable
    @ColumnName(Columns.FEATURE_TYPE)
    @ColumnAdapter(DbFeatureTypeColumnAdapter.class)
    public abstract FeatureType featureType();

    @AutoValue.Builder
    public static abstract class Builder extends BaseIdentifiableDataModel.Builder<Builder> {
        public abstract Builder uid(@NonNull String uid);

        public abstract Builder createdAtClient(@Nullable String createdAtClient);

        public abstract Builder lastUpdatedAtClient(@Nullable String lastUpdatedAtClient);

        public abstract Builder organisationUnit(@Nullable String organisationUnit);

        public abstract Builder trackedEntityType(@Nullable String trackedEntityType);

        public abstract Builder coordinates(@Nullable String coordinates);

        public abstract Builder featureType(@Nullable FeatureType featureType);

        public abstract TrackedEntityInstanceModel build();
    }
}
