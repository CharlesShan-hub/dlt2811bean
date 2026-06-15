package com.ysh.dlt2811bean.service.svc.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSetDataSetValues")
class CmsSetDataSetValuesTest implements
        ServiceNameTest<CmsSetDataSetValues>,
        CopyTest<CmsSetDataSetValues> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.SET_DATA_SET_VALUES;
    }

    @Override
    public CmsSetDataSetValues createAsdu() {
        return new CmsSetDataSetValues(MessageType.REQUEST);
    }

    @Override
    public CmsSetDataSetValues createCopyableAsdu() {
        return new CmsSetDataSetValues(MessageType.REQUEST)
                .datasetReference("IED1.AP1.LD1.LN1.DS1")
                .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
                .addMemberValue(new CmsInt32(42))
                .reqId(10);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSetDataSetValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetDataSetValues(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
                        .addMemberValue(new CmsInt32(100))
                        .addMemberValue(new CmsInt32(200))
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1.DA1", result.referenceAfter().get());
        assertEquals(2, result.memberValue().size());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsSetDataSetValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetDataSetValues(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .addMemberValue(new CmsInt32(42))
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
        assertEquals(1, result.memberValue().size());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetDataSetValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetDataSetValues(MessageType.RESPONSE_POSITIVE).reqId(3));

        assertEquals(3, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE).reqId(4);
        asdu.addResult(CmsServiceError.ACCESS_VIOLATION);
        CmsSetDataSetValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(4, result.reqId().get());
    }
}
