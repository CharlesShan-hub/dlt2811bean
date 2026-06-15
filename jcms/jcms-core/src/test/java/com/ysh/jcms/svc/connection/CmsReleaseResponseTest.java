package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsReleaseResponseTest {
    @Test
    public void roundup() {
        CmsReleaseResponse a = new CmsReleaseResponse()
            .reqId(30)
            .assocId(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05})
            .serviceError(CmsServiceError.NO_ERROR);
        byte[] encoded = a.encode();

        CmsReleaseResponse b = new CmsReleaseResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
