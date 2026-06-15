package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsQueryLogByTime")
class CmsQueryLogByTimeTest implements ServiceNameTest<CmsQueryLogByTime> {

    @Override public ServiceName expectedServiceName() { return ServiceName.QUERY_LOG_BY_TIME; }
    @Override public CmsQueryLogByTime createAsdu() { return new CmsQueryLogByTime(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip with all optionals")
    void requestRoundTrip() throws Exception {
        CmsQueryLogByTime result = AsduTestUtil.roundTripViaApdu(
                new CmsQueryLogByTime(MessageType.REQUEST)
                        .logReference("IED1.AP1.LD1.LN1.LOG1")
                        .startTime(10000L, 15000).stopTime(50000L, 15000)
                        .entryAfter(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07})
                        .reqId(1));
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.LOG1", result.logReference().get());
        assertTrue(result.isFieldPresent("startTime"));
        assertTrue(result.isFieldPresent("stopTime"));
        assertTrue(result.isFieldPresent("entryAfter"));
    }

    @Test @DisplayName("REQUEST without optional parameters")
    void requestWithoutOptionals() throws Exception {
        CmsQueryLogByTime result = AsduTestUtil.roundTripViaApdu(
                new CmsQueryLogByTime(MessageType.REQUEST)
                        .logReference("IED1.AP1.LD1.LN1.LOG1").reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsQueryLogByTime asdu = new CmsQueryLogByTime(MessageType.RESPONSE_POSITIVE).reqId(3);
        asdu.moreFollows().set(false);
        CmsQueryLogByTime result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(3, result.reqId().get());
        assertTrue(result.logEntry().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsQueryLogByTime result = AsduTestUtil.roundTripViaApdu(
                new CmsQueryLogByTime(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(4));
        assertEquals(4, result.reqId().get());
    }
}
