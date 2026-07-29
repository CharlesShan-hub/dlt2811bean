package com.ysh.jcms.pdu.connection;

import com.ysh.jcms.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.data.sequence.connection.CmsAuthenticationParameter;
import com.ysh.jcms.data.sequence.time.CmsUtcTime;
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
        CmsAssociateRequest a = new CmsAssociateRequest().serverAccessPointReference("MyAccessPoint");
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundup_with_both_optional() {
        CmsAssociateRequest a = new CmsAssociateRequest()
                .serverAccessPointReference("sapRef")
                .authenticationParameter(new CmsAuthenticationParameter());
        a.authenticationParameter.signatureCertificate.value(new byte[]{0x11, 0x22});
        a.authenticationParameter.signedTime = new CmsUtcTime()
                .secondsSinceEpoch(1234567890L).fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(true));
        a.authenticationParameter.signedValue.value(new byte[]{0x33, 0x44});
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
