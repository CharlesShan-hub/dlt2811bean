package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetLogicalDeviceDirectory")
class CmsGetLogicalDeviceDirectoryTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .referenceAfter("IED1.AP1.LD1.LN1")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.REQUEST);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalDeviceDirectory result = (CmsGetLogicalDeviceDirectory) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertEquals("IED1.AP1.LD1.LN1", result.referenceAfter().get());

        System.out.println(result);
    }

    @Test
    @DisplayName("REQUEST without optional ldName and referenceAfter")
    void requestWithoutOptionals() throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.REQUEST);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalDeviceDirectory result = (CmsGetLogicalDeviceDirectory) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertTrue(result.ldName().get().isEmpty());
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("REQUEST with ldName only")
    void requestWithLdNameOnly() throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD2")
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.REQUEST);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalDeviceDirectory result = (CmsGetLogicalDeviceDirectory) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals("IED1.AP1.LD2", result.ldName().get());
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(4);
        asdu.lnReference().add(new CmsSubReference("LN1"));
        asdu.lnReference().add(new CmsSubReference("LN2"));
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.RESPONSE_POSITIVE);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalDeviceDirectory result = (CmsGetLogicalDeviceDirectory) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(2, result.lnReference().size());
        assertEquals("LN1", result.lnReference().get(0).get());
        assertEquals("LN2", result.lnReference().get(1).get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty reference list")
    void positiveResponseEmpty() throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(5);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.RESPONSE_POSITIVE);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalDeviceDirectory result = (CmsGetLogicalDeviceDirectory) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertTrue(result.lnReference().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(6);

        CmsApdu apdu = new CmsApdu(asdu, MessageType.RESPONSE_NEGATIVE);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalDeviceDirectory result = (CmsGetLogicalDeviceDirectory) decoded.getAsdu();
        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetLogicalDeviceDirectory service = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .referenceAfter("IED1.AP1.LD1.LN1")
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsGetLogicalDeviceDirectory.write(pos, service);

        CmsGetLogicalDeviceDirectory result = CmsGetLogicalDeviceDirectory.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertEquals("IED1.AP1.LD1.LN1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetLogicalDeviceDirectory original = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .referenceAfter("IED1.AP1.LD1.LN1")
            .reqId(10);

        CmsGetLogicalDeviceDirectory copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.ldName().get(), copy.ldName().get());
        assertEquals(original.referenceAfter().get(), copy.referenceAfter().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_LOGIC_DEVICE_DIRECTORY")
    void serviceCode() {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
        assertEquals(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .referenceAfter("IED1.AP1.LD1.LN1")
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetLogicalDeviceDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
        assertTrue(str.contains("ldName: (CmsObjectName) IED1.AP1.LD1"));
        assertTrue(str.contains("referenceAfter: (CmsObjectReference) IED1.AP1.LD1.LN1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_POSITIVE")
    void toStringPositive() {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(2);
        asdu.lnReference().add(new CmsSubReference("LN1"));
        asdu.moreFollows().set(true);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetLogicalDeviceDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("lnReference: (CmsArray) [(CmsSubReference) LN1]"));
        assertTrue(str.contains("moreFollows: (CmsBoolean) true"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetLogicalDeviceDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 3"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
