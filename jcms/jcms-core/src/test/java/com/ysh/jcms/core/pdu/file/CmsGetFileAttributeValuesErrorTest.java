package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetFileAttributeValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetFileAttributeValuesError a = new CmsGetFileAttributeValuesError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        byte[] encoded = a.encode();

        CmsGetFileAttributeValuesError b = new CmsGetFileAttributeValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
