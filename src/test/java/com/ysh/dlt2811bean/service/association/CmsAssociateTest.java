package com.ysh.dlt2811bean.service.association;

import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.enums.MessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAssociate")
class CmsAssociateTest {

    @Test
    @DisplayName("encode and decode REQUEST")
    void request() throws Exception {
        CmsAssociate req = new CmsAssociate(MessageType.REQUEST);
        req.setReqId(new byte[]{0x00, 0x01});
        req.setServerAccessPointReference(new ServerAccessPointReference("MyServer", "AP1"));
        AuthenticationParameter authParam = new AuthenticationParameter();
        authParam.signatureCertificate().set(new byte[]{0x01, 0x02, 0x03});
        req.setAuthenticationParameter(authParam);

        byte[] frame = req.encode();

        CmsAssociate decoded = new CmsAssociate(MessageType.REQUEST);
        decoded.decode(frame);

        assertEquals(MessageType.REQUEST, decoded.getMessageType());
        assertArrayEquals(new byte[]{0x00, 0x01}, decoded.getReqId().get());
        assertEquals("MyServer.AP1", decoded.getServerAccessPointReference().get());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, decoded.getAuthenticationParameter().signatureCertificate().get());
    }

    @Test
    @DisplayName("encode and decode RESPONSE_POSITIVE")
    void responsePositive() throws Exception {
        CmsAssociate resp = new CmsAssociate(MessageType.RESPONSE_POSITIVE);
        resp.getReqId().set(new byte[]{0x00, 0x01});
        resp.setAssociationId(new CmsOctetString(new byte[64]).size(64));
        resp.setResult(new CmsServiceError(CmsServiceError.NO_ERROR));

        byte[] frame = resp.encode();

        CmsAssociate decoded = new CmsAssociate(MessageType.RESPONSE_POSITIVE);
        decoded.decode(frame);

        assertEquals(MessageType.RESPONSE_POSITIVE, decoded.getMessageType());
        assertArrayEquals(new byte[]{0x00, 0x01}, decoded.getReqId().get());
        assertEquals(CmsServiceError.NO_ERROR, decoded.getResult().get());
    }

    @Test
    @DisplayName("encode and decode RESPONSE_NEGATIVE")
    void responseNegative() throws Exception {
        CmsAssociate resp = new CmsAssociate(MessageType.RESPONSE_NEGATIVE);
        resp.getReqId().set(new byte[]{0x00, 0x01});
        resp.setServiceError(new CmsServiceError(CmsServiceError.ACCESS_VIOLATION));

        byte[] frame = resp.encode();

        CmsAssociate decoded = new CmsAssociate(MessageType.RESPONSE_NEGATIVE);
        decoded.decode(frame);

        assertEquals(MessageType.RESPONSE_NEGATIVE, decoded.getMessageType());
        assertArrayEquals(new byte[]{0x00, 0x01}, decoded.getReqId().get());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, decoded.getServiceError().get());
    }

    @Test
    @DisplayName("APCH header flags are set correctly for each message type")
    void apchFlags() throws Exception {
        CmsAssociate req = new CmsAssociate(MessageType.REQUEST);
        req.setReqId(new byte[]{0x00, 0x01});
        req.setServerAccessPointReference(new ServerAccessPointReference("test", "AP"));
        byte[] reqFrame = req.encode();
        assertEquals(MessageType.REQUEST, req.getMessageType());
        assertFalse((reqFrame[2] & 0x80) != 0, "Request should have Resp=0");

        CmsAssociate resp = new CmsAssociate(MessageType.RESPONSE_POSITIVE);
        resp.setReqId(new byte[]{0x00, 0x01});
        resp.setAssociationId(new CmsOctetString(new byte[64]).size(64));
        resp.setResult(new CmsServiceError(CmsServiceError.NO_ERROR));
        byte[] respFrame = resp.encode();
        assertTrue((respFrame[2] & 0x80) != 0, "Response should have Resp=1");
        assertFalse((respFrame[2] & 0x40) != 0, "Positive response should have Err=0");

        CmsAssociate neg = new CmsAssociate(MessageType.RESPONSE_NEGATIVE);
        neg.setReqId(new byte[]{0x00, 0x01});
        neg.setServiceError(new CmsServiceError(CmsServiceError.ACCESS_VIOLATION));
        byte[] negFrame = neg.encode();
        assertTrue((negFrame[2] & 0x80) != 0, "Negative response should have Resp=1");
        assertTrue((negFrame[2] & 0x40) != 0, "Negative response should have Err=1");
    }

    @Test
    @DisplayName("toString includes fields based on message type")
    void toStringFormat() {
        CmsAssociate req = new CmsAssociate(MessageType.REQUEST);
        req.getReqId().set(new byte[]{0x00, 0x01});
        req.setServerAccessPointReference(new ServerAccessPointReference("MyServer", "AP1"));
        String reqStr = req.toString();
        assertTrue(reqStr.contains("serverAccessPointReference"));
        assertTrue(reqStr.contains("MyServer.AP1"));

        CmsAssociate neg = new CmsAssociate(MessageType.RESPONSE_NEGATIVE);
        neg.getReqId().set(new byte[]{0x00, 0x01});
        neg.setServiceError(new CmsServiceError(CmsServiceError.ACCESS_VIOLATION));
        String negStr = neg.toString();
        assertTrue(negStr.contains("serviceError"));
    }
}
