package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetLogicalDeviceDirectory")
class CmsGetLogicalDeviceDirectoryTest implements
        ServiceNameTest<CmsGetLogicalDeviceDirectory>,
        CopyTest<CmsGetLogicalDeviceDirectory>,
        FromFlagsTest<CmsGetLogicalDeviceDirectory> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_LOGIC_DEVICE_DIRECTORY;
    }

    @Override
    public CmsGetLogicalDeviceDirectory createAsdu() {
        return new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
    }

    @Override
    public CmsGetLogicalDeviceDirectory createCopyableAsdu() {
        return new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
                .ldName("IED1.AP1.LD1")
                .referenceAfter("IED1.AP1.LD1.LN1")
                .reqId(10);
    }

    @Override
    public CmsGetLogicalDeviceDirectory createFromFlags(boolean resp, boolean err) {
        return new CmsGetLogicalDeviceDirectory(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetLogicalDeviceDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
                        .ldName("IED1.AP1.LD1")
                        .referenceAfter("IED1.AP1.LD1.LN1")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertEquals("IED1.AP1.LD1.LN1", result.referenceAfter().get());
        assertTrue(result.isFieldPresent("ldName"));
        assertTrue(result.isFieldPresent("referenceAfter"));
    }

    @Test
    @DisplayName("REQUEST without optional ldName and referenceAfter")
    void requestWithoutOptionals() throws Exception {
        CmsGetLogicalDeviceDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogicalDeviceDirectory(MessageType.REQUEST).reqId(2));

        assertEquals(2, result.reqId().get());
        assertFalse(result.isFieldPresent("ldName"));
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.ldName().get().isEmpty());
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("REQUEST with ldName only")
    void requestWithLdNameOnly() throws Exception {
        CmsGetLogicalDeviceDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
                        .ldName("IED1.AP1.LD2")
                        .reqId(3));

        assertEquals(3, result.reqId().get());
        assertEquals("IED1.AP1.LD2", result.ldName().get());
        assertTrue(result.isFieldPresent("ldName"));
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_POSITIVE).reqId(4);
        asdu.lnReference().add(new CmsSubReference("LN1"));
        asdu.lnReference().add(new CmsSubReference("LN2"));
        asdu.moreFollows().set(true);

        CmsGetLogicalDeviceDirectory result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(4, result.reqId().get());
        assertEquals(2, result.lnReference().size());
        assertEquals("LN1", result.lnReference().get(0).get());
        assertEquals("LN2", result.lnReference().get(1).get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty reference list")
    void positiveResponseEmpty() throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_POSITIVE).reqId(5);
        asdu.moreFollows().set(false);
        CmsGetLogicalDeviceDirectory result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(5, result.reqId().get());
        assertTrue(result.lnReference().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetLogicalDeviceDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(6));

        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetLogicalDeviceDirectory result = AsduTestUtil.roundTripViaAsdu(
                new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
                        .ldName("IED1.AP1.LD1")
                        .referenceAfter("IED1.AP1.LD1.LN1")
                        .reqId(10));

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.ldName().get());
        assertEquals("IED1.AP1.LD1.LN1", result.referenceAfter().get());
    }
}
