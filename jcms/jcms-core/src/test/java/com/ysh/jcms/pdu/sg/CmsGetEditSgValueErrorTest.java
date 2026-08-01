package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetEditSgValueErrorTest {
    @Test
    public void roundup() {
        CmsGetEditSgValueError a = new CmsGetEditSgValueError(CmsServiceError.CLASS_NOT_SUPPORTED);
        byte[] encoded = a.encode();

        CmsGetEditSgValueError b = new CmsGetEditSgValueError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
