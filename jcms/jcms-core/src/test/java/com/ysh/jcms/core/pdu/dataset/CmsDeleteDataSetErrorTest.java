package com.ysh.jcms.core.pdu.dataset;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDeleteDataSetErrorTest {
    @Test
    public void roundup() {
        CmsDeleteDataSetError a = new CmsDeleteDataSetError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsDeleteDataSetError b = new CmsDeleteDataSetError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
