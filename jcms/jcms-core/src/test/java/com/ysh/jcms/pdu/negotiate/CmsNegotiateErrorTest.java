package com.ysh.jcms.pdu.negotiate;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsNegotiateErrorTest {
    @Test
    public void roundup() {
        CmsNegotiateError a = new CmsNegotiateError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsNegotiateError b = new CmsNegotiateError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
