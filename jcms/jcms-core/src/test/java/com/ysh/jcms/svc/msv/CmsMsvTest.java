package com.ysh.jcms.svc.msv;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsMsvTest {
    @Test
    public void send_msv_message() {
        CmsSendMsvMessage a = new CmsSendMsvMessage()
            .reqId(30)
            .msvId("msvId".getBytes())
            .datSetPresent(false)
            .smpCnt(1)
            .confRev(5L)
            .refTmPresent(false)
            .smpSynch(0)
            .smpRatePresent(false)
            .simulation(false)
            .smpModPresent(false);
        CmsData d1 = new CmsData();
        d1.choice.value(CmsData.CHOICE_INT32);
        d1.alt_int32.value(100);
        a.sample.add(d1);
        byte[] encoded = a.encode();

        CmsSendMsvMessage b = new CmsSendMsvMessage();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void simple_error() {
        CmsGetMsvcbValuesError a = new CmsGetMsvcbValuesError()
            .reqId(99)
            .serviceError(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetMsvcbValuesError b = new CmsGetMsvcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
