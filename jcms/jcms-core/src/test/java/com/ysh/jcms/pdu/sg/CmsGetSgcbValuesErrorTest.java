package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetSgcbValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetSgcbValuesError a = new CmsGetSgcbValuesError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetSgcbValuesError b = new CmsGetSgcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
