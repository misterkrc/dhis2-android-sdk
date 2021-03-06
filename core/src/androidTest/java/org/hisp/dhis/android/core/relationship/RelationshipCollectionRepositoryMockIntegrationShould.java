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

package org.hisp.dhis.android.core.relationship;

import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.common.BaseNameableObject;
import org.hisp.dhis.android.core.data.database.MockIntegrationShould;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class RelationshipCollectionRepositoryMockIntegrationShould extends MockIntegrationShould {

    @BeforeClass
    public static void setUpAll() throws Exception {
        downloadMetadata();
        downloadTrackedEntityInstances();
    }

    @Test
    public void find_all() {
        List<Relationship> relationships =
                d2.relationshipModule().relationships
                        .get();

        assertThat(relationships.size(), is(2));
    }

    @Test
    public void filter_by_uid() {
        List<Relationship> relationships =
                d2.relationshipModule().relationships
                        .byUid().eq("AJOytZW7OaI")
                        .get();

        assertThat(relationships.size(), is(1));
    }

    @Test
    public void filter_by_name() {
        List<Relationship> relationships =
                d2.relationshipModule().relationships
                        .byName().eq("Lab Sample to Person")
                        .get();

        assertThat(relationships.size(), is(1));
    }

    @Test
    public void filter_by_created() throws ParseException {
        List<Relationship> relationships =
                d2.relationshipModule().relationships
                        .byCreated().eq(BaseNameableObject.DATE_FORMAT.parse("2019-02-07T08:06:28.369"))
                        .get();

        assertThat(relationships.size(), is(1));
    }

    @Test
    public void filter_by_last_updated() throws ParseException {
        List<Relationship> relationships =
                d2.relationshipModule().relationships
                        .byLastUpdated().eq(BaseNameableObject.DATE_FORMAT.parse("2018-02-07T08:06:28.369"))
                        .get();

        assertThat(relationships.size(), is(1));
    }

    @Test
    public void filter_by_relationship_type() {
        List<Relationship> relationships =
                d2.relationshipModule().relationships
                        .byRelationshipType().eq("V2kkHafqs8G")
                        .get();

        assertThat(relationships.size(), is(1));
    }

    @Test
    public void get_by_item() {
        RelationshipItem item = RelationshipItem.builder().trackedEntityInstance(
                RelationshipItemTrackedEntityInstance.builder().trackedEntityInstance("nWrB0TfWlvh").build()).build();
        List<Relationship> relationships = d2.relationshipModule().relationships.getByItem(item);
        assertThat(relationships.size(), is(4));
    }

}
