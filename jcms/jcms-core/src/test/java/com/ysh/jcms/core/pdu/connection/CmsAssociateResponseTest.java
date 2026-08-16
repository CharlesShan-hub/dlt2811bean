package com.ysh.jcms.core.pdu.connection;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.sequence.connection.CmsAuthenticationParameter;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateResponseTest {
    @Test
    public void roundup_without_auth() {
        CmsAssociateResponse a = new CmsAssociateResponse()
                .associationId(new byte[]{0x01, 0x02, 0x03, 0x04})
                .serviceError(CmsServiceError.NO_ERROR);
        byte[] encoded = a.encode();

        CmsAssociateResponse b = new CmsAssociateResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundup_with_auth() {
        CmsAuthenticationParameter p = new CmsAuthenticationParameter();
        p.signatureCertificate.value(new byte[]{0x11, 0x22});
        p.signedTime(new CmsUtcTime()
                .secondsSinceEpoch(987654321L)
                .fractionOfSecond(100000)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(false)));
        p.signedValue.value(new byte[]{0x33, 0x44});

        CmsAssociateResponse a = new CmsAssociateResponse()
                .associationId(new byte[]{0x05, 0x06, 0x07, 0x08})
                .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                .authenticationParameter(p);
        byte[] encoded = a.encode();

        CmsAssociateResponse b = new CmsAssociateResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
