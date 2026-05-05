package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetBRCBValues")
class CmsGetBRCBValuesTest implements ServiceNameTest<CmsGetBRCBValues> {
    @Override public ServiceName expectedServiceName() { return ServiceName.GET_BRCBVALUES; }
    @Override public CmsGetBRCBValues createAsdu() { return new CmsGetBRCBValues(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.REQUEST).reqId(1);
        asdu.addBrcbReference("IED1.AP1.LD1.LN1.RPT1");
        CmsGetBRCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(1, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetBRCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetBRCBValues(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetBRCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetBRCBValues(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
    }
}
