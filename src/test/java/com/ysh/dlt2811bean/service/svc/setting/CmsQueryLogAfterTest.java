package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsQueryLogAfter")
class CmsQueryLogAfterTest implements ServiceNameTest<CmsQueryLogAfter> {
    @Override public ServiceName expectedServiceName() { return ServiceName.QUERY_LOG_AFTER; }
    @Override public CmsQueryLogAfter createAsdu() { return new CmsQueryLogAfter(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsQueryLogAfter result = AsduTestUtil.roundTripViaApdu(
                new CmsQueryLogAfter(MessageType.REQUEST)
                        .logReference("IED1.AP1.LD1.LN1.LOG1")
                        .entry(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07})
                        .reqId(1));
        assertEquals(1, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsQueryLogAfter result = AsduTestUtil.roundTripViaApdu(
                new CmsQueryLogAfter(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsQueryLogAfter result = AsduTestUtil.roundTripViaApdu(
                new CmsQueryLogAfter(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
    }
}
