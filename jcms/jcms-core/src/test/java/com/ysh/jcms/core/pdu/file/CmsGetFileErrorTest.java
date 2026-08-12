package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetFileErrorTest {
    @Test
    public void roundup() {
        CmsGetFileError a = new CmsGetFileError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetFileError b = new CmsGetFileError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
