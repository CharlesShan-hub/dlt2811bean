package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.time.CmsTimeQuality;
import com.ysh.jcms.data.time.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateRequestTest {
    @Test
    public void roundup_without_optional() {
        CmsAssociateRequest a = new CmsAssociateRequest();
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundup_with_sap_ref() {
        CmsAssociateRequest a = new CmsAssociateRequest().sapRef("MyAccessPoint");
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundup_with_both_optional() {
        CmsAssociateRequest a = new CmsAssociateRequest()
                .sapRef("sapRef")
                .authParam(new CmsAuthenticationParameter());
        a.authParam.signature.value(new byte[]{0x11, 0x22});
        a.authParam.signedTime = new CmsUtcTime()
                .secondsSinceEpoch(1234567890L).fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(true));
        a.authParam.signedValue.value(new byte[]{0x33, 0x44});
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
