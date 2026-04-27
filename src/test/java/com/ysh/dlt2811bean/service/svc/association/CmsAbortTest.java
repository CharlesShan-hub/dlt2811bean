package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AbortReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAbort")
class CmsAbortTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip")
    void requestRoundTrip() throws Exception {
        CmsAbort service = new CmsAbort(MessageType.REQUEST)
            .reqId(1)
            .reason(AbortReason.INVALID_ARGUMENT);

        PerOutputStream pos = new PerOutputStream();
        CmsAbort.write(pos, service);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        CmsAbort result = CmsAbort.read(pis, MessageType.REQUEST);

        assertEquals(1, result.reqId());
        assertEquals(AbortReason.INVALID_ARGUMENT, result.reason().get());
        assertEquals(MessageType.REQUEST, result.messageType());
    }

    @Test
    @DisplayName("INDICATION: encode and decode round-trip")
    void indicationRoundTrip() throws Exception {
        CmsAbort service = new CmsAbort(MessageType.INDICATION)
            .reason(AbortReason.OTHER);

        PerOutputStream pos = new PerOutputStream();
        CmsAbort.write(pos, service);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        CmsAbort result = CmsAbort.read(pis, MessageType.INDICATION);

        assertEquals(AbortReason.OTHER, result.reason().get());
        assertEquals(MessageType.INDICATION, result.messageType());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsAbort original = new CmsAbort(MessageType.REQUEST)
            .reqId(5)
            .reason(AbortReason.MAX_SERV_OUTSTANDING_EXCEEDED);

        CmsAbort cloned = (CmsAbort) original.copy();

        assertEquals(original.reqId(), cloned.reqId());
        assertEquals(original.reason().get(), cloned.reason().get());
        assertNotSame(original, cloned);
        assertNotSame(original.reason(), cloned.reason());
    }

    @Test
    @DisplayName("copy is independent")
    void copyIsIndependent() {
        CmsAbort original = new CmsAbort(MessageType.REQUEST)
            .reqId(5)
            .reason(AbortReason.INVALID_REQ_ID);

        CmsAbort cloned = (CmsAbort) original.copy();
        cloned.reason(AbortReason.OTHER);

        assertEquals(AbortReason.INVALID_REQ_ID, original.reason().get());
        assertEquals(AbortReason.OTHER, cloned.reason().get());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsAbort service = new CmsAbort(MessageType.REQUEST)
            .reqId(3)
            .reason(AbortReason.UNRECOGNIZED_SERVICE);

        String s = service.toString();
        assertTrue(s.contains("reqId=3"));
        assertTrue(s.contains("AbortReason"));
    }

    @Test
    @DisplayName("toString for INDICATION")
    void toStringIndication() {
        CmsAbort service = new CmsAbort(MessageType.INDICATION)
            .reason(AbortReason.OTHER);

        String s = service.toString();
        assertFalse(s.contains("reqId"));
        assertTrue(s.contains("AbortReason"));
    }
}
