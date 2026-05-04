package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsLogEntry;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsLogEntryData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsQueryLogAfter")
class CmsQueryLogAfterTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsQueryLogAfter asdu = new CmsQueryLogAfter(MessageType.REQUEST)
            .logReference("IED1.AP1.LD1.LN1.LOG1")
            .startTime(43200000L, 15000)
            .entry(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07})
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsQueryLogAfter result = (CmsQueryLogAfter) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.LOG1", result.logReference().get());
        assertTrue(result.isFieldPresent("startTime"));
        assertArrayEquals(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07}, result.entry().get());
    }

    @Test
    @DisplayName("REQUEST without optional startTime")
    void requestWithoutStartTime() throws Exception {
        CmsQueryLogAfter asdu = new CmsQueryLogAfter(MessageType.REQUEST)
            .logReference("IED1.AP1.LD1.LN1.LOG1")
            .entry(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07})
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsQueryLogAfter result = (CmsQueryLogAfter) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsQueryLogAfter asdu = new CmsQueryLogAfter(MessageType.RESPONSE_POSITIVE)
            .reqId(3);

        CmsLogEntry entry = new CmsLogEntry();
        entry.timeOfEntry.msOfDay(10000L).daysSince1984(15001);
        entry.entryID.set(new byte[]{0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17});

        CmsLogEntryData data = new CmsLogEntryData();
        data.reference.set("DO1");
        data.fc.set("ST");
        data.value(new CmsInt32(42));
        entry.entryData().add(data);
        asdu.logEntry().add(entry);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsQueryLogAfter result = (CmsQueryLogAfter) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(1, result.logEntry().size());
        assertEquals("DO1", result.logEntry().get(0).entryData().get(0).reference().get());
        assertEquals("ST", result.logEntry().get(0).entryData().get(0).fc().get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsQueryLogAfter asdu = new CmsQueryLogAfter(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsQueryLogAfter result = (CmsQueryLogAfter) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsQueryLogAfter original = new CmsQueryLogAfter(MessageType.REQUEST)
            .logReference("IED1.AP1.LD1.LN1.LOG1")
            .entry(new byte[8])
            .reqId(10);

        CmsQueryLogAfter copy = original.copy();
        assertEquals(original.reqId().get(), copy.reqId().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("getServiceCode returns QUERY_LOG_AFTER")
    void serviceCode() {
        CmsQueryLogAfter asdu = new CmsQueryLogAfter(MessageType.REQUEST);
        assertEquals(ServiceName.QUERY_LOG_AFTER, asdu.getServiceName());
    }
}
