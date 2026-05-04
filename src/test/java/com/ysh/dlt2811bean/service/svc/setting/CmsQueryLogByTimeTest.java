package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsQueryLogByTime")
class CmsQueryLogByTimeTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsQueryLogByTime asdu = new CmsQueryLogByTime(MessageType.REQUEST)
            .logReference("IED1.AP1.LD1.LN1.LOG1")
            .startTime(10000L, 15000)
            .stopTime(50000L, 15000)
            .entryAfter(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07})
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsQueryLogByTime result = (CmsQueryLogByTime) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.LOG1", result.logReference().get());
        assertTrue(result.isFieldPresent("startTime"));
        assertTrue(result.isFieldPresent("stopTime"));
        assertTrue(result.isFieldPresent("entryAfter"));
    }

    @Test
    @DisplayName("REQUEST without optional parameters")
    void requestWithoutOptionals() throws Exception {
        CmsQueryLogByTime asdu = new CmsQueryLogByTime(MessageType.REQUEST)
            .logReference("IED1.AP1.LD1.LN1.LOG1")
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsQueryLogByTime result = (CmsQueryLogByTime) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsQueryLogByTime asdu = new CmsQueryLogByTime(MessageType.RESPONSE_POSITIVE)
            .reqId(3);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsQueryLogByTime result = (CmsQueryLogByTime) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertTrue(result.logEntry().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsQueryLogByTime asdu = new CmsQueryLogByTime(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsQueryLogByTime result = (CmsQueryLogByTime) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsQueryLogByTime original = new CmsQueryLogByTime(MessageType.REQUEST)
            .logReference("IED1.AP1.LD1.LN1.LOG1")
            .reqId(10);

        CmsQueryLogByTime copy = original.copy();
        assertEquals(original.reqId().get(), copy.reqId().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("getServiceCode returns QUERY_LOG_BY_TIME")
    void serviceCode() {
        CmsQueryLogByTime asdu = new CmsQueryLogByTime(MessageType.REQUEST);
        assertEquals(ServiceName.QUERY_LOG_BY_TIME, asdu.getServiceName());
    }
}
