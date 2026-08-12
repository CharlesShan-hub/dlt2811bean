package com.ysh.jcms.core.pdu.dataset;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataSetDirectoryErrorTest {
    @Test
    public void roundup() {
        CmsGetDataSetDirectoryError a = new CmsGetDataSetDirectoryError(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        byte[] encoded = a.encode();

        CmsGetDataSetDirectoryError b = new CmsGetDataSetDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
