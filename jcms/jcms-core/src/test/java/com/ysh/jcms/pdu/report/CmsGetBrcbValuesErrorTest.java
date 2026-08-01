package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetBrcbValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetBrcbValuesError a = new CmsGetBrcbValuesError(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetBrcbValuesError b = new CmsGetBrcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
