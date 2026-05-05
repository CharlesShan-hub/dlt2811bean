package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetLogStatusValues")
class CmsGetLogStatusValuesTest implements ServiceNameTest<CmsGetLogStatusValues> {
    @Override public ServiceName expectedServiceName() { return ServiceName.GET_LOG_STATUS_VALUES; }
    @Override public CmsGetLogStatusValues createAsdu() { return new CmsGetLogStatusValues(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsGetLogStatusValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogStatusValues(MessageType.REQUEST).reqId(1));
        assertEquals(1, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetLogStatusValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogStatusValues(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetLogStatusValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogStatusValues(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
    }
}
