package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataSetValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetDataSetValuesError a = new CmsGetDataSetValuesError(CmsServiceError.FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT);
        byte[] encoded = a.encode();

        CmsGetDataSetValuesError b = new CmsGetDataSetValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
