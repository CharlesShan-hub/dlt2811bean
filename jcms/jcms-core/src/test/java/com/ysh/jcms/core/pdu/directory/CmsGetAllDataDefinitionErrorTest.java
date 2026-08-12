package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionErrorTest {
    @Test
    public void roundup() {
        CmsGetAllDataDefinitionError a = new CmsGetAllDataDefinitionError(CmsServiceError.TYPE_CONFLICT);
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionError b = new CmsGetAllDataDefinitionError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
