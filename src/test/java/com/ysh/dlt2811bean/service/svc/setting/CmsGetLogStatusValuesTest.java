package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorLogStatusChoice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetLogStatusValues")
class CmsGetLogStatusValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetLogStatusValues asdu = new CmsGetLogStatusValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addLogReference("IED1.AP1.LD1.LN1.LOG1");
        asdu.addLogReference("IED1.AP1.LD1.LN1.LOG2");

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogStatusValues result = (CmsGetLogStatusValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.logReference().size());
        assertEquals("IED1.AP1.LD1.LN1.LOG1", result.logReference().get(0).get());
        assertEquals("IED1.AP1.LD1.LN1.LOG2", result.logReference().get(1).get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetLogStatusValues asdu = new CmsGetLogStatusValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsErrorLogStatusChoice errorChoice = new CmsErrorLogStatusChoice()
            .selectError();
        errorChoice.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addLogChoice(errorChoice);

        CmsErrorLogStatusChoice valueChoice = new CmsErrorLogStatusChoice()
            .selectValue();
        valueChoice.value.oldEntrTm.msOfDay(10000L).daysSince1984(15000);
        valueChoice.value.newEntrTm.msOfDay(50000L).daysSince1984(15000);
        valueChoice.value.oldEntr.set(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07});
        valueChoice.value.newEntr.set(new byte[]{0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F});
        asdu.addLogChoice(valueChoice);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogStatusValues result = (CmsGetLogStatusValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(2, result.log().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.log().get(0).error.get());
        assertEquals(10000L, result.log().get(1).value.oldEntrTm.msOfDay.get());
        assertEquals(50000L, result.log().get(1).value.newEntrTm.msOfDay.get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetLogStatusValues asdu = new CmsGetLogStatusValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogStatusValues result = (CmsGetLogStatusValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetLogStatusValues original = new CmsGetLogStatusValues(MessageType.REQUEST)
            .reqId(10);
        original.addLogReference("IED1.AP1.LD1.LN1.LOG1");

        CmsGetLogStatusValues copy = original.copy();
        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.logReference().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("getServiceCode returns GET_LOG_STATUS_VALUES")
    void serviceCode() {
        CmsGetLogStatusValues asdu = new CmsGetLogStatusValues(MessageType.REQUEST);
        assertEquals(ServiceName.GET_LOG_STATUS_VALUES, asdu.getServiceName());
    }
}
