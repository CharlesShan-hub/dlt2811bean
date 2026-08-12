package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogStatusValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetLogStatusValuesError a = new CmsGetLogStatusValuesError(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        byte[] encoded = a.encode();

        CmsGetLogStatusValuesError b = new CmsGetLogStatusValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
