package com.ysh.dlt2811bean.service.svc.sv;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsErrorMsvcbChoice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetMSVCBValues")
class CmsGetMSVCBValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetMSVCBValues asdu = new CmsGetMSVCBValues(MessageType.REQUEST)
            .reqId(1);
        asdu.addReference("IED1.AP1.LD1.LN1.MSVCB1");

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetMSVCBValues result = (CmsGetMSVCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(1, result.reference().size());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetMSVCBValues asdu = new CmsGetMSVCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsErrorMsvcbChoice errorChoice = new CmsErrorMsvcbChoice().selectError();
        errorChoice.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addErrorMsvcbChoice(errorChoice);

        CmsErrorMsvcbChoice msvcbChoice = new CmsErrorMsvcbChoice().selectMsvcb();
        msvcbChoice.msvcb.msvCBName.set("MSVCB1");
        asdu.addErrorMsvcbChoice(msvcbChoice);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetMSVCBValues result = (CmsGetMSVCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(2, result.errorMsvcb().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.errorMsvcb().get(0).error.get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetMSVCBValues asdu = new CmsGetMSVCBValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetMSVCBValues result = (CmsGetMSVCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("getServiceCode returns GET_MSVCBVALUES")
    void serviceCode() {
        assertEquals(ServiceName.GET_MSVCBVALUES, new CmsGetMSVCBValues(MessageType.REQUEST).getServiceName());
    }
}
