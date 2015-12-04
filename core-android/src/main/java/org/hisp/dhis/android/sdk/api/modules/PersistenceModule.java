/*
 * Copyright (c) 2015, University of Oslo
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

package org.hisp.dhis.android.sdk.api.modules;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.hisp.dhis.android.sdk.common.FailedItemStore;
import org.hisp.dhis.android.sdk.common.base.IMapper;
import org.hisp.dhis.android.sdk.common.state.IStateMapper;
import org.hisp.dhis.android.sdk.common.state.StateMapper;
import org.hisp.dhis.android.sdk.constant.ConstantMapper;
import org.hisp.dhis.android.sdk.constant.ConstantStore;
import org.hisp.dhis.android.sdk.dashboard.DashboardContentMapper;
import org.hisp.dhis.android.sdk.dashboard.DashboardContentStore;
import org.hisp.dhis.android.sdk.dashboard.DashboardElementMapper;
import org.hisp.dhis.android.sdk.dashboard.DashboardItemMapper;
import org.hisp.dhis.android.sdk.dashboard.DashboardItemStore;
import org.hisp.dhis.android.sdk.dashboard.DashboardMapper;
import org.hisp.dhis.android.sdk.dashboard.DashboardStore;
import org.hisp.dhis.android.sdk.dataelement.DataElementMapper;
import org.hisp.dhis.android.sdk.dataelement.DataElementStore;
import org.hisp.dhis.android.sdk.dataset.DataSetStore;
import org.hisp.dhis.android.sdk.enrollment.EnrollmentMapper;
import org.hisp.dhis.android.sdk.enrollment.EnrollmentStore;
import org.hisp.dhis.android.sdk.event.EventMapper;
import org.hisp.dhis.android.sdk.event.EventStore;
import org.hisp.dhis.android.sdk.flow.Constant$Flow;
import org.hisp.dhis.android.sdk.flow.Dashboard$Flow;
import org.hisp.dhis.android.sdk.flow.DashboardContent$Flow;
import org.hisp.dhis.android.sdk.flow.DashboardElement$Flow;
import org.hisp.dhis.android.sdk.flow.DashboardItem$Flow;
import org.hisp.dhis.android.sdk.flow.DataElement$Flow;
import org.hisp.dhis.android.sdk.flow.DataSet$Flow;
import org.hisp.dhis.android.sdk.flow.Enrollment$Flow;
import org.hisp.dhis.android.sdk.flow.Event$Flow;
import org.hisp.dhis.android.sdk.flow.Option$Flow;
import org.hisp.dhis.android.sdk.flow.OptionSet$Flow;
import org.hisp.dhis.android.sdk.flow.OrganisationUnit$Flow;
import org.hisp.dhis.android.sdk.flow.Program$Flow;
import org.hisp.dhis.android.sdk.flow.ProgramIndicator$Flow;
import org.hisp.dhis.android.sdk.flow.ProgramRule$Flow;
import org.hisp.dhis.android.sdk.flow.ProgramRuleAction$Flow;
import org.hisp.dhis.android.sdk.flow.ProgramRuleVariable$Flow;
import org.hisp.dhis.android.sdk.flow.ProgramStage$Flow;
import org.hisp.dhis.android.sdk.flow.ProgramStageDataElement$Flow;
import org.hisp.dhis.android.sdk.flow.ProgramStageSection$Flow;
import org.hisp.dhis.android.sdk.flow.ProgramTrackedEntityAttribute$Flow;
import org.hisp.dhis.android.sdk.flow.Relationship$Flow;
import org.hisp.dhis.android.sdk.flow.RelationshipType$Flow;
import org.hisp.dhis.android.sdk.flow.TrackedEntity$Flow;
import org.hisp.dhis.android.sdk.flow.TrackedEntityAttribute$Flow;
import org.hisp.dhis.android.sdk.flow.TrackedEntityAttributeValue$Flow;
import org.hisp.dhis.android.sdk.flow.TrackedEntityDataValue$Flow;
import org.hisp.dhis.android.sdk.flow.TrackedEntityInstance$Flow;
import org.hisp.dhis.android.sdk.flow.User$Flow;
import org.hisp.dhis.android.sdk.flow.UserAccount$Flow;
import org.hisp.dhis.android.sdk.interpretation.InterpretationCommentStore;
import org.hisp.dhis.android.sdk.interpretation.InterpretationElementStore;
import org.hisp.dhis.android.sdk.interpretation.InterpretationStore;
import org.hisp.dhis.android.sdk.optionset.OptionMapper;
import org.hisp.dhis.android.sdk.optionset.OptionSetMapper;
import org.hisp.dhis.android.sdk.optionset.OptionSetStore;
import org.hisp.dhis.android.sdk.optionset.OptionStore;
import org.hisp.dhis.android.sdk.organisationunit.OrganisationUnitMapper;
import org.hisp.dhis.android.sdk.organisationunit.OrganisationUnitStore;
import org.hisp.dhis.android.sdk.program.ProgramIndicatorMapper;
import org.hisp.dhis.android.sdk.program.ProgramIndicatorStore;
import org.hisp.dhis.android.sdk.program.ProgramMapper;
import org.hisp.dhis.android.sdk.program.ProgramRuleActionMapper;
import org.hisp.dhis.android.sdk.program.ProgramRuleActionStore;
import org.hisp.dhis.android.sdk.program.ProgramRuleMapper;
import org.hisp.dhis.android.sdk.program.ProgramRuleStore;
import org.hisp.dhis.android.sdk.program.ProgramRuleVariableMapper;
import org.hisp.dhis.android.sdk.program.ProgramRuleVariableStore;
import org.hisp.dhis.android.sdk.program.ProgramStageDataElementMapper;
import org.hisp.dhis.android.sdk.program.ProgramStageDataElementStore;
import org.hisp.dhis.android.sdk.program.ProgramStageMapper;
import org.hisp.dhis.android.sdk.program.ProgramStageSectionMapper;
import org.hisp.dhis.android.sdk.program.ProgramStageSectionStore;
import org.hisp.dhis.android.sdk.program.ProgramStageStore;
import org.hisp.dhis.android.sdk.program.ProgramStore;
import org.hisp.dhis.android.sdk.program.ProgramTrackedEntityAttributeMapper;
import org.hisp.dhis.android.sdk.program.ProgramTrackedEntityAttributeStore;
import org.hisp.dhis.android.sdk.relationship.RelationshipStore;
import org.hisp.dhis.android.sdk.relationship.RelationshipTypeMapper;
import org.hisp.dhis.android.sdk.relationship.RelationshipTypeStore;
import org.hisp.dhis.android.sdk.trackedentity.TrackedEntityAttributeMapper;
import org.hisp.dhis.android.sdk.trackedentity.TrackedEntityAttributeStore;
import org.hisp.dhis.android.sdk.trackedentity.TrackedEntityAttributeValueStore;
import org.hisp.dhis.android.sdk.trackedentity.TrackedEntityDataValueMapper;
import org.hisp.dhis.android.sdk.trackedentity.TrackedEntityDataValueStore;
import org.hisp.dhis.android.sdk.trackedentity.TrackedEntityInstanceMapper;
import org.hisp.dhis.android.sdk.trackedentity.TrackedEntityInstanceStore;
import org.hisp.dhis.android.sdk.trackedentity.TrackedEntityMapper;
import org.hisp.dhis.android.sdk.trackedentity.TrackedEntityStore;
import org.hisp.dhis.android.sdk.user.UserAccountMapper;
import org.hisp.dhis.android.sdk.user.UserAccountStore;
import org.hisp.dhis.android.sdk.user.UserMapper;
import org.hisp.dhis.android.sdk.user.UserStore;
import org.hisp.dhis.java.sdk.common.IFailedItemStore;
import org.hisp.dhis.java.sdk.common.persistence.IIdentifiableObjectStore;
import org.hisp.dhis.java.sdk.common.persistence.IPersistenceModule;
import org.hisp.dhis.java.sdk.common.persistence.ITransactionManager;
import org.hisp.dhis.java.sdk.dashboard.IDashboardElementStore;
import org.hisp.dhis.java.sdk.dashboard.IDashboardItemContentStore;
import org.hisp.dhis.java.sdk.dashboard.IDashboardItemStore;
import org.hisp.dhis.java.sdk.dashboard.IDashboardStore;
import org.hisp.dhis.java.sdk.common.IStateStore;
import org.hisp.dhis.android.sdk.common.state.StateStore;
import org.hisp.dhis.android.sdk.dashboard.DashboardElementStore;
import org.hisp.dhis.java.sdk.dataset.IDataSetStore;
import org.hisp.dhis.java.sdk.enrollment.IEnrollmentStore;
import org.hisp.dhis.java.sdk.event.IEventStore;
import org.hisp.dhis.java.sdk.interpretation.IInterpretationCommentStore;
import org.hisp.dhis.java.sdk.interpretation.IInterpretationElementStore;
import org.hisp.dhis.java.sdk.models.constant.Constant;
import org.hisp.dhis.java.sdk.models.dashboard.Dashboard;
import org.hisp.dhis.java.sdk.models.dashboard.DashboardContent;
import org.hisp.dhis.java.sdk.models.dashboard.DashboardElement;
import org.hisp.dhis.java.sdk.models.dashboard.DashboardItem;
import org.hisp.dhis.java.sdk.models.dataelement.DataElement;
import org.hisp.dhis.java.sdk.models.dataset.DataSet;
import org.hisp.dhis.java.sdk.models.enrollment.Enrollment;
import org.hisp.dhis.java.sdk.models.event.Event;
import org.hisp.dhis.java.sdk.models.interpretation.Interpretation;
import org.hisp.dhis.java.sdk.models.optionset.Option;
import org.hisp.dhis.java.sdk.models.optionset.OptionSet;
import org.hisp.dhis.java.sdk.models.organisationunit.OrganisationUnit;
import org.hisp.dhis.java.sdk.models.program.Program;
import org.hisp.dhis.java.sdk.models.program.ProgramIndicator;
import org.hisp.dhis.java.sdk.models.program.ProgramRule;
import org.hisp.dhis.java.sdk.models.program.ProgramRuleAction;
import org.hisp.dhis.java.sdk.models.program.ProgramRuleVariable;
import org.hisp.dhis.java.sdk.models.program.ProgramStage;
import org.hisp.dhis.java.sdk.models.program.ProgramStageDataElement;
import org.hisp.dhis.java.sdk.models.program.ProgramStageSection;
import org.hisp.dhis.java.sdk.models.program.ProgramTrackedEntityAttribute;
import org.hisp.dhis.java.sdk.models.relationship.Relationship;
import org.hisp.dhis.java.sdk.models.relationship.RelationshipType;
import org.hisp.dhis.java.sdk.models.trackedentity.TrackedEntity;
import org.hisp.dhis.java.sdk.models.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.java.sdk.models.trackedentity.TrackedEntityAttributeValue;
import org.hisp.dhis.java.sdk.models.trackedentity.TrackedEntityDataValue;
import org.hisp.dhis.java.sdk.models.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.java.sdk.models.user.User;
import org.hisp.dhis.java.sdk.models.user.UserAccount;
import org.hisp.dhis.java.sdk.models.utils.IModelUtils;
import org.hisp.dhis.java.sdk.optionset.IOptionStore;
import org.hisp.dhis.java.sdk.organisationunit.IOrganisationUnitStore;
import org.hisp.dhis.java.sdk.program.IProgramIndicatorStore;
import org.hisp.dhis.java.sdk.program.IProgramRuleActionStore;
import org.hisp.dhis.java.sdk.program.IProgramRuleStore;
import org.hisp.dhis.java.sdk.program.IProgramRuleVariableStore;
import org.hisp.dhis.java.sdk.program.IProgramStageDataElementStore;
import org.hisp.dhis.java.sdk.program.IProgramStageSectionStore;
import org.hisp.dhis.java.sdk.program.IProgramStageStore;
import org.hisp.dhis.java.sdk.program.IProgramStore;
import org.hisp.dhis.java.sdk.program.IProgramTrackedEntityAttributeStore;
import org.hisp.dhis.java.sdk.relationship.IRelationshipStore;
import org.hisp.dhis.java.sdk.trackedentity.ITrackedEntityAttributeValueStore;
import org.hisp.dhis.java.sdk.trackedentity.ITrackedEntityDataValueStore;
import org.hisp.dhis.java.sdk.trackedentity.ITrackedEntityInstanceStore;
import org.hisp.dhis.java.sdk.user.IUserAccountStore;
import org.hisp.dhis.java.sdk.user.IUserStore;

public class PersistenceModule implements IPersistenceModule {
    private final IStateStore stateStore;
    private final IDashboardStore dashboardStore;
    private final IDashboardItemStore dashboardItemStore;
    private final IDashboardElementStore dashboardElementStore;
    private final IDashboardItemContentStore dashboardItemContentStore;

    // Meta data store objects
    private final IIdentifiableObjectStore<Constant> constantStore;
    private final IIdentifiableObjectStore<DataElement> dataElementStore;
    private final IOptionStore optionStore;
    private final IIdentifiableObjectStore<OptionSet> optionSetStore;
    private final IOrganisationUnitStore organisationUnitStore;
    private final IProgramStore programStore;
    private final IIdentifiableObjectStore<TrackedEntity> trackedEntityStore;
    private final IIdentifiableObjectStore<TrackedEntityAttribute> trackedEntityAttributeStore;
    private final IProgramTrackedEntityAttributeStore programTrackedEntityAttributeStore;
    private final IProgramStageDataElementStore programStageDataElementStore;
    private final IProgramIndicatorStore programIndicatorStore;
    private final IProgramStageSectionStore programStageSectionStore;
    private final IProgramStageStore programStageStore;
    private final IProgramRuleStore programRuleStore;
    private final IProgramRuleActionStore programRuleActionStore;
    private final IProgramRuleVariableStore programRuleVariableStore;
    private final IIdentifiableObjectStore<RelationshipType> relationshipTypeStore;

    private final IDataSetStore dataSetStore;

    //Tracker store objects
    private final ITrackedEntityAttributeValueStore trackedEntityAttributeValueStore;
    private final IRelationshipStore relationshipStore;
    private final ITrackedEntityInstanceStore trackedEntityInstanceStore;
    private final ITrackedEntityDataValueStore trackedEntityDataValueStore;
    private final IEventStore eventStore;
    private final IEnrollmentStore enrollmentStore;

    // Interpretation store objects
    private final IIdentifiableObjectStore<Interpretation> interpretationStore;
    private final IInterpretationCommentStore interpretationCommentStore;
    private final IInterpretationElementStore interpretationElementStore;

    // User store object
    private final IUserAccountStore userAccountStore;
    private final IUserStore userStore;

    private final IFailedItemStore failedItemStore;

    private final ITransactionManager transactionManager;
    private final IModelUtils modelUtils = null;//new ModelUtils();

    private final IStateMapper stateMapper;
    private final IMapper<Dashboard, Dashboard$Flow> dashboardMapper;
    private final IMapper<DashboardItem, DashboardItem$Flow> dashboardItemMapper;
    private final IMapper<DashboardElement, DashboardElement$Flow> dashboardElementMapper;
    private final IMapper<DashboardContent, DashboardContent$Flow> dashboardContentMapper;
    private IMapper<Event, Event$Flow> eventMapper = null;
    private IMapper<Enrollment, Enrollment$Flow> enrollmentMapper = null;
    private IMapper<TrackedEntityInstance, TrackedEntityInstance$Flow> trackedEntityInstanceMapper = null;
    private IMapper<TrackedEntityDataValue, TrackedEntityDataValue$Flow> trackedEntityDataValueMapper = null;
    private IMapper<TrackedEntityAttributeValue, TrackedEntityAttributeValue$Flow> trackedEntityAttributeValueMapper = null;
    private IMapper<Relationship, Relationship$Flow> relationshipMapper = null;
    private final IMapper<Constant, Constant$Flow> constantMapper;
    private final IMapper<DataElement, DataElement$Flow> dataElementMapper;
    private final IMapper<Option, Option$Flow> optionMapper;
    private final IMapper<OptionSet, OptionSet$Flow> optionSetMapper;
    private final IMapper<OrganisationUnit, OrganisationUnit$Flow> organisationUnitMapper;
    private final IMapper<Program, Program$Flow> programMapper;
    private final IMapper<TrackedEntity, TrackedEntity$Flow> trackedEntityMapper;
    private final IMapper<TrackedEntityAttribute, TrackedEntityAttribute$Flow> trackedEntityAttributeMapper;
    private final IMapper<ProgramTrackedEntityAttribute, ProgramTrackedEntityAttribute$Flow> programTrackedEntityAttributeMapper;
    private final IMapper<ProgramStageDataElement, ProgramStageDataElement$Flow> programStageDataElementMapper;
    private final IMapper<ProgramIndicator, ProgramIndicator$Flow> programIndicatorMapper;
    private final IMapper<ProgramStageSection, ProgramStageSection$Flow> programStageSectionMapper;
    private final IMapper<ProgramStage, ProgramStage$Flow> programStageMapper;
    private final IMapper<ProgramRule, ProgramRule$Flow> programRuleMapper;
    private final IMapper<ProgramRuleAction, ProgramRuleAction$Flow> programRuleActionMapper;
    private final IMapper<ProgramRuleVariable, ProgramRuleVariable$Flow> programRuleVariableMapper;
    private final IMapper<RelationshipType, RelationshipType$Flow> relationshipTypeMapper;
    private final IMapper<DataSet, DataSet$Flow> dataSetMapper;
    private final IMapper<UserAccount, UserAccount$Flow> userAccountMapper;
    private final IMapper<User, User$Flow> userMapper;

    public PersistenceModule(Context context) {
        FlowManager.init(context);
        transactionManager = new TransactionManager(modelUtils);

        stateMapper = new StateMapper();
        dashboardMapper = new DashboardMapper();
        dashboardItemMapper = new DashboardItemMapper(dashboardMapper);
        dashboardElementMapper = new DashboardElementMapper(dashboardItemMapper);
        dashboardContentMapper = new DashboardContentMapper();
        trackedEntityDataValueMapper = new TrackedEntityDataValueMapper(eventMapper);
        eventMapper = new EventMapper(enrollmentMapper, trackedEntityInstanceMapper, trackedEntityDataValueMapper);
        enrollmentMapper = new EnrollmentMapper(trackedEntityInstanceMapper, eventMapper, trackedEntityAttributeValueMapper);
        trackedEntityInstanceMapper = new TrackedEntityInstanceMapper(trackedEntityAttributeValueMapper, relationshipMapper);
        constantMapper = new ConstantMapper();
        dataElementMapper = new DataElementMapper();
        optionMapper = new OptionMapper();
        optionSetMapper = new OptionSetMapper(optionMapper);
        organisationUnitMapper = new OrganisationUnitMapper();
        trackedEntityMapper = new TrackedEntityMapper();
        trackedEntityAttributeMapper = new TrackedEntityAttributeMapper();
        programTrackedEntityAttributeMapper = new ProgramTrackedEntityAttributeMapper();
        programStageDataElementMapper = new ProgramStageDataElementMapper();
        programIndicatorMapper = new ProgramIndicatorMapper();
        programStageSectionMapper = new ProgramStageSectionMapper(programStageDataElementMapper, programIndicatorMapper);
        programStageMapper = new ProgramStageMapper(programStageDataElementMapper, programStageSectionMapper, programIndicatorMapper);
        programRuleActionMapper = new ProgramRuleActionMapper();
        programRuleMapper = new ProgramRuleMapper(programRuleActionMapper);
        programRuleVariableMapper = new ProgramRuleVariableMapper();
        relationshipTypeMapper = new RelationshipTypeMapper();
        programMapper = new ProgramMapper(programStageMapper, programTrackedEntityAttributeMapper);
        dataSetMapper = null;//new DataSetMapper();
        userAccountMapper = new UserAccountMapper();
        userMapper = new UserMapper();

        stateStore = new StateStore(stateMapper, dashboardMapper, dashboardItemMapper,
                dashboardElementMapper, eventMapper, enrollmentMapper, trackedEntityInstanceMapper);
        dashboardStore = new DashboardStore(dashboardMapper);
        dashboardItemStore = new DashboardItemStore(dashboardItemMapper);
        dashboardElementStore = new DashboardElementStore(dashboardElementMapper);
        dashboardItemContentStore = new DashboardContentStore(dashboardContentMapper);
        constantStore = new ConstantStore(constantMapper);
        dataElementStore = new DataElementStore(dataElementMapper);
        optionStore = new OptionStore(optionMapper);
        optionSetStore = new OptionSetStore(optionSetMapper, optionStore);
        organisationUnitStore = new OrganisationUnitStore(organisationUnitMapper);
        programStore = new ProgramStore(programMapper, transactionManager, organisationUnitMapper);
        trackedEntityStore = new TrackedEntityStore(trackedEntityMapper);
        trackedEntityAttributeStore = new TrackedEntityAttributeStore(trackedEntityAttributeMapper);
        programTrackedEntityAttributeStore = new ProgramTrackedEntityAttributeStore(programTrackedEntityAttributeMapper);
        programStageDataElementStore = new ProgramStageDataElementStore(programStageDataElementMapper);
        programIndicatorStore = new ProgramIndicatorStore(programIndicatorMapper);
        programStageSectionStore = new ProgramStageSectionStore(programStageSectionMapper);
        programStageStore = new ProgramStageStore(programStageMapper);
        programRuleActionStore = new ProgramRuleActionStore(programRuleActionMapper);
        programRuleStore = new ProgramRuleStore(programRuleMapper, programRuleActionStore, programRuleActionMapper);
        programRuleVariableStore = new ProgramRuleVariableStore(programRuleVariableMapper);
        relationshipTypeStore = new RelationshipTypeStore(relationshipTypeMapper);
        dataSetStore = new DataSetStore(dataSetMapper, organisationUnitMapper);
        trackedEntityAttributeValueStore = new TrackedEntityAttributeValueStore(trackedEntityAttributeValueMapper, stateStore, programStore);
        relationshipStore = new RelationshipStore(relationshipMapper, stateStore);
        trackedEntityInstanceStore = new TrackedEntityInstanceStore(trackedEntityInstanceMapper, stateStore);
        trackedEntityDataValueStore = new TrackedEntityDataValueStore(trackedEntityDataValueMapper, stateStore);
        eventStore = new EventStore(eventMapper, stateStore);
        enrollmentStore = new EnrollmentStore(eventStore, stateStore, trackedEntityAttributeValueStore, enrollmentMapper);
        interpretationStore = new InterpretationStore();
        interpretationCommentStore = new InterpretationCommentStore();
        interpretationElementStore = new InterpretationElementStore();
        userAccountStore = new UserAccountStore(userAccountMapper);
        userStore = new UserStore(userMapper);
        failedItemStore = new FailedItemStore();
    }

    @Override
    public ITransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public IStateStore getStateStore() {
        return stateStore;
    }

    @Override
    public IDashboardStore getDashboardStore() {
        return dashboardStore;
    }

    @Override
    public IDashboardItemStore getDashboardItemStore() {
        return dashboardItemStore;
    }

    @Override
    public IDashboardElementStore getDashboardElementStore() {
        return dashboardElementStore;
    }

    @Override
    public IDashboardItemContentStore getDashboardContentStore() {
        return dashboardItemContentStore;
    }

    @Override
    public IIdentifiableObjectStore<Constant> getConstantStore() {
        return constantStore;
    }

    @Override
    public IIdentifiableObjectStore<DataElement> getDataElementStore() {
        return dataElementStore;
    }

    @Override
    public IOptionStore getOptionStore() {
        return optionStore;
    }

    @Override
    public IIdentifiableObjectStore<OptionSet> getOptionSetStore() {
        return optionSetStore;
    }

    @Override
    public IOrganisationUnitStore getOrganisationUnitStore() {
        return organisationUnitStore;
    }

    @Override
    public IProgramStore getProgramStore() {
        return programStore;
    }

    @Override
    public IIdentifiableObjectStore<TrackedEntity> getTrackedEntityStore() {
        return trackedEntityStore;
    }

    @Override
    public IIdentifiableObjectStore<TrackedEntityAttribute> getTrackedEntityAttributeStore() {
        return trackedEntityAttributeStore;
    }

    @Override
    public IProgramTrackedEntityAttributeStore getProgramTrackedEntityAttributeStore() {
        return programTrackedEntityAttributeStore;
    }

    @Override
    public IProgramStageDataElementStore getProgramStageDataElementStore() {
        return programStageDataElementStore;
    }

    @Override
    public IProgramIndicatorStore getProgramIndicatorStore() {
        return programIndicatorStore;
    }

    @Override
    public IProgramStageSectionStore getProgramStageSectionStore() {
        return programStageSectionStore;
    }

    @Override
    public IProgramStageStore getProgramStageStore() {
        return programStageStore;
    }

    @Override
    public IProgramRuleStore getProgramRuleStore() {
        return programRuleStore;
    }

    @Override
    public IProgramRuleActionStore getProgramRuleActionStore() {
        return programRuleActionStore;
    }

    @Override
    public IProgramRuleVariableStore getProgramRuleVariableStore() {
        return programRuleVariableStore;
    }

    @Override
    public IIdentifiableObjectStore<RelationshipType> getRelationshipTypeStore() {
        return relationshipTypeStore;
    }

    @Override
    public IDataSetStore getDataStore() {
        return dataSetStore;
    }

    @Override
    public ITrackedEntityAttributeValueStore getTrackedEntityAttributeValueStore() {
        return trackedEntityAttributeValueStore;
    }

    @Override
    public IRelationshipStore getRelationshipStore() {
        return relationshipStore;
    }

    @Override
    public ITrackedEntityInstanceStore getTrackedEntityInstanceStore() {
        return trackedEntityInstanceStore;
    }

    @Override
    public ITrackedEntityDataValueStore getTrackedEntityDataValueStore() {
        return trackedEntityDataValueStore;
    }

    @Override
    public IEventStore getEventStore() {
        return eventStore;
    }

    @Override
    public IEnrollmentStore getEnrollmentStore() {
        return enrollmentStore;
    }

    @Override
    public IIdentifiableObjectStore<Interpretation> getInterpretationStore() {
        return interpretationStore;
    }

    @Override
    public IInterpretationCommentStore getInterpretationCommentStore() {
        return interpretationCommentStore;
    }

    @Override
    public IInterpretationElementStore getInterpretationElementStore() {
        return interpretationElementStore;
    }

    @Override
    public IUserAccountStore getUserAccountStore() {
        return userAccountStore;
    }

    @Override
    public IUserStore getUserStore() {
        return userStore;
    }

    @Override
    public IFailedItemStore getFailedItemStore() {
        return failedItemStore;
    }
}
