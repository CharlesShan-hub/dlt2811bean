package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDirectoryEntry;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetDataDirectory")
class CmsGetDataDirectoryTest implements
        ServiceNameTest<CmsGetDataDirectory>,
        CopyTest<CmsGetDataDirectory>,
        FromFlagsTest<CmsGetDataDirectory> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_DATA_DIRECTORY;
    }

    @Override
    public CmsGetDataDirectory createAsdu() {
        return new CmsGetDataDirectory(MessageType.REQUEST);
    }

    @Override
    public CmsGetDataDirectory createCopyableAsdu() {
        return new CmsGetDataDirectory(MessageType.REQUEST)
                .dataReference("IED1.AP1.LD1.LN1.DO1")
                .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA2")
                .reqId(10);
    }

    @Override
    public CmsGetDataDirectory createFromFlags(boolean resp, boolean err) {
        return new CmsGetDataDirectory(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetDataDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataDirectory(MessageType.REQUEST)
                        .dataReference("IED1.AP1.LD1.LN1.DO1")
                        .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA2")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.dataReference().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1.DA2", result.referenceAfter().get());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetDataDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataDirectory(MessageType.REQUEST)
                        .dataReference("IED1.AP1.LD1.LN1.DO1")
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.dataReference().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.RESPONSE_POSITIVE).reqId(3);
        asdu.dataAttribute().add(new CmsGetDataDirectoryEntry()
                .reference("DO1.DA1").fc("ST"));
        asdu.dataAttribute().add(new CmsGetDataDirectoryEntry()
                .reference("DO1.DA2").fc("MX"));
        asdu.moreFollows().set(true);

        CmsGetDataDirectory result = AsduTestUtil.roundTripViaApdu(asdu);

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
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.RESPONSE_POSITIVE).reqId(4);
        asdu.moreFollows().set(false);
        CmsGetDataDirectory result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(4, result.reqId().get());
        assertTrue(result.dataAttribute().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetDataDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataDirectory(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(5));

        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetDataDirectory result = AsduTestUtil.roundTripViaAsdu(
                new CmsGetDataDirectory(MessageType.REQUEST)
                        .dataReference("IED1.AP1.LD1.LN1.DO1")
                        .reqId(10));

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.dataReference().get());
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
}
