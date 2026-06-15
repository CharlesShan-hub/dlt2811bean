package com.ysh.jcms.svc.negotiate;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsNegotiateTest {
    @Test
    public void request_roundtrip() {
        CmsNegotiateRequest a = new CmsNegotiateRequest();
        a.reqId.value(1);
        a.apduSize.value(1024);
        a.asduSize.value(65536L);
        a.protocolVersion.value(1L);
        byte[] encoded = a.encode();

        CmsNegotiateRequest b = new CmsNegotiateRequest();
        b.decode(encoded);
        assertEquals(1, b.reqId.value());
        assertEquals(1024, b.apduSize.value());
        assertEquals(65536L, b.asduSize.value());
        assertEquals(1L, b.protocolVersion.value());
    }

    @Test
    public void response_roundtrip() {
        CmsNegotiateResponse a = new CmsNegotiateResponse();
        a.reqId.value(2);
        a.apduSize.value(2048);
        a.asduSize.value(131072L);
        a.protocolVersion.value(2L);
        a.modelVersion.value("1.0".getBytes());
        byte[] encoded = a.encode();

        CmsNegotiateResponse b = new CmsNegotiateResponse();
        b.decode(encoded);
        assertEquals(2, b.reqId.value());
        assertEquals(2048, b.apduSize.value());
        assertEquals(131072L, b.asduSize.value());
        assertEquals(2L, b.protocolVersion.value());
        assertArrayEquals("1.0".getBytes(), b.modelVersion.value());
    }

    @Test
    public void error_roundtrip() {
        CmsNegotiateError a = new CmsNegotiateError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsNegotiateError b = new CmsNegotiateError();
        b.decode(encoded);
        assertEquals(99, b.reqId.value());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, b.serviceError.value());
    }
}
