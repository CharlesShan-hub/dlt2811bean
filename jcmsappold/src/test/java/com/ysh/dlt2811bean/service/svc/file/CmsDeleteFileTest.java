package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDeleteFile")
class CmsDeleteFileTest implements ServiceNameTest<CmsDeleteFile> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.DELETE_FILE;
    }

    @Override
    public CmsDeleteFile createAsdu() {
        return new CmsDeleteFile(MessageType.REQUEST);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsDeleteFile result = AsduTestUtil.roundTripViaApdu(
                new CmsDeleteFile(MessageType.REQUEST)
                        .fileName("report.txt")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("report.txt", result.fileName().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsDeleteFile result = AsduTestUtil.roundTripViaApdu(
                new CmsDeleteFile(MessageType.RESPONSE_POSITIVE).reqId(2));

        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsDeleteFile result = AsduTestUtil.roundTripViaApdu(
                new CmsDeleteFile(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(3));

        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }
}
