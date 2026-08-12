package com.ysh.jcms.core.pdu.data;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataDirectoryErrorTest {
    @Test
    public void roundup() {
        CmsGetDataDirectoryError a = new CmsGetDataDirectoryError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsGetDataDirectoryError b = new CmsGetDataDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
