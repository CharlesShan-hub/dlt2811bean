package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateErrorTest {
    @Test
    public void roundtrip() {
        CmsAssociateError a = new CmsAssociateError()
            .reqId(5)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsAssociateError b = new CmsAssociateError();
        b.decode(encoded);
        assertEquals(5, b.reqId.value());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, b.serviceError.value());
    }
}
