package com.ysh.dlt2811bean.service.svc.dataset;

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

@DisplayName("CmsSetDataSetValues")
class CmsSetDataSetValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
            .addMemberValue(new CmsInt32(100))
            .addMemberValue(new CmsInt32(200))
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataSetValues result = (CmsSetDataSetValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1.DA1", result.referenceAfter().get());
        assertEquals(2, result.memberValue().size());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .addMemberValue(new CmsInt32(42))
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataSetValues result = (CmsSetDataSetValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
        assertEquals(1, result.memberValue().size());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.RESPONSE_POSITIVE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataSetValues result = (CmsSetDataSetValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(4);
        asdu.addResult(CmsServiceError.ACCESS_VIOLATION);
        asdu.addResult(CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataSetValues result = (CmsSetDataSetValues) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(2, result.result().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.result().get(0).get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.result().get(1).get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: empty result list")
    void negativeResponseEmpty() throws Exception {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(5);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetDataSetValues result = (CmsSetDataSetValues) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertTrue(result.result().isEmpty());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsSetDataSetValues service = new CmsSetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .addMemberValue(new CmsInt32(42))
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsSetDataSetValues.write(pos, service);

        CmsSetDataSetValues result = CmsSetDataSetValues.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertEquals(1, result.memberValue().size());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsSetDataSetValues original = new CmsSetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
            .addMemberValue(new CmsInt32(42))
            .reqId(10);

        CmsSetDataSetValues copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.datasetReference().get(), copy.datasetReference().get());
        assertEquals(original.referenceAfter().get(), copy.referenceAfter().get());
        assertEquals(1, copy.memberValue().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns SET_DATA_SET_VALUES")
    void serviceCode() {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.REQUEST);
        assertEquals(ServiceName.SET_DATA_SET_VALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .addMemberValue(new CmsInt32(100))
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSetDataSetValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(2);
        asdu.addResult(CmsServiceError.ACCESS_VIOLATION);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSetDataSetValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
    }
}