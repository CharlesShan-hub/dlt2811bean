package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorLcbChoice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetLCBValues")
class CmsGetLCBValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetLCBValues asdu = new CmsGetLCBValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addReference("IED1.AP1.LD1.LN1.LCB1");
        asdu.addReference("IED1.AP1.LD1.LN1.LCB2");

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLCBValues result = (CmsGetLCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertEquals("IED1.AP1.LD1.LN1.LCB1", result.reference().get(0).get());
        assertEquals("IED1.AP1.LD1.LN1.LCB2", result.reference().get(1).get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetLCBValues asdu = new CmsGetLCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsErrorLcbChoice errorChoice = new CmsErrorLcbChoice()
            .selectError();
        errorChoice.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addLcbChoice(errorChoice);

        CmsErrorLcbChoice lcbChoice = new CmsErrorLcbChoice()
            .selectValue();
        lcbChoice.value.logEna.set(true);
        lcbChoice.value.logRef.set("IED1.AP1.LD1.LN1.LOG1");
        asdu.addLcbChoice(lcbChoice);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLCBValues result = (CmsGetLCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(2, result.lcb().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.lcb().get(0).error.get());
        assertTrue(result.lcb().get(1).value.logEna().get());
        assertEquals("IED1.AP1.LD1.LN1.LOG1", result.lcb().get(1).value.logRef().get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetLCBValues asdu = new CmsGetLCBValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLCBValues result = (CmsGetLCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetLCBValues original = new CmsGetLCBValues(MessageType.REQUEST)
            .reqId(10);
        original.addReference("IED1.AP1.LD1.LN1.LCB1");

        CmsGetLCBValues copy = original.copy();
        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.reference().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("getServiceCode returns GET_LCBVALUES")
    void serviceCode() {
        CmsGetLCBValues asdu = new CmsGetLCBValues(MessageType.REQUEST);
        assertEquals(ServiceName.GET_LCBVALUES, asdu.getServiceName());
    }
}
