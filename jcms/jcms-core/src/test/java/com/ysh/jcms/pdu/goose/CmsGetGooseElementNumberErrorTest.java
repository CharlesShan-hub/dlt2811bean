package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetGooseElementNumberErrorTest {
    @Test
    public void roundup() {
        CmsGetGooseElementNumberError a = new CmsGetGooseElementNumberError(CmsServiceError.CLASS_NOT_SUPPORTED);
        byte[] encoded = a.encode();

        CmsGetGooseElementNumberError b = new CmsGetGooseElementNumberError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
