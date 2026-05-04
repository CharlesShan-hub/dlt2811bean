package com.ysh.dlt2811bean.service.svc.negotiation;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAssociateNegotiate")
class CmsAssociateNegotiateTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(MessageType.REQUEST)
            .apduSize(4096)
            .asduSize(65535L)
            .protocolVersion(1L)
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsAssociateNegotiate result = (CmsAssociateNegotiate) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(4096, result.apduSize().get());
        assertEquals(65535L, result.asduSize().get());
        assertEquals(1L, result.protocolVersion().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(MessageType.RESPONSE_POSITIVE)
            .apduSize(4096)
            .asduSize(65535L)
            .protocolVersion(1L)
            .modelVersion("1.0")
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsAssociateNegotiate result = (CmsAssociateNegotiate) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(4096, result.apduSize().get());
        assertEquals(65535L, result.asduSize().get());
        assertEquals(1L, result.protocolVersion().get());
        assertEquals("1.0", result.modelVersion().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsAssociateNegotiate result = (CmsAssociateNegotiate) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsAssociateNegotiate service = new CmsAssociateNegotiate(MessageType.REQUEST)
            .apduSize(2048)
            .asduSize(10000L)
            .protocolVersion(2L)
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsAssociateNegotiate.write(pos, service);

        CmsAssociateNegotiate result = CmsAssociateNegotiate.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals(2048, result.apduSize().get());
        assertEquals(10000L, result.asduSize().get());
        assertEquals(2L, result.protocolVersion().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsAssociateNegotiate original = new CmsAssociateNegotiate(MessageType.REQUEST)
            .apduSize(4096)
            .asduSize(65535L)
            .protocolVersion(1L)
            .reqId(10);

        CmsAssociateNegotiate copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.apduSize().get(), copy.apduSize().get());
        assertEquals(original.asduSize().get(), copy.asduSize().get());
        assertEquals(original.protocolVersion().get(), copy.protocolVersion().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns ASSOCIATE_NEGOTIATE")
    void serviceCode() {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(MessageType.REQUEST);
        assertEquals(ServiceName.ASSOCIATE_NEGOTIATE, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(MessageType.REQUEST)
            .apduSize(4096)
            .asduSize(65535L)
            .protocolVersion(1L)
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsAssociateNegotiate) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsAssociateNegotiate) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
