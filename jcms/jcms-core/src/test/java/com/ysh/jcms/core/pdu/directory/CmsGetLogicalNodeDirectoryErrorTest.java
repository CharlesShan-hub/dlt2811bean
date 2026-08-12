package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryErrorTest {
    @Test
    public void roundup() {
        CmsGetLogicalNodeDirectoryError a = new CmsGetLogicalNodeDirectoryError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryError b = new CmsGetLogicalNodeDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
