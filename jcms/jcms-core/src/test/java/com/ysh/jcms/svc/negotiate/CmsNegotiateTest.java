package com.ysh.jcms.svc.negotiate;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsNegotiateTest {
    @Test
    public void request_roundtrip() {
        CmsNegotiateRequest a = new CmsNegotiateRequest()
            .reqId(1)
            .apduSize(1024)
            .asduSize(65536L)
            .protocolVersion(1L);
        byte[] encoded = a.encode();

        CmsNegotiateRequest b = new CmsNegotiateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundtrip() {
        CmsNegotiateResponse a = new CmsNegotiateResponse()
            .reqId(2)
            .apduSize(2048)
            .asduSize(131072L)
            .protocolVersion(2L)
            .modelVersion("1.0".getBytes());
        byte[] encoded = a.encode();

        CmsNegotiateResponse b = new CmsNegotiateResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundtrip() {
        CmsNegotiateError a = new CmsNegotiateError()
            .reqId(99)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsNegotiateError b = new CmsNegotiateError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
