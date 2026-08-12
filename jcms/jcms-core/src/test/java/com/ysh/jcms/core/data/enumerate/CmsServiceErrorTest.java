package com.ysh.jcms.core.data.enumerate;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsServiceErrorTest {
    @Test
    public void roundup() {
        CmsServiceError a = new CmsServiceError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();
        CmsServiceError b = new CmsServiceError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
