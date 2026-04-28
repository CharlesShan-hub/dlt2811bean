package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAssociate")
class CmsAssociateTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
            .serverAccessPointReference("IED1", "AP1")
            .authenticationParameter(new AuthenticationParameter()
                .signatureCertificate(new byte[]{0x01, 0x02, 0x03})
                .signedTime(1715000000L, 1234567, 4)
                .signedValue(new byte[]{0x0A, 0x0B}))
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.REQUEST);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsAssociate result = (CmsAssociate) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1", result.serverAccessPointReference().get());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03},
            result.authenticationParameter().signatureCertificate.get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsAssociate asdu = new CmsAssociate(MessageType.RESPONSE_POSITIVE)
            .associationId(new byte[]{
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
                0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
                0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27,
                0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F,
                0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
                0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F
            })
            .authenticationParameter(new AuthenticationParameter()
                .signatureCertificate(new byte[]{0x10, 0x20})
                .signedTime(1715000000L, 1234567, 4)
                .signedValue(new byte[]{0x30, 0x40}))
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.RESPONSE_POSITIVE);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsAssociate result = (CmsAssociate) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(CmsServiceError.NO_ERROR, result.serviceError().get());
        assertArrayEquals(new byte[]{0x10, 0x20},
            result.authenticationParameter().signatureCertificate.get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsAssociate asdu = new CmsAssociate(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.RESPONSE_NEGATIVE);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsAssociate result = (CmsAssociate) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsAssociate service = new CmsAssociate(MessageType.REQUEST)
            .serverAccessPointReference("IED2", "AP2")
            .reqId(5);

        PerOutputStream pos = new PerOutputStream();
        CmsAssociate.write(pos, service);

        CmsAssociate result = CmsAssociate.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(5, result.reqId().get());
        assertEquals("IED2.AP2", result.serverAccessPointReference().get());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsAssociate original = new CmsAssociate(MessageType.REQUEST)
            .serverAccessPointReference("IED1", "AP1")
            .reqId(10);

        CmsAssociate copy = (CmsAssociate) original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.serverAccessPointReference().get(), copy.serverAccessPointReference().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsAssociate asdu = new CmsAssociate(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsAssociate asdu = new CmsAssociate(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsAssociate asdu = new CmsAssociate(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }
}
