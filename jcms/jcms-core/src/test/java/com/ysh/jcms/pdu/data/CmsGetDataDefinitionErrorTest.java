package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataDefinitionErrorTest {
    @Test
    public void roundup() {
        CmsGetDataDefinitionError a = new CmsGetDataDefinitionError(CmsServiceError.CLASS_NOT_SUPPORTED);
        byte[] encoded = a.encode();

        CmsGetDataDefinitionError b = new CmsGetDataDefinitionError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
