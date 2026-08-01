package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.enumerate.CmsServiceError;
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
