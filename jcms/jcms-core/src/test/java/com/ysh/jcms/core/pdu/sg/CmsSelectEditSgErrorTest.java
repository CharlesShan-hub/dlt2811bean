package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectEditSgErrorTest {
    @Test
    public void roundup() {
        CmsSelectEditSgError a = new CmsSelectEditSgError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsSelectEditSgError b = new CmsSelectEditSgError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
