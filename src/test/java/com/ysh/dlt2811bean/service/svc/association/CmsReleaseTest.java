package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsRelease")
class CmsReleaseTest implements
        ServiceNameTest<CmsRelease>,
        CopyTest<CmsRelease>,
        FromFlagsTest<CmsRelease> {

    private static byte[] assocId() {
        byte[] id = new byte[64];
        for (int i = 0; i < 64; i++) {
            id[i] = (byte) i;
        }
        return id;
    }

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.RELEASE;
    }

    @Override
    public CmsRelease createAsdu() {
        return new CmsRelease(MessageType.REQUEST);
    }

    @Override
    public CmsRelease createCopyableAsdu() {
        return new CmsRelease(MessageType.REQUEST)
                .associationId(assocId())
                .reqId(10);
    }

    @Override
    public CmsRelease createFromFlags(boolean resp, boolean err) {
        return new CmsRelease(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsRelease result = AsduTestUtil.roundTripViaApdu(
                new CmsRelease(MessageType.REQUEST)
                        .associationId(assocId())
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertArrayEquals(assocId(), result.associationId().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsRelease result = AsduTestUtil.roundTripViaApdu(
                new CmsRelease(MessageType.RESPONSE_POSITIVE)
                        .associationId(assocId())
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertArrayEquals(assocId(), result.associationId().get());
        assertEquals(CmsServiceError.NO_ERROR, result.serviceError().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsRelease result = AsduTestUtil.roundTripViaApdu(
                new CmsRelease(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(3));

        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsRelease result = AsduTestUtil.roundTripViaAsdu(
                new CmsRelease(MessageType.REQUEST)
                        .associationId(assocId())
                        .reqId(5));

        assertEquals(5, result.reqId().get());
        assertArrayEquals(assocId(), result.associationId().get());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsRelease asdu = new CmsRelease(MessageType.REQUEST)
                .associationId(assocId())
                .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsRelease) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
        assertTrue(str.contains("associationId: (CmsOctetString)"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsRelease asdu = new CmsRelease(MessageType.RESPONSE_NEGATIVE)
                .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsRelease) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
