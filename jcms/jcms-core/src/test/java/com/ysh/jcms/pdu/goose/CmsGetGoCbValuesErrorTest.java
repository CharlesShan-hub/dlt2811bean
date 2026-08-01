package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetGoCbValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetGoCbValuesError a = new CmsGetGoCbValuesError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetGoCbValuesError b = new CmsGetGoCbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
