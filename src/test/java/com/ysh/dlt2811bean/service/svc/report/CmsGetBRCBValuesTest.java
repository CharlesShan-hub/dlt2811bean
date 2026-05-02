package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorBrcbChoice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetBRCBValues")
class CmsGetBRCBValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addBrcbReference("IED1.AP1.LD1.LN1.BRCB1");
        asdu.addBrcbReference("IED1.AP1.LD1.LN1.BRCB2");

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetBRCBValues result = (CmsGetBRCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.brcbReference().size());
        assertEquals("IED1.AP1.LD1.LN1.BRCB1", result.brcbReference().get(0).get());
        assertEquals("IED1.AP1.LD1.LN1.BRCB2", result.brcbReference().get(1).get());
    }

    @Test
    @DisplayName("REQUEST: empty reference list")
    void requestEmptyReferences() throws Exception {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.REQUEST)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetBRCBValues result = (CmsGetBRCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertTrue(result.brcbReference().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(3);

        CmsErrorBrcbChoice errorChoice = new CmsErrorBrcbChoice()
            .selectError();
        errorChoice.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addErrorChoice(errorChoice);

        CmsErrorBrcbChoice brcbChoice = new CmsErrorBrcbChoice()
            .selectBrcb();
        brcbChoice.brcb.brcbName.set("BRCB1");
        brcbChoice.brcb.brcbRef.set("IED1.AP1.LD1.LN1.BRCB1");
        asdu.addErrorChoice(brcbChoice);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetBRCBValues result = (CmsGetBRCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(2, result.errorBrcb().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.errorBrcb().get(0).error.get());
        assertEquals("BRCB1", result.errorBrcb().get(1).brcb.brcbName.get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty list")
    void positiveResponseEmpty() throws Exception {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetBRCBValues result = (CmsGetBRCBValues) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertTrue(result.errorBrcb().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(5);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetBRCBValues result = (CmsGetBRCBValues) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetBRCBValues service = new CmsGetBRCBValues(MessageType.REQUEST)
            .reqId(10);

        service.addBrcbReference("IED1.AP1.LD1.LN1.BRCB1");

        PerOutputStream pos = new PerOutputStream();
        CmsGetBRCBValues.write(pos, service);

        CmsGetBRCBValues result = CmsGetBRCBValues.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals(1, result.brcbReference().size());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetBRCBValues original = new CmsGetBRCBValues(MessageType.REQUEST)
            .reqId(10);

        original.addBrcbReference("IED1.AP1.LD1.LN1.BRCB1");

        CmsGetBRCBValues copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.brcbReference().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_BRCBVALUES")
    void serviceCode() {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.REQUEST);
        assertEquals(ServiceName.GET_BRCBVALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addBrcbReference("IED1.AP1.LD1.LN1.BRCB1");

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetBRCBValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetBRCBValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
