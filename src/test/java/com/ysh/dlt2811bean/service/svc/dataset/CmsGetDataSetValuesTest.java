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

@DisplayName("CmsGetDataSetValues")
class CmsGetDataSetValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataSetValues result = (CmsGetDataSetValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1.DA1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataSetValues result = (CmsGetDataSetValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.RESPONSE_POSITIVE)
            .reqId(3);
        asdu.value().add(new CmsInt32(100));
        asdu.value().add(new CmsInt32(200));
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataSetValues result = (CmsGetDataSetValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(2, result.value().size());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty value list")
    void positiveResponseEmpty() throws Exception {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.RESPONSE_POSITIVE)
            .reqId(4);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataSetValues result = (CmsGetDataSetValues) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertTrue(result.value().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(5);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataSetValues result = (CmsGetDataSetValues) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetDataSetValues service = new CmsGetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsGetDataSetValues.write(pos, service);

        CmsGetDataSetValues result = CmsGetDataSetValues.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetDataSetValues original = new CmsGetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
            .reqId(10);

        CmsGetDataSetValues copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.datasetReference().get(), copy.datasetReference().get());
        assertEquals(original.referenceAfter().get(), copy.referenceAfter().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_DATA_SET_VALUES")
    void serviceCode() {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.REQUEST);
        assertEquals(ServiceName.GET_DATA_SET_VALUES, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataSetValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataSetValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}