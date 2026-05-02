package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetEditSGValue")
class CmsGetEditSGValueTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.REQUEST)
            .reqId(1);

        asdu.addData("IED1.AP1.LD1.LN1.DO1", "SG");
        asdu.addData("IED1.AP1.LD1.LN1.DO2", "SE");

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetEditSGValue result = (CmsGetEditSGValue) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("SG", result.data().get(0).fc().get());
        assertEquals("IED1.AP1.LD1.LN1.DO2", result.data().get(1).reference().get());
        assertEquals("SE", result.data().get(1).fc().get());
    }

    @Test
    @DisplayName("REQUEST: empty data list")
    void requestEmptyData() throws Exception {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.REQUEST)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetEditSGValue result = (CmsGetEditSGValue) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertTrue(result.data().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.RESPONSE_POSITIVE)
            .reqId(3);
        asdu.value().add(new CmsInt32(100));
        asdu.value().add(new CmsInt32(200));
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetEditSGValue result = (CmsGetEditSGValue) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(2, result.value().size());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty value list")
    void positiveResponseEmpty() throws Exception {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.RESPONSE_POSITIVE)
            .reqId(4);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetEditSGValue result = (CmsGetEditSGValue) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertTrue(result.value().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(5);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetEditSGValue result = (CmsGetEditSGValue) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetEditSGValue service = new CmsGetEditSGValue(MessageType.REQUEST)
            .reqId(10);

        service.addData("IED1.AP1.LD1.LN1.DO1", "SG");

        PerOutputStream pos = new PerOutputStream();
        CmsGetEditSGValue.write(pos, service);

        CmsGetEditSGValue result = CmsGetEditSGValue.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("SG", result.data().get(0).fc().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetEditSGValue original = new CmsGetEditSGValue(MessageType.REQUEST)
            .reqId(10);

        original.addData("IED1.AP1.LD1.LN1.DO1", "SG");

        CmsGetEditSGValue copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.data().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_EDIT_SG_VALUE")
    void serviceCode() {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.REQUEST);
        assertEquals(ServiceName.GET_EDIT_SG_VALUE, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.REQUEST)
            .reqId(1);

        asdu.addData("IED1.AP1.LD1.LN1.DO1", "SG");

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetEditSGValue) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetEditSGValue) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
