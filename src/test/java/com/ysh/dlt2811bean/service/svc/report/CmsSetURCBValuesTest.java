package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesResultEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSetURCBValues")
class CmsSetURCBValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.REQUEST)
            .reqId(1);

        CmsSetURCBValuesEntry entry1 = new CmsSetURCBValuesEntry();
        entry1.reference.set("IED1.AP1.LD1.LN1.URCB1");
        entry1.rptEna.set(true);
        entry1.gi.set(true);
        asdu.addUrcb(entry1);

        CmsSetURCBValuesEntry entry2 = new CmsSetURCBValuesEntry();
        entry2.reference.set("IED1.AP1.LD1.LN1.URCB2");
        entry2.rptEna.set(false);
        asdu.addUrcb(entry2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetURCBValues result = (CmsSetURCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.urcb().size());
        assertEquals("IED1.AP1.LD1.LN1.URCB1", result.urcb().get(0).reference().get());
        assertTrue(result.urcb().get(0).isFieldPresent("rptEna"));
        assertTrue(result.urcb().get(0).rptEna().get());
        assertTrue(result.urcb().get(0).isFieldPresent("gi"));
        assertEquals("IED1.AP1.LD1.LN1.URCB2", result.urcb().get(1).reference().get());
        assertFalse(result.urcb().get(1).isFieldPresent("rptEna"));
        assertFalse(result.urcb().get(1).isFieldPresent("gi"));
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetURCBValues result = (CmsSetURCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(3);

        CmsSetURCBValuesResultEntry resultEntry = new CmsSetURCBValuesResultEntry();
        resultEntry.error.set(CmsServiceError.ACCESS_VIOLATION);
        resultEntry.rptEna.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        asdu.addResult(resultEntry);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetURCBValues result = (CmsSetURCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(1, result.result().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.result().get(0).error.get());
        assertEquals(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE, result.result().get(0).rptEna().get());
        assertTrue(result.result().get(0).isFieldPresent("rptEna"));
        assertFalse(result.result().get(0).isFieldPresent("rptID"));
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsSetURCBValues original = new CmsSetURCBValues(MessageType.REQUEST)
            .reqId(10);

        CmsSetURCBValuesEntry entry = new CmsSetURCBValuesEntry();
        entry.reference.set("IED1.AP1.LD1.LN1.URCB1");
        original.addUrcb(entry);

        CmsSetURCBValues copy = original.copy();
        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.urcb().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsSetURCBValues asdu = new CmsSetURCBValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsSetURCBValues asdu = new CmsSetURCBValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsSetURCBValues asdu = new CmsSetURCBValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns SET_URCBVALUES")
    void serviceCode() {
        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.REQUEST);
        assertEquals(ServiceName.SET_URCBVALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.REQUEST)
            .reqId(1);

        CmsSetURCBValuesEntry entry = new CmsSetURCBValuesEntry();
        entry.reference.set("IED1.AP1.LD1.LN1.URCB1");
        asdu.addUrcb(entry);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSetURCBValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }
}
