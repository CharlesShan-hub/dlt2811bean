package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorUrcbChoice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetURCBValues")
class CmsGetURCBValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetURCBValues asdu = new CmsGetURCBValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addReference("IED1.AP1.LD1.LN1.URCB1");
        asdu.addReference("IED1.AP1.LD1.LN1.URCB2");

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetURCBValues result = (CmsGetURCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertEquals("IED1.AP1.LD1.LN1.URCB1", result.reference().get(0).get());
        assertEquals("IED1.AP1.LD1.LN1.URCB2", result.reference().get(1).get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetURCBValues asdu = new CmsGetURCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsErrorUrcbChoice errorChoice = new CmsErrorUrcbChoice()
            .selectError();
        errorChoice.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addUrcbChoice(errorChoice);

        CmsErrorUrcbChoice urcbChoice = new CmsErrorUrcbChoice()
            .selectValue();
        urcbChoice.value.rptID.set("RPT01");
        urcbChoice.value.urcbRef.set("IED1.AP1.LD1.LN1.URCB1");
        asdu.addUrcbChoice(urcbChoice);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetURCBValues result = (CmsGetURCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(2, result.urcb().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.urcb().get(0).error.get());
        assertEquals("RPT01", result.urcb().get(1).value.rptID.get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetURCBValues asdu = new CmsGetURCBValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetURCBValues result = (CmsGetURCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetURCBValues original = new CmsGetURCBValues(MessageType.REQUEST)
            .reqId(10);
        original.addReference("IED1.AP1.LD1.LN1.URCB1");

        CmsGetURCBValues copy = original.copy();
        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.reference().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("getServiceCode returns GET_URCBVALUES")
    void serviceCode() {
        CmsGetURCBValues asdu = new CmsGetURCBValues(MessageType.REQUEST);
        assertEquals(ServiceName.GET_URCBVALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetURCBValues asdu = new CmsGetURCBValues(MessageType.REQUEST)
            .reqId(1);
        asdu.addReference("IED1.AP1.LD1.LN1.URCB1");

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetURCBValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }
}
