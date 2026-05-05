package com.ysh.dlt2811bean.service.svc.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsCreateDataSet")
class CmsCreateDataSetTest implements
        ServiceNameTest<CmsCreateDataSet>,
        CopyTest<CmsCreateDataSet>,
        FromFlagsTest<CmsCreateDataSet> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.CREATE_DATA_SET;
    }

    @Override
    public CmsCreateDataSet createAsdu() {
        return new CmsCreateDataSet(MessageType.REQUEST);
    }

    @Override
    public CmsCreateDataSet createCopyableAsdu() {
        return new CmsCreateDataSet(MessageType.REQUEST)
                .datasetReference("IED1.AP1.LD1.LN1.DS1")
                .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
                .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
                .reqId(10);
    }

    @Override
    public CmsCreateDataSet createFromFlags(boolean resp, boolean err) {
        return new CmsCreateDataSet(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsCreateDataSet result = AsduTestUtil.roundTripViaApdu(
                new CmsCreateDataSet(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .referenceAfter("IED1.AP1.LD1.LN1.DO1.DA1")
                        .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
                        .addMemberData("IED1.AP1.LD1.LN1.DO2", "MX")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1.DA1", result.referenceAfter().get());
        assertEquals(2, result.memberData().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.memberData().get(0).reference().get());
        assertEquals("ST", result.memberData().get(0).fc().get());
        assertEquals("IED1.AP1.LD1.LN1.DO2", result.memberData().get(1).reference().get());
        assertEquals("MX", result.memberData().get(1).fc().get());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsCreateDataSet result = AsduTestUtil.roundTripViaApdu(
                new CmsCreateDataSet(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
        assertEquals(1, result.memberData().size());
    }

    @Test
    @DisplayName("REQUEST with empty memberData")
    void requestEmptyMemberData() throws Exception {
        CmsCreateDataSet result = AsduTestUtil.roundTripViaApdu(
                new CmsCreateDataSet(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .reqId(3));

        assertEquals(3, result.reqId().get());
        assertTrue(result.memberData().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsCreateDataSet result = AsduTestUtil.roundTripViaApdu(
                new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE).reqId(4));

        assertEquals(4, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsCreateDataSet result = AsduTestUtil.roundTripViaApdu(
                new CmsCreateDataSet(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(5));

        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsCreateDataSet result = AsduTestUtil.roundTripViaAsdu(
                new CmsCreateDataSet(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
                        .reqId(10));

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
        assertEquals(1, result.memberData().size());
        assertEquals("ST", result.memberData().get(0).fc().get());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
                .datasetReference("IED1.AP1.LD1.LN1.DS1")
                .addMemberData("IED1.AP1.LD1.LN1.DO1", "ST")
                .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsCreateDataSet) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }
}
