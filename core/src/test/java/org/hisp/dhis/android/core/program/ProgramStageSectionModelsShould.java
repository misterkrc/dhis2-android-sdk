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

import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.common.BaseObjectShould;
import org.hisp.dhis.android.core.common.ObjectShould;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ProgramStageSectionModelsShould extends BaseObjectShould implements ObjectShould {

    public ProgramStageSectionModelsShould() {
        super("program/program_stage_section.json");
    }

    @Test
    public void have_the_equals_method_conform_to_contract() {
        EqualsVerifier.forClass(ProgramStageSectionModel.builder().build().getClass())
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }

    @Override
    @Test
    public void map_from_json_string() throws IOException, ParseException {
        ProgramStageSection programStageSection = objectMapper.readValue(jsonStream, ProgramStageSection.class);

        assertThat(programStageSection.lastUpdated()).isEqualTo(
                BaseIdentifiableObject.DATE_FORMAT.parse("2015-10-14T13:37:29.904"));
        assertThat(programStageSection.created()).isEqualTo(
                BaseIdentifiableObject.DATE_FORMAT.parse("2015-01-26T13:14:09.957"));
        assertThat(programStageSection.uid()).isEqualTo("bbjzL5gp0NZ");
        assertThat(programStageSection.name()).isEqualTo("Care at Birth");
        assertThat(programStageSection.displayName()).isEqualTo("Care at Birth");
        assertThat(programStageSection.sortOrder()).isEqualTo(0);
        assertThat(programStageSection.programIndicators()).isEmpty();

        assertThat(programStageSection.dataElements().get(0).uid()).isEqualTo("Itl05OEupgQ");
        assertThat(programStageSection.dataElements().get(1).uid()).isEqualTo("Mfq2Y9N21KZ");
        assertThat(programStageSection.dataElements().get(2).uid()).isEqualTo("mGHBXrtqSut");
    }
}
