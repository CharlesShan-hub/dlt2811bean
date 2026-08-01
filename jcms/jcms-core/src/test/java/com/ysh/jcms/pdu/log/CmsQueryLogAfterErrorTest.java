package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsQueryLogAfterErrorTest {
    @Test
    public void roundup() {
        CmsQueryLogAfterError a = new CmsQueryLogAfterError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsQueryLogAfterError b = new CmsQueryLogAfterError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
