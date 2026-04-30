package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetLogicalNodeDirectory")
class CmsGetLogicalNodeDirectoryTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .acsiClass(new CmsACSIClass(CmsACSIClass.DATA_OBJECT))
            .referenceAfter("IED1.AP1.LD1.LN1.DO1")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalNodeDirectory result = (CmsGetLogicalNodeDirectory) decoded.getAsdu();
        System.out.println(result);
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertEquals(CmsACSIClass.DATA_OBJECT, result.acsiClass().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("REQUEST with lnReference instead of ldName")
    void requestWithLnReference() throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
            .lnReference("IED1.AP1.LD1.LN1")
            .acsiClass(new CmsACSIClass(CmsACSIClass.BRCB))
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalNodeDirectory result = (CmsGetLogicalNodeDirectory) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1", result.lnReference().get());
        assertEquals(CmsACSIClass.BRCB, result.acsiClass().get());
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .acsiClass(new CmsACSIClass(CmsACSIClass.DATA_SET))
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalNodeDirectory result = (CmsGetLogicalNodeDirectory) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertEquals(CmsACSIClass.DATA_SET, result.acsiClass().get());
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(4);
        asdu.reference().add(new CmsSubReference("DO1"));
        asdu.reference().add(new CmsSubReference("DO2"));
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalNodeDirectory result = (CmsGetLogicalNodeDirectory) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertEquals("DO1", result.reference().get(0).get());
        assertEquals("DO2", result.reference().get(1).get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty reference list")
    void positiveResponseEmpty() throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(5);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalNodeDirectory result = (CmsGetLogicalNodeDirectory) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertTrue(result.reference().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(6);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetLogicalNodeDirectory result = (CmsGetLogicalNodeDirectory) decoded.getAsdu();
        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetLogicalNodeDirectory service = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .acsiClass(new CmsACSIClass(CmsACSIClass.URCB))
            .referenceAfter("IED1.AP1.LD1.LN1.DO1")
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsGetLogicalNodeDirectory.write(pos, service);

        CmsGetLogicalNodeDirectory result = CmsGetLogicalNodeDirectory.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertEquals(CmsACSIClass.URCB, result.acsiClass().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetLogicalNodeDirectory original = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .acsiClass(new CmsACSIClass(CmsACSIClass.GO_CB))
            .referenceAfter("IED1.AP1.LD1.LN1.DO1")
            .reqId(10);

        CmsGetLogicalNodeDirectory copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.ldName().get(), copy.ldName().get());
        assertEquals(original.acsiClass().get(), copy.acsiClass().get());
        assertEquals(original.referenceAfter().get(), copy.referenceAfter().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_LOGIC_NODE_DIRECTORY")
    void serviceCode() {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST);
        assertEquals(ServiceName.GET_LOGIC_NODE_DIRECTORY, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .acsiClass(new CmsACSIClass(CmsACSIClass.DATA_OBJECT))
            .referenceAfter("IED1.AP1.LD1.LN1.DO1")
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetLogicalNodeDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
        assertTrue(str.contains("ldName: (CmsObjectName) IED1.AP1.LD1"));
        assertTrue(str.contains("acsiClass: (CmsACSIClass) 1"));
        assertTrue(str.contains("referenceAfter: (CmsObjectReference) IED1.AP1.LD1.LN1.DO1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_POSITIVE")
    void toStringPositive() {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(2);
        asdu.reference().add(new CmsSubReference("DO1"));
        asdu.moreFollows().set(true);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetLogicalNodeDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("reference: (CmsArray) [(CmsSubReference) DO1]"));
        assertTrue(str.contains("moreFollows: (CmsBoolean) true"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetLogicalNodeDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 3"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
