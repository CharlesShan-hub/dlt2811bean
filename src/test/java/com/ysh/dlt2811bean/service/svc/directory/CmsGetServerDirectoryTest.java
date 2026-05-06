package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetServerDirectory")
class CmsGetServerDirectoryTest implements
        ServiceNameTest<CmsGetServerDirectory>,
        CopyTest<CmsGetServerDirectory>,
        FromFlagsTest<CmsGetServerDirectory> {

    // ==================== Mixin factory methods ====================

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_SERVER_DIRECTORY;
    }

    @Override
    public CmsGetServerDirectory createAsdu() {
        return new CmsGetServerDirectory(MessageType.REQUEST);
    }

    @Override
    public CmsGetServerDirectory createCopyableAsdu() {
        return new CmsGetServerDirectory(MessageType.REQUEST)
                .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
                .referenceAfter("IED1.AP1.LD1")
                .reqId(10);
    }

    @Override
    public CmsGetServerDirectory createFromFlags(boolean resp, boolean err) {
        return new CmsGetServerDirectory(resp, err);
    }

    // ==================== REQUEST tests ====================

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetServerDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetServerDirectory(MessageType.REQUEST)
                        .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
                        .referenceAfter("IED1.AP1.LD1")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals(CmsObjectClass.LOGICAL_DEVICE, result.objectClass().get());
        assertEquals("IED1.AP1.LD1", result.referenceAfter().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetServerDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetServerDirectory(MessageType.REQUEST)
                        .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals(CmsObjectClass.LOGICAL_DEVICE, result.objectClass().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    // ==================== RESPONSE_POSITIVE tests ====================

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE).reqId(3);
        asdu.reference().add(new CmsObjectReference("IED1.AP1.LD1"));
        asdu.reference().add(new CmsObjectReference("IED1.AP1.LD2"));
        asdu.moreFollows().set(true);

        CmsGetServerDirectory result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(3, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertEquals("IED1.AP1.LD1", result.reference().get(0).get());
        assertEquals("IED1.AP1.LD2", result.reference().get(1).get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty reference list")
    void positiveResponseEmpty() throws Exception {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE).reqId(4);
        asdu.moreFollows().set(false);
        CmsGetServerDirectory result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(4, result.reqId().get());
        assertTrue(result.reference().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    // ==================== RESPONSE_NEGATIVE tests ====================

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetServerDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetServerDirectory(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(5));

        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    // ==================== ASDU-only round-trip ====================

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetServerDirectory result = AsduTestUtil.roundTripViaAsdu(
                new CmsGetServerDirectory(MessageType.REQUEST)
                        .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
                        .referenceAfter("IED1.AP1.LD1")
                        .reqId(10));

        assertEquals(10, result.reqId().get());
        assertEquals(CmsObjectClass.LOGICAL_DEVICE, result.objectClass().get());
        assertEquals("IED1.AP1.LD1", result.referenceAfter().get());
    }

    // ==================== toString tests ====================

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
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE).reqId(2);
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
