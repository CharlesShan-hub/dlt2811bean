package com.ysh.jcms.core.pdu.data;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetDataValuesError a = new CmsGetDataValuesError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        byte[] encoded = a.encode();

        CmsGetDataValuesError b = new CmsGetDataValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
