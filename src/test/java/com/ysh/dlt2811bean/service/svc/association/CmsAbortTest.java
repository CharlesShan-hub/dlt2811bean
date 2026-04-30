package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AbortReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAbort")
class CmsAbortTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsAbort asdu = new CmsAbort(MessageType.REQUEST)
            .reason(AbortReason.UNRECOGNIZED_SERVICE)
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsAbort result = (CmsAbort) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(AbortReason.UNRECOGNIZED_SERVICE, result.reason().get());

        System.out.println(result);
    }

    @Test
    @DisplayName("REQUEST with INVALID_ARGUMENT reason")
    void requestInvalidArgument() throws Exception {
        CmsAbort asdu = new CmsAbort(MessageType.REQUEST)
            .reason(AbortReason.INVALID_ARGUMENT)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsAbort result = (CmsAbort) decoded.getAsdu();
        System.out.println( result);
        assertEquals(2, result.reqId().get());
        assertEquals(AbortReason.INVALID_ARGUMENT, result.reason().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsAbort service = new CmsAbort(MessageType.REQUEST)
            .reason(AbortReason.INVALID_REQ_ID)
            .reqId(5);

        PerOutputStream pos = new PerOutputStream();
        CmsAbort.write(pos, service);

        CmsAbort result = CmsAbort.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(5, result.reqId().get());
        assertEquals(AbortReason.INVALID_REQ_ID, result.reason().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsAbort original = new CmsAbort(MessageType.REQUEST)
            .reason(AbortReason.INVALID_ARGUMENT)
            .reqId(10);

        CmsAbort copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.reason().get(), copy.reason().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsAbort asdu = new CmsAbort(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns ABORT")
    void serviceCode() {
        CmsAbort asdu = new CmsAbort(MessageType.REQUEST);
        assertEquals(ServiceName.ABORT, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsAbort asdu = new CmsAbort(MessageType.REQUEST)
            .reason(AbortReason.UNRECOGNIZED_SERVICE)
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsAbort) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
        assertTrue(str.contains("reason: (AbortReason) 1"));
    }

    @Test
    @DisplayName("default reason is OTHER")
    void defaultReason() {
        CmsAbort asdu = new CmsAbort(MessageType.REQUEST);
        assertEquals(AbortReason.OTHER, asdu.reason().get());
    }
}
