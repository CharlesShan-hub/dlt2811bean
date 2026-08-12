package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetFileErrorTest {
    @Test
    public void roundup() {
        CmsSetFileError a = new CmsSetFileError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsSetFileError b = new CmsSetFileError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
