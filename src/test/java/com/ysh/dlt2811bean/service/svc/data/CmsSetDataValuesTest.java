package com.ysh.dlt2811bean.service.svc.data;

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

@DisplayName("CmsSetDataValues")
class CmsSetDataValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST", new CmsInt32(100));
        asdu.addData("IED1.AP1.LD1.LN1.DO2", "MX", new CmsInt32(200));

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataValues result = (CmsSetDataValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
        assertTrue(result.data().get(0).isFieldPresent("fc"));
        assertEquals("IED1.AP1.LD1.LN1.DO2", result.data().get(1).reference().get());
        assertEquals("MX", result.data().get(1).fc().get());
    }

    @Test
    @DisplayName("REQUEST without optional fc")
    void requestWithoutFc() throws Exception {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST)
            .reqId(2);

        asdu.addData("IED1.AP1.LD1.LN1.DO1", "", new CmsInt32(100));

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataValues result = (CmsSetDataValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertFalse(result.data().get(0).isFieldPresent("fc"));
    }

    @Test
    @DisplayName("REQUEST: empty data list")
    void requestEmptyData() throws Exception {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataValues result = (CmsSetDataValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertTrue(result.data().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.RESPONSE_POSITIVE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataValues result = (CmsSetDataValues) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(5);
        asdu.result().add(new CmsServiceError(CmsServiceError.ACCESS_VIOLATION));
        asdu.result().add(new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE));

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataValues result = (CmsSetDataValues) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertEquals(2, result.result().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.result().get(0).get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.result().get(1).get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: empty result list")
    void negativeResponseEmpty() throws Exception {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(6);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataValues result = (CmsSetDataValues) decoded.getAsdu();
        assertEquals(6, result.reqId().get());
        assertTrue(result.result().isEmpty());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsSetDataValues service = new CmsSetDataValues(MessageType.REQUEST)
            .reqId(10);

        service.addData("IED1.AP1.LD1.LN1.DO1", "ST", new CmsInt32(42));

        PerOutputStream pos = new PerOutputStream();
        CmsSetDataValues.write(pos, service);

        CmsSetDataValues result = CmsSetDataValues.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
        assertTrue(result.data().get(0).isFieldPresent("fc"));
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsSetDataValues original = new CmsSetDataValues(MessageType.REQUEST)
            .reqId(10);

        original.addData("IED1.AP1.LD1.LN1.DO1", "ST", new CmsInt32(42));

        CmsSetDataValues copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.data().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsSetDataValues asdu = new CmsSetDataValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsSetDataValues asdu = new CmsSetDataValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsSetDataValues asdu = new CmsSetDataValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns SET_DATA_VALUES")
    void serviceCode() {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST);
        assertEquals(ServiceName.SET_DATA_VALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST", new CmsInt32(100));

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSetDataValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(2);
        asdu.result().add(new CmsServiceError(CmsServiceError.ACCESS_VIOLATION));

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSetDataValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
    }

    @Test
    @DisplayName("addData convenience method")
    void addDataConvenience() {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST)
            .reqId(1);

        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST", new CmsInt32(100));

        assertEquals(1, asdu.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", asdu.data().get(0).reference().get());
        assertEquals("ST", asdu.data().get(0).fc().get());
    }
}