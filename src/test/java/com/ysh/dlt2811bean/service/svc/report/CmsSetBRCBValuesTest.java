package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetBRCBValuesEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSetBRCBValues")
class CmsSetBRCBValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.REQUEST)
            .reqId(1);

        CmsSetBRCBValuesEntry entry1 = new CmsSetBRCBValuesEntry();
        entry1.reference.set("IED1.AP1.LD1.LN1.BRCB1");
        entry1.rptID.set("RPT01");
        entry1.rptEna.set(true);
        asdu.addBrcb(entry1);

        CmsSetBRCBValuesEntry entry2 = new CmsSetBRCBValuesEntry();
        entry2.reference.set("IED1.AP1.LD1.LN1.BRCB2");
        entry2.rptEna.set(false);
        asdu.addBrcb(entry2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetBRCBValues result = (CmsSetBRCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.brcb().size());
        assertEquals("IED1.AP1.LD1.LN1.BRCB1", result.brcb().get(0).reference().get());
        assertTrue(result.brcb().get(0).rptEna().get());
        assertTrue(result.brcb().get(0).isFieldPresent("rptID"));
        assertEquals("IED1.AP1.LD1.LN1.BRCB2", result.brcb().get(1).reference().get());
        assertFalse(result.brcb().get(1).rptEna().get());
        assertFalse(result.brcb().get(1).isFieldPresent("rptID"));
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetBRCBValues result = (CmsSetBRCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(3);
        asdu.addResult(CmsServiceError.ACCESS_VIOLATION);
        asdu.addResult(CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetBRCBValues result = (CmsSetBRCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(2, result.result().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.result().get(0).get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.result().get(1).get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsSetBRCBValues original = new CmsSetBRCBValues(MessageType.REQUEST)
            .reqId(10);

        CmsSetBRCBValuesEntry entry = new CmsSetBRCBValuesEntry();
        entry.reference.set("IED1.AP1.LD1.LN1.BRCB1");
        original.addBrcb(entry);

        CmsSetBRCBValues copy = original.copy();
        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.brcb().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns SET_BRCBVALUES")
    void serviceCode() {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.REQUEST);
        assertEquals(ServiceName.SET_BRCBVALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.REQUEST)
            .reqId(1);

        CmsSetBRCBValuesEntry entry = new CmsSetBRCBValuesEntry();
        entry.reference.set("IED1.AP1.LD1.LN1.BRCB1");
        asdu.addBrcb(entry);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSetBRCBValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }
}
