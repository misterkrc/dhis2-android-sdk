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

package org.hisp.dhis.android.core.user;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import org.hisp.dhis.android.core.common.BaseIdentifiableObjectModel;
import org.hisp.dhis.android.core.utils.Utils;

@Deprecated
@AutoValue
public abstract class UserModel extends BaseIdentifiableObjectModel {
    public static final String TABLE = "User";
    public static final String GENDER_MALE = "gender_male";
    public static final String GENDER_FEMALE = "gender_female";
    public static final String GENDER_OTHER = "gender_other";

    public static class Columns extends BaseIdentifiableObjectModel.Columns {
        public static final String BIRTHDAY = "birthday";
        public static final String EDUCATION = "education";
        public static final String GENDER = "gender";
        public static final String JOB_TITLE = "jobTitle";
        public static final String SURNAME = "surname";
        public static final String FIRST_NAME = "firstName";
        public static final String INTRODUCTION = "introduction";
        public static final String EMPLOYER = "employer";
        public static final String INTERESTS = "interests";
        public static final String LANGUAGES = "languages";
        public static final String EMAIL = "email";
        public static final String PHONE_NUMBER = "phoneNumber";
        public static final String NATIONALITY = "nationality";

        @Override
        public String[] all() {
            return Utils.appendInNewArray(super.all(),
                    BIRTHDAY, EDUCATION, GENDER, JOB_TITLE, SURNAME, FIRST_NAME, INTRODUCTION,
                    EMPLOYER, INTERESTS, LANGUAGES, EMAIL, PHONE_NUMBER, NATIONALITY);
        }
    }

    @Nullable
    public abstract String birthday();

    @Nullable
    public abstract String education();

    @Nullable
    public abstract String gender();

    @Nullable
    public abstract String jobTitle();

    @Nullable
    public abstract String surname();

    @Nullable
    public abstract String firstName();

    @Nullable
    public abstract String introduction();

    @Nullable
    public abstract String employer();

    @Nullable
    public abstract String interests();

    @Nullable
    public abstract String languages();

    @Nullable
    public abstract String email();

    @Nullable
    public abstract String phoneNumber();

    @Nullable
    public abstract String nationality();

    @NonNull
    public static Builder builder() {
        return new $$AutoValue_UserModel.Builder();
    }

    @NonNull
    public static UserModel create(Cursor cursor) {
        return AutoValue_UserModel.createFromCursor(cursor);
    }

    @AutoValue.Builder
    public static abstract class Builder extends BaseIdentifiableObjectModel.Builder<Builder> {
        public abstract Builder birthday(@Nullable String birthday);

        public abstract Builder education(@Nullable String education);

        public abstract Builder gender(@Nullable String gender);

        public abstract Builder jobTitle(@Nullable String jobTitle);

        public abstract Builder surname(@Nullable String surName);

        public abstract Builder firstName(@Nullable String firstName);

        public abstract Builder introduction(@Nullable String introduction);

        public abstract Builder employer(@Nullable String employer);

        public abstract Builder interests(@Nullable String interests);

        public abstract Builder languages(@Nullable String languages);

        public abstract Builder email(@Nullable String email);

        public abstract Builder phoneNumber(@Nullable String phoneNumber);

        public abstract Builder nationality(@Nullable String nationality);

        public abstract UserModel build();
    }
}
