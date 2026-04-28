package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsRelease")
class CmsReleaseTest {

    private static byte[] assocId() {
        byte[] id = new byte[64];
        for (int i = 0; i < 64; i++) {
            id[i] = (byte) i;
        }
        return id;
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsRelease asdu = new CmsRelease(MessageType.REQUEST)
            .associationId(assocId())
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.REQUEST);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsRelease result = (CmsRelease) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertArrayEquals(assocId(), result.associationId().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsRelease asdu = new CmsRelease(MessageType.RESPONSE_POSITIVE)
            .associationId(assocId())
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.RESPONSE_POSITIVE);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsRelease result = (CmsRelease) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertArrayEquals(assocId(), result.associationId().get());
        assertEquals(CmsServiceError.NO_ERROR, result.serviceError().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsRelease asdu = new CmsRelease(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.RESPONSE_NEGATIVE);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsRelease result = (CmsRelease) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsRelease service = new CmsRelease(MessageType.REQUEST)
            .associationId(assocId())
            .reqId(5);

        PerOutputStream pos = new PerOutputStream();
        CmsRelease.write(pos, service);

        CmsRelease result = CmsRelease.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(5, result.reqId().get());
        assertArrayEquals(assocId(), result.associationId().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsRelease original = new CmsRelease(MessageType.REQUEST)
            .associationId(assocId())
            .reqId(10);

        CmsRelease copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertArrayEquals(original.associationId().get(), copy.associationId().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsRelease asdu = new CmsRelease(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsRelease asdu = new CmsRelease(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsRelease asdu = new CmsRelease(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns RELEASE")
    void serviceCode() {
        CmsRelease asdu = new CmsRelease(MessageType.REQUEST);
        assertEquals(ServiceCode.RELEASE, asdu.getServiceCode());
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
