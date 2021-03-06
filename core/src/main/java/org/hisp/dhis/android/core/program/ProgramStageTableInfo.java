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

package org.hisp.dhis.android.core.program;

import org.hisp.dhis.android.core.arch.db.TableInfo;
import org.hisp.dhis.android.core.common.BaseIdentifiableObjectModel;
import org.hisp.dhis.android.core.common.BaseModel;
import org.hisp.dhis.android.core.utils.Utils;

public final class ProgramStageTableInfo {

    private ProgramStageTableInfo() {
    }

    public static final TableInfo TABLE_INFO = new TableInfo() {

        @Override
        public String name() {
            return "ProgramStage";
        }

        @Override
        public BaseModel.Columns columns() {
            return new Columns();
        }
    };

    static class Columns extends BaseIdentifiableObjectModel.Columns {
        static final String ACCESS_DATA_WRITE = "accessDataWrite";

        @Override
        public String[] all() {
            return Utils.appendInNewArray(super.all(),
                    ProgramStageFields.DESCRIPTION,
                    ProgramStageFields.DISPLAY_DESCRIPTION,
                    ProgramStageFields.EXECUTION_DATE_LABEL,
                    ProgramStageFields.ALLOW_GENERATE_NEXT_VISIT,
                    ProgramStageFields.VALID_COMPLETE_ONLY,
                    ProgramStageFields.REPORT_DATE_TO_USE,
                    ProgramStageFields.OPEN_AFTER_ENROLLMENT,
                    ProgramStageFields.REPEATABLE,
                    ProgramStageFields.CAPTURE_COORDINATES,
                    ProgramStageFields.FORM_TYPE,
                    ProgramStageFields.DISPLAY_GENERATE_EVENT_BOX,
                    ProgramStageFields.GENERATED_BY_ENROLMENT_DATE,
                    ProgramStageFields.AUTO_GENERATE_EVENT,
                    ProgramStageFields.SORT_ORDER,
                    ProgramStageFields.HIDE_DUE_DATE,
                    ProgramStageFields.BLOCK_ENTRY_FORM,
                    ProgramStageFields.MIN_DAYS_FROM_START,
                    ProgramStageFields.STANDARD_INTERVAL,
                    ProgramStageFields.PROGRAM,
                    ProgramStageFields.PERIOD_TYPE,
                    ACCESS_DATA_WRITE,
                    ProgramStageFields.REMIND_COMPLETED,
                    ProgramStageFields.FEATURE_TYPE
            );
        }
    }
}