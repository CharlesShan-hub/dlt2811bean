package com.ysh.jcms.core.data.sequence.connection;

import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAuthenticationParameterTest {

    @Test
    public void roundup_with_setters() {
        CmsAuthenticationParameter a = new CmsAuthenticationParameter()
                .signatureCertificate(new byte[]{0x11, 0x22})
                .signedTime(new CmsUtcTime()
                        .secondsSinceEpoch(1234567890L)
                        .fractionOfSecond(0)
                        .timeQuality(new CmsTimeQuality()
                            .leap_seconds_known(true)  // 知道闰秒
                            .clock_failure(false)            // 没故障
                            .clock_not_synchronized(false)   // 已同步
                            .precision(24)))
                .signedValue(new byte[]{0x33, 0x44});

        byte[] encoded = a.encode();
        CmsAuthenticationParameter b = new CmsAuthenticationParameter();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundup_with_seconds_setter() {
        CmsAuthenticationParameter a = new CmsAuthenticationParameter()
                .signatureCertificate(new byte[]{0x01, 0x02})
                .signedTimeSeconds(1000000L)
                .signedValue(new byte[]{0x03, 0x04});

        byte[] encoded = a.encode();
        CmsAuthenticationParameter b = new CmsAuthenticationParameter();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
