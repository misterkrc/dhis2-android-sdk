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

package org.hisp.dhis.android.core.arch.db;

import org.assertj.core.util.Lists;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class WhereClauseBuilderShould {

    @Test
    public void build_where_statement_for_one_key_value_pair_with_string_value() {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        String whereStatement = builder
                .appendKeyStringValue("COL", "VAL")
                .build();
        assertThat(whereStatement).isEqualTo("COL = 'VAL'");
    }

    @Test
    public void build_where_statement_for_one_key_value_pair_with_int_value() {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        String whereStatement = builder
                .appendKeyNumberValue("COL", 2)
                .build();
        assertThat(whereStatement).isEqualTo("COL = 2");
    }

    @Test
    public void build_where_statement_for_one_key_value_pair_with_like_string_value() {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        String whereStatement = builder
                .appendKeyLikeStringValue("COL", "VAL")
                .build();
        assertThat(whereStatement).isEqualTo("COL LIKE 'VAL'");
    }

    @Test
    public void build_where_statement_for_two_key_value_pairs() {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        String whereStatement = builder
                .appendKeyStringValue("COL1", "VAL1")
                .appendKeyStringValue("COL2", "VAL2")
                .build();
        assertThat(whereStatement).isEqualTo("COL1 = 'VAL1' AND COL2 = 'VAL2'");
    }

    @Test
    public void build_where_statement_for_two_key_value_pairs_with_or_logic_gate() {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        String whereStatement = builder
                .appendOrKeyStringValue("COL1", "VAL1")
                .appendOrKeyStringValue("COL2", "VAL2")
                .build();
        assertThat(whereStatement).isEqualTo("COL1 = 'VAL1' OR COL2 = 'VAL2'");
    }

    @Test
    public void build_where_statement_for_not_in_key_values() {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        String whereStatement = builder
                .appendNotInKeyStringValues("COL1", Lists.newArrayList("VAL1", "VAL2"))
                .build();
        assertThat(whereStatement).isEqualTo("COL1 NOT IN ('VAL1', 'VAL2')");
    }

    @Test
    public void build_where_statement_for_in_key_values() {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        String whereStatement = builder
                .appendInKeyStringValues("COL1", Lists.newArrayList("VAL1", "VAL2"))
                .build();
        assertThat(whereStatement).isEqualTo("COL1 IN ('VAL1', 'VAL2')");
    }

    @Test
    public void build_where_statement_for_is_null_value() {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        String whereStatement = builder
                .appendIsNullValue("COL1")
                .build();
        assertThat(whereStatement).isEqualTo("COL1 IS NULL");
    }

    @Test(expected = RuntimeException.class)
    public void throw_exception_for_no_pairs() {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        builder.build();
    }
}