package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorLcbChoice;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetLCBValues")
class CmsGetLCBValuesTest implements ServiceNameTest<CmsGetLCBValues> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_LCBVALUES;
    }

    @Override
    public CmsGetLCBValues createAsdu() {
        return new CmsGetLCBValues(MessageType.REQUEST);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetLCBValues asdu = new CmsGetLCBValues(MessageType.REQUEST).reqId(1);
        asdu.addReference("IED1.AP1.LD1.LN1.LCB1");
        asdu.addReference("IED1.AP1.LD1.LN1.LCB2");

        CmsGetLCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertEquals("IED1.AP1.LD1.LN1.LCB1", result.reference().get(0).get());
        assertEquals("IED1.AP1.LD1.LN1.LCB2", result.reference().get(1).get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetLCBValues asdu = new CmsGetLCBValues(MessageType.RESPONSE_POSITIVE).reqId(2);
        CmsErrorLcbChoice errorChoice = new CmsErrorLcbChoice().selectError();
        errorChoice.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addLcbChoice(errorChoice);
        CmsErrorLcbChoice lcbChoice = new CmsErrorLcbChoice().selectValue();
        lcbChoice.value.logEna.set(true);
        lcbChoice.value.logRef.set("IED1.AP1.LD1.LN1.LOG1");
        asdu.addLcbChoice(lcbChoice);
        asdu.moreFollows().set(true);

        CmsGetLCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
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
        CmsGetLCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLCBValues(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }
}
