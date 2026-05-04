package com.ysh.dlt2811bean.service.svc.sv;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesResultEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSetMSVCBValues")
class CmsSetMSVCBValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSetMSVCBValues asdu = new CmsSetMSVCBValues(MessageType.REQUEST)
            .reqId(1);

        CmsSetMSVCBValuesEntry entry = new CmsSetMSVCBValuesEntry();
        entry.reference.set("IED1.AP1.LD1.LN1.MSVCB1");
        entry.svEna.set(true);
        asdu.addMsvcb(entry);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetMSVCBValues result = (CmsSetMSVCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(1, result.msvcb().size());
        assertEquals("IED1.AP1.LD1.LN1.MSVCB1", result.msvcb().get(0).reference().get());
        assertTrue(result.msvcb().get(0).isFieldPresent("svEna"));
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetMSVCBValues asdu = new CmsSetMSVCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetMSVCBValues result = (CmsSetMSVCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetMSVCBValues asdu = new CmsSetMSVCBValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(3);

        CmsSetMSVCBValuesResultEntry entry = new CmsSetMSVCBValuesResultEntry();
        entry.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addResult(entry);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetMSVCBValues result = (CmsSetMSVCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(1, result.result().size());
    }

    @Test
    @DisplayName("getServiceCode returns SET_MSVCBVALUES")
    void serviceCode() {
        assertEquals(ServiceName.SET_MSVCBVALUES, new CmsSetMSVCBValues(MessageType.REQUEST).getServiceName());
    }
}
