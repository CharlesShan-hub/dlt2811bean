package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorSgcbChoice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetSGCBValues")
class CmsGetSGCBValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addSgcbReference("IED1.AP1.LD1.LN1.SGCB1");
        asdu.addSgcbReference("IED1.AP1.LD1.LN1.SGCB2");

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetSGCBValues result = (CmsGetSGCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.sgcbReference().size());
        assertEquals("IED1.AP1.LD1.LN1.SGCB1", result.sgcbReference().get(0).get());
        assertEquals("IED1.AP1.LD1.LN1.SGCB2", result.sgcbReference().get(1).get());
    }

    @Test
    @DisplayName("REQUEST: empty reference list")
    void requestEmptyReferences() throws Exception {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.REQUEST)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetSGCBValues result = (CmsGetSGCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertTrue(result.sgcbReference().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(3);

        CmsErrorSgcbChoice errorChoice = new CmsErrorSgcbChoice()
            .selectError();
        errorChoice.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addErrorChoice(errorChoice);

        CmsErrorSgcbChoice sgcbChoice = new CmsErrorSgcbChoice()
            .selectSgcb();
        sgcbChoice.sgcb.sgcbName.set("SGCB1");
        sgcbChoice.sgcb.sgcbRef.set("IED1.AP1.LD1.LN1.SGCB1");
        asdu.addErrorChoice(sgcbChoice);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetSGCBValues result = (CmsGetSGCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(2, result.errorSgcb().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.errorSgcb().get(0).error.get());
        assertEquals("SGCB1", result.errorSgcb().get(1).sgcb.sgcbName.get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty list")
    void positiveResponseEmpty() throws Exception {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetSGCBValues result = (CmsGetSGCBValues) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertTrue(result.errorSgcb().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(5);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetSGCBValues result = (CmsGetSGCBValues) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetSGCBValues service = new CmsGetSGCBValues(MessageType.REQUEST)
            .reqId(10);

        service.addSgcbReference("IED1.AP1.LD1.LN1.SGCB1");

        PerOutputStream pos = new PerOutputStream();
        CmsGetSGCBValues.write(pos, service);

        CmsGetSGCBValues result = CmsGetSGCBValues.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals(1, result.sgcbReference().size());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetSGCBValues original = new CmsGetSGCBValues(MessageType.REQUEST)
            .reqId(10);

        original.addSgcbReference("IED1.AP1.LD1.LN1.SGCB1");

        CmsGetSGCBValues copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.sgcbReference().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_SGCBVALUES")
    void serviceCode() {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.REQUEST);
        assertEquals(ServiceName.GET_SGCBVALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addSgcbReference("IED1.AP1.LD1.LN1.SGCB1");

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetSGCBValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetSGCBValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
