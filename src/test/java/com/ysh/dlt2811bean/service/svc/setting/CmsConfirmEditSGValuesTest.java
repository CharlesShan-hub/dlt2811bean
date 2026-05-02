package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsConfirmEditSGValues")
class CmsConfirmEditSGValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsConfirmEditSGValues result = (CmsConfirmEditSGValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.SGCB1", result.sgcbReference().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsConfirmEditSGValues result = (CmsConfirmEditSGValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsConfirmEditSGValues result = (CmsConfirmEditSGValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsConfirmEditSGValues service = new CmsConfirmEditSGValues(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsConfirmEditSGValues.write(pos, service);

        CmsConfirmEditSGValues result = CmsConfirmEditSGValues.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.SGCB1", result.sgcbReference().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsConfirmEditSGValues original = new CmsConfirmEditSGValues(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .reqId(10);

        CmsConfirmEditSGValues copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.sgcbReference().get(), copy.sgcbReference().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns CONFIRM_EDIT_SG_VALUES")
    void serviceCode() {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(MessageType.REQUEST);
        assertEquals(ServiceName.CONFIRM_EDIT_SG_VALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsConfirmEditSGValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsConfirmEditSGValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
