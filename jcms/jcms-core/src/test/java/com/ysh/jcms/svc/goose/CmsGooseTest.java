package com.ysh.jcms.svc.goose;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGooseTest {
    @Test
    public void get_gocb_request() {
        CmsGetGoCbValuesRequest a = new CmsGetGoCbValuesRequest()
            .reqId(1);
        a.reference.add(new CmsObjectReference("goref1".getBytes()));
        byte[] encoded = a.encode();

        CmsGetGoCbValuesRequest b = new CmsGetGoCbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void send_goose_message() {
        CmsSendGooseMessage a = new CmsSendGooseMessage()
            .reqId(20)
            .goId("goID1".getBytes())
            .datSetPresent(false)
            .goRefPresent(false)
            .stNum(1L)
            .sqNum(100L)
            .simulation(false)
            .confRev(5L)
            .ndsCom(false);
        a.t.secondsSinceEpoch.value(1000000L);
        a.t.fractionOfSecond.value(0);
        a.t.timeQuality.leap_seconds_known.value(false);
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
        CmsGetGoCbValuesError a = new CmsGetGoCbValuesError()
            .reqId(99)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetGoCbValuesError b = new CmsGetGoCbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
