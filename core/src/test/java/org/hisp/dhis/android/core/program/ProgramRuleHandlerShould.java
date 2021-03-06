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

import org.hisp.dhis.android.core.arch.handlers.IdentifiableSyncHandlerImpl;
import org.hisp.dhis.android.core.arch.handlers.SyncHandler;
import org.hisp.dhis.android.core.common.HandleAction;
import org.hisp.dhis.android.core.common.IdentifiableObjectStore;
import org.hisp.dhis.android.core.common.OrphanCleaner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ProgramRuleHandlerShould {
    @Mock
    private IdentifiableObjectStore<ProgramRule> programRuleStore;

    @Mock
    private SyncHandler<ProgramRuleAction> programRuleActionHandler;

    @Mock
    private ProgramRule programRule;

    @Mock
    private OrphanCleaner<ProgramRule, ProgramRuleAction> programRuleActionCleaner;

    @Mock
    private List<ProgramRuleAction> programRuleActions;

    // object to test
    private IdentifiableSyncHandlerImpl<ProgramRule> programRuleHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        programRuleHandler = new ProgramRuleHandler(programRuleStore, programRuleActionHandler,
                programRuleActionCleaner);

        when(programRule.uid()).thenReturn("test_program_rule_uid");
        when(programRule.programRuleActions()).thenReturn(programRuleActions);
    }

    @Test
    public void extend_identifiable_sync_handler_impl() {
        IdentifiableSyncHandlerImpl<ProgramRule> genericHandler = new ProgramRuleHandler(null, null, null);
    }

    @Test
    public void call_program_rule_action_handler() {
        programRuleHandler.handle(programRule);
        verify(programRuleActionHandler).handleMany(programRuleActions);
    }

    @Test
    public void call_program_rule_action_orphan_cleaner_on_update() {
        when(programRuleStore.updateOrInsert(programRule)).thenReturn(HandleAction.Update);
        programRuleHandler.handle(programRule);
        verify(programRuleActionCleaner).deleteOrphan(programRule, programRuleActions);
    }

    @Test
    public void not_call_program_rule_action_orphan_cleaner_on_insert() {
        when(programRuleStore.updateOrInsert(programRule)).thenReturn(HandleAction.Insert);
        programRuleHandler.handle(programRule);
        verify(programRuleActionCleaner, never()).deleteOrphan(programRule, programRuleActions);
    }
}