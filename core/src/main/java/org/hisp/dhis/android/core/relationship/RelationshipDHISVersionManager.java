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

import org.hisp.dhis.android.core.systeminfo.DHISVersionManager;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.android.core.utils.CodeGeneratorImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
public class RelationshipDHISVersionManager {

    private final DHISVersionManager versionManager;

    @Inject
    public RelationshipDHISVersionManager(DHISVersionManager versionManager) {
        this.versionManager = versionManager;
    }

    public List<Relationship229Compatible> to229Compatible(List<Relationship> storedRelationships, String teiUid) {
        List<Relationship229Compatible> transformedRelationships = new ArrayList<>();
        for (Relationship relationship : storedRelationships) {
            RelationshipItem fromTei = relationship.from();
            if (versionManager.is2_29() || fromTei != null && fromTei.trackedEntityInstance() != null &&
                    fromTei.trackedEntityInstance().trackedEntityInstance().equals(teiUid)) {
                transformedRelationships.add(to229Compatible(relationship, teiUid));
            }
        }
        return transformedRelationships;
    }

    Relationship229Compatible to229Compatible(Relationship relationship, String teiUid) {
        Relationship229Compatible.Builder builder = Relationship229Compatible.builder()
                .name(relationship.name())
                .created(relationship.created())
                .lastUpdated(relationship.lastUpdated());

        if (versionManager.is2_29()) {
            return builder
                    .uid(relationship.relationshipType())
                    .trackedEntityInstanceA(relationship.from().trackedEntityInstance().trackedEntityInstance())
                    .trackedEntityInstanceB(relationship.to().trackedEntityInstance().trackedEntityInstance())
                    .relative(getRelative230(relationship, teiUid))
                    .build();
        } else {
            return builder
                    .uid(relationship.uid())
                    .relationshipType(relationship.relationshipType())
                    .from(relationship.from())
                    .to(relationship.to())
                    .build();
        }
    }

    public Relationship from229Compatible(Relationship229Compatible relationship229Compatible) {
        Relationship.Builder builder = Relationship.builder()
                .name(relationship229Compatible.name())
                .created(relationship229Compatible.created())
                .lastUpdated(relationship229Compatible.lastUpdated());

        if (versionManager.is2_29()) {
            return builder
                    .uid(new CodeGeneratorImpl().generate())
                    .relationshipType(relationship229Compatible.uid())
                    .from(RelationshipHelper.teiItem(relationship229Compatible.trackedEntityInstanceA()))
                    .to(RelationshipHelper.teiItem(relationship229Compatible.trackedEntityInstanceB()))
                    .build();
        } else {
            return builder
                    .uid(relationship229Compatible.uid())
                    .relationshipType(relationship229Compatible.relationshipType())
                    .from(relationship229Compatible.from())
                    .to(relationship229Compatible.to())
                    .build();
        }
    }

    public TrackedEntityInstance getRelativeTei(Relationship229Compatible relationship229Compatible, String teiUid) {
        if (versionManager.is2_29()) {
            return relationship229Compatible.relative();
        } else {
            return getRelative230(relationship229Compatible, teiUid);
        }
    }

    boolean isRelationshipSupported(BaseRelationship relationship) {
        return isItemSupported(relationship.from()) && isItemSupported(relationship.to());
    }

    private boolean isItemSupported(RelationshipItem item) {
        if (versionManager.is2_29()) {
            return item.hasTrackedEntityInstance();
        } else {
            return true;
        }
    }

    private TrackedEntityInstance getRelative230(BaseRelationship relationship229Compatible, String teiUid) {
        String fromTEIUid = RelationshipHelper.getTeiUid(relationship229Compatible.from());
        String toTEIUid = RelationshipHelper.getTeiUid(relationship229Compatible.to());

        if (fromTEIUid == null || toTEIUid == null) {
            return null;
        }

        String relatedTEIUid = teiUid.equals(fromTEIUid) ? toTEIUid : fromTEIUid;

        return TrackedEntityInstance.builder()
                .uid(relatedTEIUid)
                .deleted(false)
                .relationships(Collections.emptyList())
                .build();
    }
}