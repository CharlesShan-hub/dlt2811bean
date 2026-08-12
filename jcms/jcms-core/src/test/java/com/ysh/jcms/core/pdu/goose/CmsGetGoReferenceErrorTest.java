package com.ysh.jcms.core.pdu.goose;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetGoReferenceErrorTest {
    @Test
    public void roundup() {
        CmsGetGoReferenceError a = new CmsGetGoReferenceError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsGetGoReferenceError b = new CmsGetGoReferenceError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
