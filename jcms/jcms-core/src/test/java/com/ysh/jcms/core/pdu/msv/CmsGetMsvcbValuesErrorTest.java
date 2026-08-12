package com.ysh.jcms.core.pdu.msv;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetMsvcbValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetMsvcbValuesError a = new CmsGetMsvcbValuesError(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetMsvcbValuesError b = new CmsGetMsvcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
