package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesEntry;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSetURCBValues")
class CmsSetURCBValuesTest implements ServiceNameTest<CmsSetURCBValues> {
    @Override public ServiceName expectedServiceName() { return ServiceName.SET_URCBVALUES; }
    @Override public CmsSetURCBValues createAsdu() { return new CmsSetURCBValues(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.REQUEST).reqId(1);
        asdu.addUrcb(new CmsSetURCBValuesEntry());
        CmsSetURCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(1, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetURCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetURCBValues(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.RESPONSE_NEGATIVE).reqId(3);
        asdu.addResult(new com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesResultEntry());
        CmsSetURCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(3, result.reqId().get());
    }
}
