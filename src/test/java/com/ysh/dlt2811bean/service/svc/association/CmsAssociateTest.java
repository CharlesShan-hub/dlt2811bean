package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAssociate")
class CmsAssociateTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip")
    void requestRoundTrip() throws Exception {
        CmsAssociate service = new CmsAssociate(MessageType.REQUEST)
            .serverAccessPointReference("IED1", "AP1")
            .authenticationParameter(new AuthenticationParameter()
                .signatureCertificate(new byte[]{0x01, 0x02, 0x03})
                .signedTime(1715000000L, 1234567, 4)
                .signedValue(new byte[]{0x0A, 0x0B}))
            .reqId(1);

        PerOutputStream pos = new PerOutputStream();
        CmsAssociate.write(pos, service);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        CmsAssociate result = CmsAssociate.read(pis, MessageType.REQUEST);

        assertEquals(1, result.reqId());
        assertEquals("IED1.AP1", result.serverAccessPointReference().get());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03},
            result.authenticationParameter().signatureCertificate.get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsAssociate service = new CmsAssociate(MessageType.RESPONSE)
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

        PerOutputStream pos = new PerOutputStream();
        CmsAssociate.write(pos, service);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        CmsAssociate result = CmsAssociate.read(pis, MessageType.RESPONSE);

        assertEquals(1, result.reqId());
        assertEquals(CmsServiceError.NO_ERROR, result.result().get());
        assertArrayEquals(new byte[]{0x10, 0x20},
            result.authenticationParameter().signatureCertificate.get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsAssociate service = new CmsAssociate(MessageType.RESPONSE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(1);

        PerOutputStream pos = new PerOutputStream();
        CmsAssociate.write(pos, service);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        CmsAssociate result = CmsAssociate.read(pis, MessageType.RESPONSE);

        assertEquals(1, result.reqId());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }
}
