package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsReleaseErrorTest {
    @Test
    public void roundtrip() {
        CmsReleaseError a = new CmsReleaseError()
            .reqId(40)
            .serviceError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsReleaseError b = new CmsReleaseError();
        b.decode(encoded);
        assertEquals(40, b.reqId.value());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, b.serviceError.value());
    }
}
