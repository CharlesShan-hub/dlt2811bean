package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetURCBValues")
class CmsGetURCBValuesTest implements ServiceNameTest<CmsGetURCBValues> {
    @Override public ServiceName expectedServiceName() { return ServiceName.GET_URCBVALUES; }
    @Override public CmsGetURCBValues createAsdu() { return new CmsGetURCBValues(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsGetURCBValues asdu = new CmsGetURCBValues(MessageType.REQUEST).reqId(1);
        asdu.addReference("IED1.AP1.LD1.LN1.URPT1");
        CmsGetURCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(1, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetURCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetURCBValues(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetURCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetURCBValues(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
    }
}
