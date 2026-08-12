package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDeleteFileErrorTest {
    @Test
    public void roundup() {
        CmsDeleteFileError a = new CmsDeleteFileError(CmsServiceError.CLASS_NOT_SUPPORTED);
        byte[] encoded = a.encode();

        CmsDeleteFileError b = new CmsDeleteFileError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
