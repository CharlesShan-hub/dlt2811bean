package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesErrorTest {
    @Test
    public void roundup() {
        CmsGetAllDataValuesError a = new CmsGetAllDataValuesError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesError b = new CmsGetAllDataValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
