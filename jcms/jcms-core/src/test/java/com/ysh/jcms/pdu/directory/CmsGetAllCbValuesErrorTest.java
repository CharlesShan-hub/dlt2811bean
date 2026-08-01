package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllCbValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetAllCbValuesError a = new CmsGetAllCbValuesError(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsGetAllCbValuesError b = new CmsGetAllCbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
