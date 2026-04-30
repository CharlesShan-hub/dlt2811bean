package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues.CmsDataEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetAllDataValues")
class CmsGetAllDataValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .fc("ST")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetAllDataValues result = (CmsGetAllDataValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertEquals("ST", result.fc().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("REQUEST with lnReference instead of ldName")
    void requestWithLnReference() throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.REQUEST)
            .lnReference("IED1.AP1.LD1.LN1")
            .fc("MX")
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetAllDataValues result = (CmsGetAllDataValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1", result.lnReference().get());
        assertEquals("MX", result.fc().get());
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("REQUEST without optional fc and referenceAfter")
    void requestWithoutOptionals() throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetAllDataValues result = (CmsGetAllDataValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.RESPONSE_POSITIVE)
            .reqId(4);

        CmsDataEntry entry1 = new CmsDataEntry()
            .reference("DO1")
            .value(new CmsInt32(100));
        CmsDataEntry entry2 = new CmsDataEntry()
            .reference("DO2")
            .value(new CmsInt32(200));
        asdu.data().add(entry1);
        asdu.data().add(entry2);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetAllDataValues result = (CmsGetAllDataValues) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(2, result.data().size());
        assertEquals("DO1", result.data().get(0).reference().get());
        assertEquals("DO2", result.data().get(1).reference().get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty data list")
    void positiveResponseEmpty() throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.RESPONSE_POSITIVE)
            .reqId(5);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetAllDataValues result = (CmsGetAllDataValues) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertTrue(result.data().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(6);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetAllDataValues result = (CmsGetAllDataValues) decoded.getAsdu();
        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetAllDataValues service = new CmsGetAllDataValues(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .fc("ST")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1")
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsGetAllDataValues.write(pos, service);

        CmsGetAllDataValues result = CmsGetAllDataValues.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertEquals("ST", result.fc().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetAllDataValues original = new CmsGetAllDataValues(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .fc("ST")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1")
            .reqId(10);

        CmsGetAllDataValues copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.ldName().get(), copy.ldName().get());
        assertEquals(original.fc().get(), copy.fc().get());
        assertEquals(original.referenceAfter().get(), copy.referenceAfter().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_ALL_DATA_VALUES")
    void serviceCode() {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.REQUEST);
        assertEquals(ServiceName.GET_ALL_DATA_VALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .fc("ST")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1")
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetAllDataValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
        assertTrue(str.contains("ldName: (CmsObjectName) IED1.AP1.LD1"));
        assertTrue(str.contains("fc: (CmsFC) ST"));
        assertTrue(str.contains("referenceAfter: (CmsObjectReference) IED1.AP1.LD1.LN1.DO1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_POSITIVE")
    void toStringPositive() {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);
        CmsDataEntry entry = new CmsDataEntry()
            .reference("DO1")
            .value(new CmsInt32(42));
        asdu.data().add(entry);
        asdu.moreFollows().set(true);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetAllDataValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("moreFollows: (CmsBoolean) true"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetAllDataValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 3"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
