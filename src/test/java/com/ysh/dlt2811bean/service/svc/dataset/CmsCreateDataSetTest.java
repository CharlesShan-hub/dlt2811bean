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

@DisplayName("CmsCreateDataSet")
class CmsCreateDataSetTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
            .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
            .addMemberData("IED1.AP1.LD1.LN1.DO2", "MX")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsCreateDataSet result = (CmsCreateDataSet) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1.DA1", result.referenceAfter().get());
        assertEquals(2, result.memberData().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.memberData().get(0).reference().get());
        assertEquals("ST", result.memberData().get(0).fc().get());
        assertEquals("IED1.AP1.LD1.LN1.DO2", result.memberData().get(1).reference().get());
        assertEquals("MX", result.memberData().get(1).fc().get());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsCreateDataSet result = (CmsCreateDataSet) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
        assertEquals(1, result.memberData().size());
    }

    @Test
    @DisplayName("REQUEST with empty memberData")
    void requestEmptyMemberData() throws Exception {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsCreateDataSet result = (CmsCreateDataSet) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertTrue(result.memberData().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsCreateDataSet result = (CmsCreateDataSet) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(5);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsCreateDataSet result = (CmsCreateDataSet) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsCreateDataSet service = new CmsCreateDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsCreateDataSet.write(pos, service);

        CmsCreateDataSet result = CmsCreateDataSet.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertEquals(1, result.memberData().size());
        assertEquals("ST", result.memberData().get(0).fc().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsCreateDataSet original = new CmsCreateDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
            .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
            .reqId(10);

        CmsCreateDataSet copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.datasetReference().get(), copy.datasetReference().get());
        assertEquals(original.referenceAfter().get(), copy.referenceAfter().get());
        assertEquals(1, copy.memberData().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsCreateDataSet asdu = new CmsCreateDataSet(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsCreateDataSet asdu = new CmsCreateDataSet(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsCreateDataSet asdu = new CmsCreateDataSet(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns CREATE_DATA_SET")
    void serviceCode() {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST);
        assertEquals(ServiceName.CREATE_DATA_SET, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
            .datasetReference("IED1.AP1.LD1.LN1.DS1")
            .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsCreateDataSet) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsCreateDataSet) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}