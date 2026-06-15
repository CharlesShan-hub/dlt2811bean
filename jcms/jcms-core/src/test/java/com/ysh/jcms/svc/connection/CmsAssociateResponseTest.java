package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateResponseTest {
    @Test
    public void roundtrip_without_auth() {
        CmsAssociateResponse a = new CmsAssociateResponse()
            .reqId(10)
            .assocId(new byte[]{0x01, 0x02, 0x03, 0x04})
            .serviceError(CmsServiceError.NO_ERROR)
            .authParamPresent(false);
        byte[] encoded = a.encode();

        CmsAssociateResponse b = new CmsAssociateResponse();
        b.decode(encoded);
        assertEquals(10, b.reqId.value());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, b.assocId.value());
        assertEquals(CmsServiceError.NO_ERROR, b.serviceError.value());
        assertFalse(b.authParamPresent.value());
    }

    @Test
    public void roundtrip_with_auth() {
        CmsAssociateResponse a = new CmsAssociateResponse()
            .reqId(11)
            .assocId(new byte[]{0x05, 0x06, 0x07, 0x08})
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .authParamPresent(true);
        a.authParam.cert.value(new byte[]{0x11, 0x22});
        a.authParam.signedTime.seconds_since_epoch.value(987654321L);
        a.authParam.signedTime.fraction_of_second.value(100000);
        a.authParam.signedTime.time_quality.leap_seconds_known.value(false);
        a.authParam.sigVal.value(new byte[]{0x33, 0x44});
        byte[] encoded = a.encode();

        CmsAssociateResponse b = new CmsAssociateResponse();
        b.decode(encoded);
        assertEquals(11, b.reqId.value());
        assertArrayEquals(new byte[]{0x05, 0x06, 0x07, 0x08}, b.assocId.value());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, b.serviceError.value());
        assertTrue(b.authParamPresent.value());
        assertArrayEquals(new byte[]{0x11, 0x22}, b.authParam.cert.value());
        assertEquals(987654321L, b.authParam.signedTime.seconds_since_epoch.value());
        assertEquals(100000, b.authParam.signedTime.fraction_of_second.value());
        assertFalse(b.authParam.signedTime.time_quality.leap_seconds_known.value());
        assertArrayEquals(new byte[]{0x33, 0x44}, b.authParam.sigVal.value());
    }
}
