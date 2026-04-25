package com.ysh.dlt2811bean.service.association;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.service.enums.MessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsRelease (SC=3)")
class CmsReleaseTest {

    @Test
    @DisplayName("encode+decode round-trip")
    void roundTrip() throws PerDecodeException {
        byte[] assocId = new byte[64];
        assocId[0] = 0x01;
        assocId[1] = (byte) 0xAB;
        assocId[63] = (byte) 0xFF;

        CmsRelease req = new CmsRelease();
        req.setAssociationId(assocId);

        byte[] frame = req.encode();

        CmsRelease decoded = new CmsRelease();
        decoded.decode(frame);
        assertArrayEquals(assocId, decoded.getAssociationId());
    }

    @Test
    @DisplayName("all-zeros associationId")
    void allZeros() throws PerDecodeException {
        byte[] assocId = new byte[64];

        CmsRelease req = new CmsRelease();
        req.setAssociationId(assocId);

        byte[] frame = req.encode();

        CmsRelease decoded = new CmsRelease();
        decoded.decode(frame);
        assertArrayEquals(assocId, decoded.getAssociationId());
    }

    @Test
    @DisplayName("APCH header: PI=0x01, SC=0x03")
    void header() {
        CmsRelease req = new CmsRelease();
        req.setAssociationId(new byte[64]);

        byte[] frame = req.encode();
        assertEquals(0x01, frame[0] & 0xFF);  // PI
        assertEquals(0x03, frame[1] & 0xFF);  // SC
    }

    @Test
    @DisplayName("response flag")
    void responseFlag() throws PerDecodeException {
        byte[] assocId = new byte[64];
        Arrays.fill(assocId, (byte) 0xAA);

        CmsRelease req = new CmsRelease();
        req.setMessageType(MessageType.RESPONSE_POSITIVE);
        req.setAssociationId(assocId);

        byte[] frame = req.encode();
        assertTrue((frame[2] & 0x80) != 0);

        CmsRelease decoded = new CmsRelease();
        decoded.decode(frame);
        assertEquals(MessageType.RESPONSE_POSITIVE, decoded.getMessageType());
        assertArrayEquals(assocId, decoded.getAssociationId());
    }

    @Test
    @DisplayName("frame length = APCH(5) + ASDU(64)")
    void frameLength() {
        CmsRelease req = new CmsRelease();
        req.setAssociationId(new byte[64]);

        byte[] frame = req.encode();
        assertEquals(5 + 64, frame.length);

        int fl = ((frame[3] & 0xFF) << 8) | (frame[4] & 0xFF);
        assertEquals(64, fl);
    }

    @Test
    @DisplayName("decode service code mismatch throws")
    void serviceCodeMismatch() {
        byte[] frame = {0x01, 0x02, 0x00, 0x00, 0x00};  // SC=2 but CmsRelease expects SC=3

        CmsRelease decoded = new CmsRelease();
        assertThrows(PerDecodeException.class, () -> decoded.decode(frame));
    }

    @Test
    @DisplayName("toString contains hex")
    void toStringTest() {
        byte[] assocId = new byte[64];
        assocId[0] = (byte) 0xDE;
        assocId[1] = (byte) 0xAD;

        CmsRelease req = new CmsRelease();
        req.setAssociationId(assocId);
        assertTrue(req.toString().contains("DEAD"));
    }
}
