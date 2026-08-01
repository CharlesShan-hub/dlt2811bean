package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetUrcbValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetUrcbValuesError a = new CmsGetUrcbValuesError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetUrcbValuesError b = new CmsGetUrcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
