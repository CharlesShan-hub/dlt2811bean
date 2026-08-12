package com.ysh.jcms.core.pdu.connection;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateErrorTest {
    @Test
    public void roundup() {
        CmsAssociateError a = new CmsAssociateError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsAssociateError b = new CmsAssociateError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
