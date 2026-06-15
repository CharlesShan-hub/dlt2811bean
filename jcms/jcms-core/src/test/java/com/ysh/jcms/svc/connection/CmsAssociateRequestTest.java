package com.ysh.jcms.svc.connection;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateRequestTest {
    @Test
    public void roundtrip_without_optional() {
        CmsAssociateRequest a = new CmsAssociateRequest()
            .reqId(1)
            .sapRefPresent(false)
            .authParamPresent(false);
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundtrip_with_sap_ref() {
        CmsAssociateRequest a = new CmsAssociateRequest()
            .reqId(2)
            .sapRefPresent(true)
            .sapRef("MyAccessPoint".getBytes());
        a.authParamPresent.value(false);
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundtrip_with_both_optional() {
        CmsAssociateRequest a = new CmsAssociateRequest()
            .reqId(3)
            .sapRefPresent(true)
            .sapRef("sapRef".getBytes())
            .authParamPresent(true);
        a.authParam.cert.value(new byte[]{0x11, 0x22});
        a.authParam.signedTime.seconds_since_epoch.value(1234567890L);
        a.authParam.signedTime.fraction_of_second.value(0);
        a.authParam.signedTime.time_quality.leap_seconds_known.value(true);
        a.authParam.sigVal.value(new byte[]{0x33, 0x44});
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
