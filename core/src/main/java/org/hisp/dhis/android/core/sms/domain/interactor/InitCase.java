package org.hisp.dhis.android.core.sms.domain.interactor;

import io.reactivex.Completable;

public class InitCase {

    public InitCase() {
        // TODO waiting for implementation
    }

    public Completable initSMSModule() {
        // FIXME nothing to init?
        // TODO add sms commands if needed
        return Completable.complete();
    }
}
