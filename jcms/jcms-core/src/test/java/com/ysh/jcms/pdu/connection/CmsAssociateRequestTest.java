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
                .authenticationParameter(new CmsAuthenticationParameter()
                        .signatureCertificate(new byte[]{0x11, 0x22})
                        .signedTime(new CmsUtcTime()
                                .secondsSinceEpoch(1234567890L)
                                .fractionOfSecond(0)
                                .timeQuality(new CmsTimeQuality()
                                    .leap_seconds_known(true)
                                    .clock_failure(true)))
                        .signedValue(new byte[]{0x33, 0x44}));
        System.out.println("before encode: packed=" + a.authenticationParameter.signedTime.timeQuality.packed());
        System.out.println("before encode: a.inner=" + a.inner);
        System.out.println("before encode: secondsSinceEpoch.value()=" + a.authenticationParameter.signedTime.secondsSinceEpoch.value());
        byte[] encoded = a.encode();
        System.out.println("after encode: a.inner=" + a.inner);

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);

        System.out.println(b.authenticationParameter.signedTime.timeQuality.clock_failure());
        System.out.println(b.authenticationParameter.signedTime.timeQuality.leap_seconds_known());
        System.out.println(b.authenticationParameter.signedTime.timeQuality.clock_not_synchronized());
        System.out.println(b.authenticationParameter.signedTime.timeQuality.packed());
        System.out.println(b.authenticationParameter.signedTime.timeQuality.inner);
        System.out.println(b.inner);
    }
}
