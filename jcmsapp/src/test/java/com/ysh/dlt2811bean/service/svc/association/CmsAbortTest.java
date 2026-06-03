package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AbortReason;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAbort")
class CmsAbortTest implements ServiceNameTest<CmsAbort>, CopyTest<CmsAbort> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.ABORT;
    }

    @Override
    public CmsAbort createAsdu() {
        return new CmsAbort(MessageType.REQUEST);
    }

    @Override
    public CmsAbort createCopyableAsdu() {
        return new CmsAbort(MessageType.REQUEST)
                .reason(AbortReason.INVALID_ARGUMENT)
                .reqId(10);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsAbort result = AsduTestUtil.roundTripViaApdu(
                new CmsAbort(MessageType.REQUEST)
                        .reason(AbortReason.UNRECOGNIZED_SERVICE)
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals(AbortReason.UNRECOGNIZED_SERVICE, result.reason().get());
    }

    @Test
    @DisplayName("REQUEST with INVALID_ARGUMENT reason")
    void requestInvalidArgument() throws Exception {
        CmsAbort result = AsduTestUtil.roundTripViaApdu(
                new CmsAbort(MessageType.REQUEST)
                        .reason(AbortReason.INVALID_ARGUMENT)
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals(AbortReason.INVALID_ARGUMENT, result.reason().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsAbort result = AsduTestUtil.roundTripViaAsdu(
                new CmsAbort(MessageType.REQUEST)
                        .reason(AbortReason.INVALID_REQ_ID)
                        .reqId(5));

        assertEquals(5, result.reqId().get());
        assertEquals(AbortReason.INVALID_REQ_ID, result.reason().get());
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
