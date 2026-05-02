package com.ysh.dlt2811bean.service.svc.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDeleteDataSet")
class CmsDeleteDataSetTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsDeleteDataSet result = (CmsDeleteDataSet) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsDeleteDataSet result = (CmsDeleteDataSet) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsDeleteDataSet result = (CmsDeleteDataSet) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsDeleteDataSet service = new CmsDeleteDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsDeleteDataSet.write(pos, service);

        CmsDeleteDataSet result = CmsDeleteDataSet.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsDeleteDataSet original = new CmsDeleteDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .reqId(10);

        CmsDeleteDataSet copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.datasetReference().get(), copy.datasetReference().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns DELETE_DATA_SET")
    void serviceCode() {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(MessageType.REQUEST);
        assertEquals(ServiceName.DELETE_DATA_SET, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsDeleteDataSet) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsDeleteDataSet) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}