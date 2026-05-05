package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetFile")
class CmsGetFileTest implements ServiceNameTest<CmsGetFile> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_FILE;
    }

    @Override
    public CmsGetFile createAsdu() {
        return new CmsGetFile(MessageType.REQUEST);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetFile result = AsduTestUtil.roundTripViaApdu(
                new CmsGetFile(MessageType.REQUEST)
                        .fileName("report.txt")
                        .startPosition(1L)
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("report.txt", result.fileName().get());
        assertEquals(1L, result.startPosition().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetFile result = AsduTestUtil.roundTripViaApdu(
                new CmsGetFile(MessageType.RESPONSE_POSITIVE)
                        .fileData(new byte[]{0x01, 0x02, 0x03})
                        .endOfFile(true)
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, result.fileData().get());
        assertTrue(result.endOfFile().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetFile result = AsduTestUtil.roundTripViaApdu(
                new CmsGetFile(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(3));

        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }
}
