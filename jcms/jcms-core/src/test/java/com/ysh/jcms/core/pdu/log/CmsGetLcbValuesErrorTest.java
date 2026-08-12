package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLcbValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetLcbValuesError a = new CmsGetLcbValuesError(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsGetLcbValuesError b = new CmsGetLcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
