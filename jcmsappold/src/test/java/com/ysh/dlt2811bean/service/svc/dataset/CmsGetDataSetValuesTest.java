package com.ysh.dlt2811bean.service.svc.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetDataSetValues")
class CmsGetDataSetValuesTest implements
        ServiceNameTest<CmsGetDataSetValues>,
        CopyTest<CmsGetDataSetValues>,
        FromFlagsTest<CmsGetDataSetValues> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_DATA_SET_VALUES;
    }

    @Override
    public CmsGetDataSetValues createAsdu() {
        return new CmsGetDataSetValues(MessageType.REQUEST);
    }

    @Override
    public CmsGetDataSetValues createCopyableAsdu() {
        return new CmsGetDataSetValues(MessageType.REQUEST)
                .datasetReference("IED1.AP1.LD1.LN1.DS1")
                .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
                .reqId(10);
    }

    @Override
    public CmsGetDataSetValues createFromFlags(boolean resp, boolean err) {
        return new CmsGetDataSetValues(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetDataSetValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataSetValues(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1.DA1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetDataSetValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataSetValues(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.RESPONSE_POSITIVE).reqId(3);
        asdu.value().add(new CmsInt32(100));
        asdu.value().add(new CmsInt32(200));
        asdu.moreFollows().set(true);

        CmsGetDataSetValues result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(3, result.reqId().get());
        assertEquals(2, result.value().size());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty value list")
    void positiveResponseEmpty() throws Exception {
        CmsGetDataSetValues asdu2 = new CmsGetDataSetValues(MessageType.RESPONSE_POSITIVE).reqId(4);
        asdu2.moreFollows().set(false);
        CmsGetDataSetValues result = AsduTestUtil.roundTripViaApdu(asdu2);

        assertEquals(4, result.reqId().get());
        assertTrue(result.value().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetDataSetValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataSetValues(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(5));

        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetDataSetValues result = AsduTestUtil.roundTripViaAsdu(
                new CmsGetDataSetValues(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .reqId(10));

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.REQUEST)
                .datasetReference("IED1.AP1.LD1.LN1.DS1")
                .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataSetValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.RESPONSE_NEGATIVE)
                .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataSetValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
