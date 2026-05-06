package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetBRCBValuesEntry;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSetBRCBValues")
class CmsSetBRCBValuesTest implements ServiceNameTest<CmsSetBRCBValues> {
    @Override public ServiceName expectedServiceName() { return ServiceName.SET_BRCB_VALUES; }
    @Override public CmsSetBRCBValues createAsdu() { return new CmsSetBRCBValues(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.REQUEST).reqId(1);
        asdu.addBrcb(new CmsSetBRCBValuesEntry());
        CmsSetBRCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(1, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetBRCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetBRCBValues(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.RESPONSE_NEGATIVE).reqId(3);
        asdu.addResult(CmsServiceError.ACCESS_VIOLATION);
        CmsSetBRCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(3, result.reqId().get());
    }
}
