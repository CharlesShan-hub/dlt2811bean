package com.ysh.jcms.svc.connection;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateRequestTest {
    @Test
    public void roundtrip_without_optional() {
        CmsAssociateRequest a = new CmsAssociateRequest();
        a.reqId.value(1);
        a.sapRefPresent.value(false);
        a.authParamPresent.value(false);
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(1, b.reqId.value());
        assertFalse(b.sapRefPresent.value());
        assertFalse(b.authParamPresent.value());
    }

    @Test
    public void roundtrip_with_sap_ref() {
        CmsAssociateRequest a = new CmsAssociateRequest();
        a.reqId.value(2);
        a.sapRefPresent.value(true);
        a.sapRef.value("MyAccessPoint".getBytes());
        a.authParamPresent.value(false);
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(2, b.reqId.value());
        assertTrue(b.sapRefPresent.value());
        assertArrayEquals("MyAccessPoint".getBytes(), b.sapRef.value());
        assertFalse(b.authParamPresent.value());
    }

    @Test
    public void roundtrip_with_both_optional() {
        CmsAssociateRequest a = new CmsAssociateRequest();
        a.reqId.value(3);
        a.sapRefPresent.value(true);
        a.sapRef.value("sapRef".getBytes());
        a.authParamPresent.value(true);
        a.authParam.cert.value(new byte[]{0x11, 0x22});
        a.authParam.signedTime.seconds_since_epoch.value(1234567890L);
        a.authParam.signedTime.fraction_of_second.value(0);
        a.authParam.signedTime.time_quality.leap_seconds_known.value(true);
        a.authParam.sigVal.value(new byte[]{0x33, 0x44});
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(3, b.reqId.value());
        assertTrue(b.sapRefPresent.value());
        assertArrayEquals("sapRef".getBytes(), b.sapRef.value());
        assertTrue(b.authParamPresent.value());
        assertArrayEquals(new byte[]{0x11, 0x22}, b.authParam.cert.value());
        assertEquals(1234567890L, b.authParam.signedTime.seconds_since_epoch.value());
        assertTrue(b.authParam.signedTime.time_quality.leap_seconds_known.value());
        assertArrayEquals(new byte[]{0x33, 0x44}, b.authParam.sigVal.value());
    }
}
