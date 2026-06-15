package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.time.CmsTimeQuality;
import com.ysh.jcms.data.time.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateRequestTest {
    @Test
    public void roundup_without_optional() {
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
    public void roundup_with_sap_ref() {
        CmsAssociateRequest a = new CmsAssociateRequest()
            .reqId(2)
            .sapRefPresent(true)
            .sapRef("MyAccessPoint".getBytes())
            .authParamPresent(false);
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundup_with_both_optional() {
        CmsAssociateRequest a = new CmsAssociateRequest()
            .reqId(3)
            .sapRefPresent(true)
            .sapRef("sapRef".getBytes())
            .authParamPresent(true)
            .authParam(new CmsAuthenticationParameter()
                .cert(new byte[]{0x11, 0x22})
                .signedTime(new CmsUtcTime()
                    .seconds_since_epoch(1234567890L)
                    .fraction_of_second(0)
                    .time_quality(new CmsTimeQuality()
                        .leap_seconds_known(true)))
                .sigVal(new byte[]{0x33, 0x44}));
        byte[] encoded = a.encode();

        CmsAssociateRequest b = new CmsAssociateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
