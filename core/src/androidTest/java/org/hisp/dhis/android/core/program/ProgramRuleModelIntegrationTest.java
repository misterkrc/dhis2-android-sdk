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

 package org.hisp.dhis.android.core.program;

import android.content.ContentValues;
import android.database.MatrixCursor;
import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.program.ProgramRuleModel.Columns;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Date;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class ProgramRuleModelIntegrationTest {
    // identifiable
    private static final long ID = 1L;
    private static final String UID = "test_uid";
    private static final String CODE = "test_code";
    private static final String NAME = "test_name";
    private static final String DISPLAY_NAME = "test_display_name";

    // used for timestamps
    private static final String DATE = "2017-11-01T11:40:00.000";

    // bound to Program Rule
    private static final String PROGRAM_STAGE = "test_programStage";
    private static final String PROGRAM = "test_program";
    private static final Integer PRIORITY = 2;
    private static final String CONDITION = "test_condition";


    @Test
    public void create_shouldConvertToModel() throws ParseException {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                Columns.ID, Columns.UID, Columns.CODE,
                Columns.NAME,
                Columns.DISPLAY_NAME,
                Columns.CREATED,
                Columns.LAST_UPDATED,
                Columns.PRIORITY, Columns.CONDITION,
                Columns.PROGRAM, Columns.PROGRAM_STAGE,
        });

        matrixCursor.addRow(new Object[]{
                ID, UID, CODE, NAME, DISPLAY_NAME, DATE, DATE,
                PRIORITY, CONDITION, PROGRAM, PROGRAM_STAGE
        });

        matrixCursor.moveToFirst();

        Date timeStamp = BaseIdentifiableObject.DATE_FORMAT.parse(DATE);

        ProgramRuleModel programRule = ProgramRuleModel.create(matrixCursor);

        assertThat(programRule.id()).isEqualTo(ID);
        assertThat(programRule.uid()).isEqualTo(UID);
        assertThat(programRule.code()).isEqualTo(CODE);
        assertThat(programRule.name()).isEqualTo(NAME);
        assertThat(programRule.displayName()).isEqualTo(DISPLAY_NAME);
        assertThat(programRule.created()).isEqualTo(timeStamp);
        assertThat(programRule.lastUpdated()).isEqualTo(timeStamp);
        assertThat(programRule.priority()).isEqualTo(PRIORITY);
        assertThat(programRule.condition()).isEqualTo(CONDITION);
        assertThat(programRule.program()).isEqualTo(PROGRAM);
        assertThat(programRule.programStage()).isEqualTo(PROGRAM_STAGE);
    }

    @Test
    public void toContentValues_shouldConvertToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProgramRuleModel.Columns.ID, ID);
        contentValues.put(ProgramRuleModel.Columns.UID, UID);
        contentValues.put(ProgramRuleModel.Columns.CODE, CODE);
        contentValues.put(ProgramRuleModel.Columns.NAME, NAME);
        contentValues.put(ProgramRuleModel.Columns.DISPLAY_NAME, DISPLAY_NAME);
        contentValues.put(ProgramRuleModel.Columns.CREATED, DATE);
        contentValues.put(ProgramRuleModel.Columns.LAST_UPDATED, DATE);
        contentValues.put(ProgramRuleModel.Columns.CONDITION, CONDITION);
        contentValues.put(ProgramRuleModel.Columns.PRIORITY, PRIORITY);
        contentValues.put(ProgramRuleModel.Columns.PROGRAM, PROGRAM);
        contentValues.put(ProgramRuleModel.Columns.PROGRAM_STAGE, PROGRAM_STAGE);

        assertThat(contentValues.getAsLong(Columns.ID)).isEqualTo(ID);
        assertThat(contentValues.getAsString(Columns.UID)).isEqualTo(UID);
        assertThat(contentValues.getAsString(Columns.CODE)).isEqualTo(CODE);
        assertThat(contentValues.getAsString(Columns.NAME)).isEqualTo(NAME);
        assertThat(contentValues.getAsString(Columns.DISPLAY_NAME)).isEqualTo(DISPLAY_NAME);
        assertThat(contentValues.getAsString(Columns.CREATED)).isEqualTo(DATE);
        assertThat(contentValues.getAsString(Columns.LAST_UPDATED)).isEqualTo(DATE);
        assertThat(contentValues.getAsString(Columns.CONDITION)).isEqualTo(CONDITION);
        assertThat(contentValues.getAsInteger(Columns.PRIORITY)).isEqualTo(PRIORITY);
        assertThat(contentValues.getAsString(Columns.PROGRAM)).isEqualTo(PROGRAM);
        assertThat(contentValues.getAsString(Columns.PROGRAM_STAGE)).isEqualTo(PROGRAM_STAGE);
    }
}