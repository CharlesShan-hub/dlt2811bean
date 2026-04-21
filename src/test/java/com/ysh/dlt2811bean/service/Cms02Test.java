package com.ysh.dlt2811bean.service;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cms02 Abort (SC=2)")
class Cms02Test {

    @Test
    @DisplayName("encode+decode round-trip — normal")
    void roundTrip_normal() throws PerDecodeException {
        Cms02 req = new Cms02();
        req.setReason(Cms02.REASON_NORMAL);

        byte[] frame = req.encode();

        Cms02 decoded = new Cms02();
        decoded.decode(frame);
        assertEquals(Cms02.REASON_NORMAL, decoded.getReason());
    }

    @Test
    @DisplayName("all reason values round-trip")
    void allReasonValues() throws PerDecodeException {
        for (int reason = 0; reason <= Cms02.REASON_MAX; reason++) {
            Cms02 req = new Cms02();
            req.setReason(reason);

            byte[] frame = req.encode();

            Cms02 decoded = new Cms02();
            decoded.decode(frame);
            assertEquals(reason, decoded.getReason(), "reason=" + reason);
        }
    }

    @Test
    @DisplayName("APCH header: PI=0x01, SC=0x02")
    void header() {
        Cms02 req = new Cms02();
        req.setReason(Cms02.REASON_NORMAL);

        byte[] frame = req.encode();
        assertEquals(0x01, frame[0] & 0xFF);  // PI
        assertEquals(0x02, frame[1] & 0xFF);  // SC
    }

    @Test
    @DisplayName("response flag")
    void responseFlag() throws PerDecodeException {
        Cms02 req = new Cms02();
        req.setResponse(true);
        req.setReason(Cms02.REASON_URGENT);

        byte[] frame = req.encode();
        assertTrue((frame[2] & 0x80) != 0);

        Cms02 decoded = new Cms02();
        decoded.decode(frame);
        assertTrue(decoded.isResponse());
        assertEquals(Cms02.REASON_URGENT, decoded.getReason());
    }

    @Test
    @DisplayName("ASDU is compact — reason fits in 3 bits (0..4)")
    void asduCompact() {
        Cms02 req = new Cms02();
        req.setReason(Cms02.REASON_OTHER);

        byte[] frame = req.encode();
        // APCH(5) + ASDU(1 byte, 3 bits used, padded to 1 byte)
        assertEquals(6, frame.length);
    }

    @Test
    @DisplayName("decode service code mismatch throws")
    void serviceCodeMismatch() {
        byte[] frame = {0x01, 0x01, 0x00, 0x00, 0x00};  // SC=1 but Cms02 expects SC=2

        Cms02 decoded = new Cms02();
        assertThrows(PerDecodeException.class, () -> decoded.decode(frame));
    }

    @Test
    @DisplayName("toString contains reason")
    void toStringTest() {
        Cms02 req = new Cms02();
        req.setReason(Cms02.REASON_AUTH_FAILURE);
        assertTrue(req.toString().contains("3"));
    }
}
