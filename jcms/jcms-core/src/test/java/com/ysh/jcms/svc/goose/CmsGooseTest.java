package com.ysh.jcms.svc.goose;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGooseTest {
    @Test
    public void get_gocb_request() {
        CmsGetGoCbValuesRequest a = new CmsGetGoCbValuesRequest();
        CmsObjectReference r1 = new CmsObjectReference("goref1".getBytes());
        a.reference.add(r1);
        a.reqId.value(1);
        byte[] encoded = a.encode();

        CmsGetGoCbValuesRequest b = new CmsGetGoCbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void send_goose_message() {
        CmsSendGooseMessage a = new CmsSendGooseMessage();
        a.reqId.value(20);
        a.goId.value("goID1".getBytes());
        a.datSetPresent.value(false);
        a.goRefPresent.value(false);
        a.t.seconds_since_epoch.value(1000000L);
        a.t.fraction_of_second.value(0);
        a.t.time_quality.leap_seconds_known.value(false);
        a.stNum.value(1L);
        a.sqNum.value(100L);
        a.simulation.value(false);
        a.confRev.value(5L);
        a.ndsCom.value(false);
        CmsData d1 = new CmsData();
        d1.choice.value(CmsData.CHOICE_BOOLEAN);
        d1.alt_boolean.value(true);
        a.data.add(d1);
        byte[] encoded = a.encode();

        CmsSendGooseMessage b = new CmsSendGooseMessage();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void simple_error() {
        CmsGetGoCbValuesError a = new CmsGetGoCbValuesError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetGoCbValuesError b = new CmsGetGoCbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
