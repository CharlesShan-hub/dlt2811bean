package com.ysh.dlt2811bean.service;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cms03 Release (SC=3)")
class Cms03Test {

    @Test
    @DisplayName("encode+decode round-trip")
    void roundTrip() throws PerDecodeException {
        byte[] assocId = new byte[64];
        assocId[0] = 0x01;
        assocId[1] = (byte) 0xAB;
        assocId[63] = (byte) 0xFF;

        Cms03 req = new Cms03();
        req.setAssociationId(assocId);

        byte[] frame = req.encode();

        Cms03 decoded = new Cms03();
        decoded.decode(frame);
        assertArrayEquals(assocId, decoded.getAssociationId());
    }

    @Test
    @DisplayName("all-zeros associationId")
    void allZeros() throws PerDecodeException {
        byte[] assocId = new byte[64];

        Cms03 req = new Cms03();
        req.setAssociationId(assocId);

        byte[] frame = req.encode();

        Cms03 decoded = new Cms03();
        decoded.decode(frame);
        assertArrayEquals(assocId, decoded.getAssociationId());
    }

    @Test
    @DisplayName("APCH header: PI=0x01, SC=0x03")
    void header() {
        Cms03 req = new Cms03();
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

        Cms03 req = new Cms03();
        req.setResponse(true);
        req.setAssociationId(assocId);

        byte[] frame = req.encode();
        assertTrue((frame[2] & 0x80) != 0);

        Cms03 decoded = new Cms03();
        decoded.decode(frame);
        assertTrue(decoded.isResponse());
        assertArrayEquals(assocId, decoded.getAssociationId());
    }

    @Test
    @DisplayName("frame length = APCH(5) + ASDU(64)")
    void frameLength() {
        Cms03 req = new Cms03();
        req.setAssociationId(new byte[64]);

        byte[] frame = req.encode();
        assertEquals(5 + 64, frame.length);

        int fl = ((frame[3] & 0xFF) << 8) | (frame[4] & 0xFF);
        assertEquals(64, fl);
    }

    @Test
    @DisplayName("decode service code mismatch throws")
    void serviceCodeMismatch() {
        byte[] frame = {0x01, 0x02, 0x00, 0x00, 0x00};  // SC=2 but Cms03 expects SC=3

        Cms03 decoded = new Cms03();
        assertThrows(PerDecodeException.class, () -> decoded.decode(frame));
    }

    @Test
    @DisplayName("toString contains hex")
    void toStringTest() {
        byte[] assocId = new byte[64];
        assocId[0] = (byte) 0xDE;
        assocId[1] = (byte) 0xAD;

        Cms03 req = new Cms03();
        req.setAssociationId(assocId);
        assertTrue(req.toString().contains("DEAD"));
    }
}
