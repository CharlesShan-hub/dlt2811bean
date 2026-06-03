package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsSetLCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsSetLCBValuesResultEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSetLCBValues")
class CmsSetLCBValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSetLCBValues asdu = new CmsSetLCBValues(MessageType.REQUEST)
            .reqId(1);

        CmsSetLCBValuesEntry entry1 = new CmsSetLCBValuesEntry();
        entry1.reference.set("IED1.AP1.LD1.LN1.LCB1");
        entry1.logEna.set(true);
        entry1.logRef.set("IED1.AP1.LD1.LN1.LOG1");
        asdu.addLcb(entry1);

        CmsSetLCBValuesEntry entry2 = new CmsSetLCBValuesEntry();
        entry2.reference.set("IED1.AP1.LD1.LN1.LCB2");
        entry2.intgPd.set(1000L);
        asdu.addLcb(entry2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetLCBValues result = (CmsSetLCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.lcb().size());
        assertEquals("IED1.AP1.LD1.LN1.LCB1", result.lcb().get(0).reference().get());
        assertTrue(result.lcb().get(0).logEna().get());
        assertTrue(result.lcb().get(0).isFieldPresent("logEna"));
        assertTrue(result.lcb().get(0).isFieldPresent("logRef"));
        assertEquals("IED1.AP1.LD1.LN1.LCB2", result.lcb().get(1).reference().get());
        assertEquals(1000L, result.lcb().get(1).intgPd().get());
        assertTrue(result.lcb().get(1).isFieldPresent("intgPd"));
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetLCBValues asdu = new CmsSetLCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetLCBValues result = (CmsSetLCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetLCBValues asdu = new CmsSetLCBValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(3);

        CmsSetLCBValuesResultEntry resultEntry = new CmsSetLCBValuesResultEntry();
        resultEntry.error.set(CmsServiceError.ACCESS_VIOLATION);
        resultEntry.logEna.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        asdu.addResult(resultEntry);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetLCBValues result = (CmsSetLCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(1, result.result().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.result().get(0).error.get());
        assertTrue(result.result().get(0).isFieldPresent("logEna"));
        assertFalse(result.result().get(0).isFieldPresent("datSet"));
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsSetLCBValues original = new CmsSetLCBValues(MessageType.REQUEST)
            .reqId(10);

        CmsSetLCBValuesEntry entry = new CmsSetLCBValuesEntry();
        entry.reference.set("IED1.AP1.LD1.LN1.LCB1");
        original.addLcb(entry);

        CmsSetLCBValues copy = original.copy();
        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.lcb().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsSetLCBValues asdu = new CmsSetLCBValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsSetLCBValues asdu = new CmsSetLCBValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsSetLCBValues asdu = new CmsSetLCBValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns SET_LCBVALUES")
    void serviceCode() {
        CmsSetLCBValues asdu = new CmsSetLCBValues(MessageType.REQUEST);
        assertEquals(ServiceName.SET_LCB_VALUES, asdu.getServiceName());
    }
}
