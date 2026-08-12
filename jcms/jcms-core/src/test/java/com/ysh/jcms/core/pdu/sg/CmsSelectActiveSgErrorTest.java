package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectActiveSgErrorTest {
    @Test
    public void roundup() {
        CmsSelectActiveSgError a = new CmsSelectActiveSgError(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsSelectActiveSgError b = new CmsSelectActiveSgError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
