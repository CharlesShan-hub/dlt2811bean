package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDefinitionEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetDataDefinition")
class CmsGetDataDefinitionTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST)
            .reqId(1);

        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST");
        asdu.addData("IED1.AP1.LD1.LN1.DO2", "MX");

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDefinition result = (CmsGetDataDefinition) decoded.getAsdu();
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
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST)
            .reqId(2);

        asdu.addData("IED1.AP1.LD1.LN1.DO1");

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDefinition result = (CmsGetDataDefinition) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertFalse(result.data().get(0).isFieldPresent("fc"));
    }

    @Test
    @DisplayName("REQUEST: empty data list")
    void requestEmptyData() throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDefinition result = (CmsGetDataDefinition) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertTrue(result.data().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.RESPONSE_POSITIVE)
            .reqId(4);

        CmsGetDataDefinitionEntry entry1 = new CmsGetDataDefinitionEntry()
            .cdcType("INC")
            .definition(ofInt32());
        CmsGetDataDefinitionEntry entry2 = new CmsGetDataDefinitionEntry()
            .definition(ofBoolean());
        asdu.definition().add(entry1);
        asdu.definition().add(entry2);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDefinition result = (CmsGetDataDefinition) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(2, result.definition().size());
        assertTrue(result.definition().get(0).isFieldPresent("cdcType"));
        assertEquals("INC", result.definition().get(0).cdcType().get());
        assertEquals(CmsDataDefinition.INT32, result.definition().get(0).definition().getChoiceIndex());
        assertFalse(result.definition().get(1).isFieldPresent("cdcType"));
        assertEquals(CmsDataDefinition.BOOLEAN, result.definition().get(1).definition().getChoiceIndex());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty definition list")
    void positiveResponseEmpty() throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.RESPONSE_POSITIVE)
            .reqId(5);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDefinition result = (CmsGetDataDefinition) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertTrue(result.definition().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(6);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDefinition result = (CmsGetDataDefinition) decoded.getAsdu();
        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetDataDefinition service = new CmsGetDataDefinition(MessageType.REQUEST)
            .reqId(10);

        service.addData("IED1.AP1.LD1.LN1.DO1", "ST");

        PerOutputStream pos = new PerOutputStream();
        CmsGetDataDefinition.write(pos, service);

        CmsGetDataDefinition result = CmsGetDataDefinition.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetDataDefinition original = new CmsGetDataDefinition(MessageType.REQUEST)
            .reqId(10);

        original.addData("IED1.AP1.LD1.LN1.DO1", "ST");

        CmsGetDataDefinition copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(1, copy.data().size());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_DATA_DEFINITION")
    void serviceCode() {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST);
        assertEquals(ServiceName.GET_DATA_DEFINITION, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST)
            .reqId(1);

        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST");

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataDefinition) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataDefinition) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }

    @Test
    @DisplayName("addData convenience methods")
    void addDataConvenience() {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST)
            .reqId(1);

        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST");
        asdu.addData("IED1.AP1.LD1.LN1.DO2");

        assertEquals(2, asdu.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", asdu.data().get(0).reference().get());
        assertEquals("ST", asdu.data().get(0).fc().get());
        assertEquals("IED1.AP1.LD1.LN1.DO2", asdu.data().get(1).reference().get());
        assertTrue(asdu.data().get(1).fc().get().isEmpty());
    }
}