package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsQueryLogByTimeErrorTest {
    @Test
    public void roundup() {
        CmsQueryLogByTimeError a = new CmsQueryLogByTimeError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsQueryLogByTimeError b = new CmsQueryLogByTimeError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
