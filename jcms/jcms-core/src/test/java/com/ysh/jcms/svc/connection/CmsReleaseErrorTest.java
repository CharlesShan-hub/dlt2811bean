package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsReleaseErrorTest {
    @Test
    public void roundup() {
        CmsReleaseError a = new CmsReleaseError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsReleaseError b = new CmsReleaseError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
