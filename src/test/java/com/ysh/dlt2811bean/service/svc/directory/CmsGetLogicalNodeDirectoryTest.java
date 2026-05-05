package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetLogicalNodeDirectory")
class CmsGetLogicalNodeDirectoryTest implements
        ServiceNameTest<CmsGetLogicalNodeDirectory>,
        CopyTest<CmsGetLogicalNodeDirectory>,
        FromFlagsTest<CmsGetLogicalNodeDirectory> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_LOGIC_NODE_DIRECTORY;
    }

    @Override
    public CmsGetLogicalNodeDirectory createAsdu() {
        return new CmsGetLogicalNodeDirectory(MessageType.REQUEST);
    }

    @Override
    public CmsGetLogicalNodeDirectory createCopyableAsdu() {
        return new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                .ldName("IED1.AP1.LD1")
                .acsiClass(new CmsACSIClass(CmsACSIClass.GO_CB))
                .referenceAfter("IED1.AP1.LD1.LN1.DO1")
                .reqId(10);
    }

    @Override
    public CmsGetLogicalNodeDirectory createFromFlags(boolean resp, boolean err) {
        return new CmsGetLogicalNodeDirectory(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetLogicalNodeDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                        .ldName("IED1.AP1.LD1")
                        .acsiClass(new CmsACSIClass(CmsACSIClass.DATA_OBJECT))
                        .referenceAfter("IED1.AP1.LD1.LN1.DO1")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.referenceRequest().ldName.get());
        assertEquals(CmsACSIClass.DATA_OBJECT, result.acsiClass().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("REQUEST with lnReference instead of ldName")
    void requestWithLnReference() throws Exception {
        CmsGetLogicalNodeDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                        .lnReference("IED1.AP1.LD1.LN1")
                        .acsiClass(new CmsACSIClass(CmsACSIClass.BRCB))
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1", result.referenceRequest().lnReference.get());
        assertEquals(CmsACSIClass.BRCB, result.acsiClass().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetLogicalNodeDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                        .ldName("IED1.AP1.LD1")
                        .acsiClass(new CmsACSIClass(CmsACSIClass.DATA_SET))
                        .reqId(3));

        assertEquals(3, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.referenceRequest().ldName.get());
        assertEquals(CmsACSIClass.DATA_SET, result.acsiClass().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_POSITIVE).reqId(4);
        asdu.referenceResponse().add(new CmsSubReference("DO1"));
        asdu.referenceResponse().add(new CmsSubReference("DO2"));
        asdu.moreFollows().set(true);

        CmsGetLogicalNodeDirectory result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(4, result.reqId().get());
        assertEquals(2, result.referenceResponse().size());
        assertEquals("DO1", result.referenceResponse().get(0).get());
        assertEquals("DO2", result.referenceResponse().get(1).get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty reference list")
    void positiveResponseEmpty() throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_POSITIVE).reqId(5);
        asdu.moreFollows().set(false);
        CmsGetLogicalNodeDirectory result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(5, result.reqId().get());
        assertTrue(result.referenceResponse().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetLogicalNodeDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(6));

        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
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
    }
}
