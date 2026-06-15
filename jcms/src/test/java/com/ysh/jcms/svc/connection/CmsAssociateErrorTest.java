package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAssociateErrorTest {
    @Test
    public void roundtrip() {
        CmsAssociateError a = new CmsAssociateError();
        a.reqId.value(5);
        a.serviceError.value(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsAssociateError b = new CmsAssociateError();
        b.decode(encoded);
        assertEquals(5, b.reqId.value());
        assertEquals(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT, b.serviceError.value());
    }
}
