package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetServerDirectoryErrorTest {
    @Test
    public void roundup() {
        CmsGetServerDirectoryError a = new CmsGetServerDirectoryError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryError b = new CmsGetServerDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
