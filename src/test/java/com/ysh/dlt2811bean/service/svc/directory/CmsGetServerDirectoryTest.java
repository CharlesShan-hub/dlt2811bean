package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetServerDirectory")
class CmsGetServerDirectoryTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.REQUEST)
            .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
            .referenceAfter("IED1.AP1.LD1")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetServerDirectory result = (CmsGetServerDirectory) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(CmsObjectClass.LOGICAL_DEVICE, result.objectClass().get());
        assertEquals("IED1.AP1.LD1", result.referenceAfter().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        System.out.println(result);
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.REQUEST)
            .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetServerDirectory result = (CmsGetServerDirectory) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(CmsObjectClass.LOGICAL_DEVICE, result.objectClass().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(3);
        asdu.reference().add(new CmsObjectReference("IED1.AP1.LD1"));
        asdu.reference().add(new CmsObjectReference("IED1.AP1.LD2"));
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetServerDirectory result = (CmsGetServerDirectory) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertEquals("IED1.AP1.LD1", result.reference().get(0).get());
        assertEquals("IED1.AP1.LD2", result.reference().get(1).get());
        assertTrue(result.isFieldPresent("moreFollows"));
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty reference list")
    void positiveResponseEmpty() throws Exception {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(4);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetServerDirectory result = (CmsGetServerDirectory) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertTrue(result.reference().isEmpty());
        assertFalse(result.isFieldPresent("moreFollows"));
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(5);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetServerDirectory result = (CmsGetServerDirectory) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetServerDirectory service = new CmsGetServerDirectory(MessageType.REQUEST)
            .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
            .referenceAfter("IED1.AP1.LD1")
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsGetServerDirectory.write(pos, service);

        CmsGetServerDirectory result = CmsGetServerDirectory.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals(CmsObjectClass.LOGICAL_DEVICE, result.objectClass().get());
        assertEquals("IED1.AP1.LD1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetServerDirectory original = new CmsGetServerDirectory(MessageType.REQUEST)
            .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
            .referenceAfter("IED1.AP1.LD1")
            .reqId(10);

        CmsGetServerDirectory copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.objectClass().get(), copy.objectClass().get());
        assertEquals(original.referenceAfter().get(), copy.referenceAfter().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_SERVER_DIRECTORY")
    void serviceCode() {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.REQUEST);
        assertEquals(ServiceName.GET_SERVER_DIRECTORY, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.REQUEST)
            .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
            .referenceAfter("IED1.AP1.LD1")
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetServerDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
        assertTrue(str.contains("objectClass: (CmsObjectClass) 1"));
        assertTrue(str.contains("referenceAfter: (CmsObjectReference) IED1.AP1.LD1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_POSITIVE")
    void toStringPositive() {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(2);
        asdu.reference().add(new CmsObjectReference("IED1.AP1.LD1"));
        asdu.moreFollows().set(true);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetServerDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("reference: (CmsArray) [(CmsObjectReference) IED1.AP1.LD1]"));
        assertTrue(str.contains("moreFollows: (CmsBoolean) true"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetServerDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 3"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
