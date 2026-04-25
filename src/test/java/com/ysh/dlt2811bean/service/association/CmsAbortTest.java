package com.ysh.dlt2811bean.service.association;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.service.enums.MessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAbort (SC=2)")
class CmsAbortTest {

    @Test
    @DisplayName("encode+decode round-trip — normal")
    void roundTrip_normal() throws PerDecodeException {
        CmsAbort req = new CmsAbort();
        req.setReason(CmsAbort.REASON_NORMAL);

        byte[] frame = req.encode();

        CmsAbort decoded = new CmsAbort();
        decoded.decode(frame);
        assertEquals(CmsAbort.REASON_NORMAL, decoded.getReason());
    }

    @Test
    @DisplayName("all reason values round-trip")
    void allReasonValues() throws PerDecodeException {
        for (int reason = 0; reason <= CmsAbort.REASON_MAX; reason++) {
            CmsAbort req = new CmsAbort();
            req.setReason(reason);

            byte[] frame = req.encode();

            CmsAbort decoded = new CmsAbort();
            decoded.decode(frame);
            assertEquals(reason, decoded.getReason(), "reason=" + reason);
        }
    }

    @Test
    @DisplayName("APCH header: PI=0x01, SC=0x02")
    void header() {
        CmsAbort req = new CmsAbort();
        req.setReason(CmsAbort.REASON_NORMAL);

        byte[] frame = req.encode();
        assertEquals(0x01, frame[0] & 0xFF);  // PI
        assertEquals(0x02, frame[1] & 0xFF);  // SC
    }

    @Test
    @DisplayName("response flag")
    void responseFlag() throws PerDecodeException {
        CmsAbort req = new CmsAbort();
        req.setMessageType(MessageType.RESPONSE_POSITIVE);
        req.setReason(CmsAbort.REASON_URGENT);

        byte[] frame = req.encode();
        assertTrue((frame[2] & 0x80) != 0);

        CmsAbort decoded = new CmsAbort();
        decoded.decode(frame);
        assertEquals(MessageType.RESPONSE_POSITIVE, decoded.getMessageType());
        assertEquals(CmsAbort.REASON_URGENT, decoded.getReason());
    }

    @Test
    @DisplayName("ASDU is compact — reason fits in 3 bits (0..4)")
    void asduCompact() {
        CmsAbort req = new CmsAbort();
        req.setReason(CmsAbort.REASON_OTHER);

        byte[] frame = req.encode();
        // APCH(5) + ASDU(1 byte, 3 bits used, padded to 1 byte)
        assertEquals(6, frame.length);
    }

    @Test
    @DisplayName("decode service code mismatch throws")
    void serviceCodeMismatch() {
        byte[] frame = {0x01, 0x01, 0x00, 0x00, 0x00};  // SC=1 but CmsAbort expects SC=2

        CmsAbort decoded = new CmsAbort();
        assertThrows(PerDecodeException.class, () -> decoded.decode(frame));
    }

    @Test
    @DisplayName("toString contains reason")
    void toStringTest() {
        CmsAbort req = new CmsAbort();
        req.setReason(CmsAbort.REASON_AUTH_FAILURE);
        assertTrue(req.toString().contains("3"));
    }
}
