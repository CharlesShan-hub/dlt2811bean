package com.ysh.jcms.svc.msv;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsMsvTest {
    @Test
    public void send_msv_message() {
        CmsSendMsvMessage a = new CmsSendMsvMessage();
        a.reqId.value(30);
        a.msvId.value("msvId".getBytes());
        a.datSetPresent.value(false);
        a.smpCnt.value(1);
        a.confRev.value(5L);
        a.refTmPresent.value(false);
        a.smpSynch.value(0);
        a.smpRatePresent.value(false);
        a.simulation.value(false);
        CmsData d1 = new CmsData();
        d1.choice.value(CmsData.CHOICE_INT32);
        d1.alt_int32.value(100);
        a.sample.add(d1);
        a.smpModPresent.value(false);
        byte[] encoded = a.encode();

        CmsSendMsvMessage b = new CmsSendMsvMessage();
        b.decode(encoded);
        assertEquals(30, b.reqId.value());
        assertEquals(1, b.sample.size());
    }

    @Test
    public void simple_error() {
        CmsGetMsvcbValuesError a = new CmsGetMsvcbValuesError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetMsvcbValuesError b = new CmsGetMsvcbValuesError();
        b.decode(encoded);
        assertEquals(CmsServiceError.INSTANCE_IN_USE, b.serviceError.value());
    }
}
