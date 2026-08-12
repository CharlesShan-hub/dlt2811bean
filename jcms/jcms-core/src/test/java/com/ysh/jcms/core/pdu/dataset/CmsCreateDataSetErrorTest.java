package com.ysh.jcms.core.pdu.dataset;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsCreateDataSetErrorTest {
    @Test
    public void roundup() {
        CmsCreateDataSetError a = new CmsCreateDataSetError(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsCreateDataSetError b = new CmsCreateDataSetError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
