package com.ysh.jcms.svc.log;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsLogTest {
    @Test
    public void query_log_by_time_request() {
        CmsQueryLogByTimeRequest a = new CmsQueryLogByTimeRequest();
        a.reqId.value(30);
        a.logReference.value("logRef".getBytes());
        a.startTimePresent.value(false);
        a.stopTimePresent.value(false);
        a.entryAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsQueryLogByTimeRequest b = new CmsQueryLogByTimeRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void query_log_after_request() {
        CmsQueryLogAfterRequest a = new CmsQueryLogAfterRequest();
        a.reqId.value(40);
        a.logReference.value("logRef".getBytes());
        a.startTimePresent.value(false);
        a.entry.value(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        byte[] encoded = a.encode();

        CmsQueryLogAfterRequest b = new CmsQueryLogAfterRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void simple_error() {
        CmsGetLcbValuesError a = new CmsGetLcbValuesError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsGetLcbValuesError b = new CmsGetLcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
