package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDirectoryEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetDataDirectory")
class CmsGetDataDirectoryTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.REQUEST)
            .dataReference("IED1.AP1.LD1.LN1.DO1")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA2")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDirectory result = (CmsGetDataDirectory) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.dataReference().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1.DA2", result.referenceAfter().get());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.REQUEST)
            .dataReference("IED1.AP1.LD1.LN1.DO1")
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDirectory result = (CmsGetDataDirectory) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.dataReference().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(3);

        CmsGetDataDirectoryEntry entry1 = new CmsGetDataDirectoryEntry()
            .reference("DO1.DA1")
            .fc("ST");
        CmsGetDataDirectoryEntry entry2 = new CmsGetDataDirectoryEntry()
            .reference("DO1.DA2")
            .fc("MX");
        asdu.dataAttribute().add(entry1);
        asdu.dataAttribute().add(entry2);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDirectory result = (CmsGetDataDirectory) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(2, result.dataAttribute().size());
        assertEquals("DO1.DA1", result.dataAttribute().get(0).reference().get());
        assertEquals("ST", result.dataAttribute().get(0).fc().get());
        assertEquals("DO1.DA2", result.dataAttribute().get(1).reference().get());
        assertEquals("MX", result.dataAttribute().get(1).fc().get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty dataAttribute list")
    void positiveResponseEmpty() throws Exception {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(4);
        asdu.moreFollows().set(false);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDirectory result = (CmsGetDataDirectory) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertTrue(result.dataAttribute().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(5);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetDataDirectory result = (CmsGetDataDirectory) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetDataDirectory service = new CmsGetDataDirectory(MessageType.REQUEST)
            .dataReference("IED1.AP1.LD1.LN1.DO1")
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsGetDataDirectory.write(pos, service);

        CmsGetDataDirectory result = CmsGetDataDirectory.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.dataReference().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsGetDataDirectory original = new CmsGetDataDirectory(MessageType.REQUEST)
            .dataReference("IED1.AP1.LD1.LN1.DO1")
            .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA2")
            .reqId(10);

        CmsGetDataDirectory copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.dataReference().get(), copy.dataReference().get());
        assertEquals(original.referenceAfter().get(), copy.referenceAfter().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns GET_DATA_DIRECTORY")
    void serviceCode() {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.REQUEST);
        assertEquals(ServiceName.GET_DATA_DIRECTORY, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.REQUEST)
            .dataReference("IED1.AP1.LD1.LN1.DO1")
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataDirectory) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}