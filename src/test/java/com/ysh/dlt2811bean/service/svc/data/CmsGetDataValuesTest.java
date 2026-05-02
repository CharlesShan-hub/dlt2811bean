package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetDataValues")
class CmsGetDataValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST)
            .reqId(1);

        CmsGetDataValuesEntry entry1 = new CmsGetDataValuesEntry()
            .reference("IED1.AP1.LD1.LN1.DO1")
            .fc("ST");
        CmsGetDataValuesEntry entry2 = new CmsGetDataValuesEntry()
            .reference("IED1.AP1.LD1.LN1.DO2")
            .fc("MX");
        asdu.data().add(entry1);
        asdu.data().add(entry2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataValues result = (CmsGetDataValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
        assertEquals("IED1.AP1.LD1.LN1.DO2", result.data().get(1).reference().get());
        assertEquals("MX", result.data().get(1).fc().get());
        assertTrue(result.data().get(0).isFieldPresent("fc"));
        assertTrue(result.data().get(1).isFieldPresent("fc"));
    }

    @Test
    @DisplayName("REQUEST without optional fc")
    void requestWithoutFc() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST)
            .reqId(2);

        CmsGetDataValuesEntry entry1 = new CmsGetDataValuesEntry()
            .reference("IED1.AP1.LD1.LN1.DO1");
        asdu.data().add(entry1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataValues result = (CmsGetDataValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertFalse(result.data().get(0).isFieldPresent("fc"));
        assertTrue(result.data().get(0).fc().get().isEmpty());
    }

    @Test
    @DisplayName("REQUEST: empty data list")
    void requestEmptyData() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataValues result = (CmsGetDataValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertTrue(result.data().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.RESPONSE_POSITIVE)
            .reqId(4);
        asdu.value().add(new CmsInt32(100));
        asdu.value().add(new CmsInt32(200));
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataValues result = (CmsGetDataValues) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(2, result.value().size());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty value list")
    void positiveResponseEmpty() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.RESPONSE_POSITIVE)
            .reqId(5);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataValues result = (CmsGetDataValues) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertTrue(result.value().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(6);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataValues result = (CmsGetDataValues) decoded.getAsdu();
        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetDataValues service = new CmsGetDataValues(MessageType.REQUEST)
            .reqId(10);

        CmsGetDataValuesEntry entry = new CmsGetDataValuesEntry()
            .reference("IED1.AP1.LD1.LN1.DO1")
            .fc("ST");
        service.data().add(entry);

        PerOutputStream pos = new PerOutputStream();
        CmsGetDataValues.write(pos, service);

        CmsGetDataValues result = CmsGetDataValues.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
        assertTrue(result.data().get(0).isFieldPresent("fc"));
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetDataValues original = new CmsGetDataValues(MessageType.REQUEST)
            .reqId(10);

        CmsGetDataValuesEntry entry = new CmsGetDataValuesEntry()
            .reference("IED1.AP1.LD1.LN1.DO1")
            .fc("ST");
        original.data().add(entry);

        CmsGetDataValues copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", copy.data().get(0).reference().get());
        assertEquals("ST", copy.data().get(0).fc().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetDataValues asdu = new CmsGetDataValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetDataValues asdu = new CmsGetDataValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetDataValues asdu = new CmsGetDataValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_DATA_VALUES")
    void serviceCode() {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST);
        assertEquals(ServiceName.GET_DATA_VALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST)
            .reqId(1);

        CmsGetDataValuesEntry entry = new CmsGetDataValuesEntry()
            .reference("IED1.AP1.LD1.LN1.DO1")
            .fc("ST");
        asdu.data().add(entry);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}