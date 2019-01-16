package org.hisp.dhis.android.core.sms.domain;

import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValue;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.internal.Util;

public class SmsFormatConverter {

    public String format(Event event, String username, String categoryOptionCombo) {
        if (event == null) {
            throw new NullPointerException("Event is null");
        }
        check(categoryOptionCombo);

        StringBuilder result = new StringBuilder();
        result
                .append(check(username))
                .append(' ')

                .append(check(event.organisationUnit()))
                .append(' ')

                .append(SubmissionType.PROGRAM_EVENT_NO_REG)
                .append(' ')

                .append(check(event.program()))
                .append(' ')

                .append(check(event.attributeOptionCombo()))
                .append(' ')

                .append(check(event.uid()))
                .append(' ')

                .append(timestamp())
                .append(' ');

        List<TrackedEntityDataValue> dataValues = event.trackedEntityDataValues();
        if (dataValues == null) {
            throw new NullPointerException("Event's data values are null");
        }
        for (TrackedEntityDataValue dataValue : dataValues) {
            result
                    .append('|')
                    .append(check(dataValue.dataElement()))
                    .append('-')
                    .append(categoryOptionCombo)
                    .append('=')
                    .append(check(dataValue.value(), false));
        }
        result.append('|');

        try {
            // TODO compress using compression library
            return addCheckSum(result.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String check(String value) {
        return check(value, true);
    }

    private String check(String value, boolean checkLength) {
        if (value == null) {
            throw new NullPointerException("Null value given to sms conversion");
        }
        if (checkLength && value.length() == 0) {
            throw new NullPointerException("Empty value given to sms conversion");
        }
        return value;
    }

    private String addCheckSum(String data) throws NoSuchAlgorithmException {
        MessageDigest mdEnc = MessageDigest.getInstance("MD5");
        mdEnc.update(data.getBytes(Util.UTF_8), 0, data.length());
        StringBuilder md5 = new StringBuilder(new BigInteger(1, mdEnc.digest()).toString(16));
        while (md5.length() < 32) {
            md5.insert(0, "0");
        }
        md5.append(data);
        return md5.toString();
    }

    private String timestamp() {
        // TODO discuss what timezone and format should be used. Always UTC?
        return new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss", Locale.ROOT).format(new Date());
    }

    private static class SubmissionType {
        public final static String AGGREGATE_DATA_SET = "DS";
        public final static String PROGRAM_EVENT_NO_REG = "SE";
        public final static String TRACKED_ENTITY_INSTANCE = "EN";
        public final static String ONE_WAY_RELATIONSHIP = "RS";
        public final static String TRACKER_EVENT = "TE";
        public final static String DELETE = "DE";
    }
}
