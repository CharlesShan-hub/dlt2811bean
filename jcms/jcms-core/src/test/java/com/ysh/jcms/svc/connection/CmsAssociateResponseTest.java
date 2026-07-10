package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateResponseTest {
    @Test
    public void roundup_without_auth() {
        CmsAssociateResponse a = new CmsAssociateResponse().reqId(10).assocId(new byte[]{0x01, 0x02, 0x03, 0x04})
                .serviceError(CmsServiceError.NO_ERROR).authParamPresent(false);
        byte[] encoded = a.encode();

        CmsAssociateResponse b = new CmsAssociateResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundup_with_auth() {
        CmsAssociateResponse a = new CmsAssociateResponse().reqId(11).assocId(new byte[]{0x05, 0x06, 0x07, 0x08})
                .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).authParamPresent(true);
        a.authParam.cert.value(new byte[]{0x11, 0x22});
        a.authParam.signedTime.secondsSinceEpoch.value(987654321L);
        a.authParam.signedTime.fractionOfSecond.value(100000);
        a.authParam.signedTime.timeQuality.leap_seconds_known.value(false);
        a.authParam.sigVal.value(new byte[]{0x33, 0x44});
        byte[] encoded = a.encode();

        CmsAssociateResponse b = new CmsAssociateResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
