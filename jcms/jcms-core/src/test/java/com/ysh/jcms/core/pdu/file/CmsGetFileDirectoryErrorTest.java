package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetFileDirectoryErrorTest {
    @Test
    public void roundup() {
        CmsGetFileDirectoryError a = new CmsGetFileDirectoryError(CmsServiceError.FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT);
        byte[] encoded = a.encode();

        CmsGetFileDirectoryError b = new CmsGetFileDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
