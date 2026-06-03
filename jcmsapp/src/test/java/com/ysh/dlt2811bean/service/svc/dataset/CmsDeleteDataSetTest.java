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

@DisplayName("CmsDeleteDataSet")
class CmsDeleteDataSetTest implements
        ServiceNameTest<CmsDeleteDataSet>,
        CopyTest<CmsDeleteDataSet>,
        FromFlagsTest<CmsDeleteDataSet> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.DELETE_DATA_SET;
    }

    @Override
    public CmsDeleteDataSet createAsdu() {
        return new CmsDeleteDataSet(MessageType.REQUEST);
    }

    @Override
    public CmsDeleteDataSet createCopyableAsdu() {
        return new CmsDeleteDataSet(MessageType.REQUEST)
                .datasetReference("IED1.AP1.LD1.LN1.DS1")
                .reqId(10);
    }

    @Override
    public CmsDeleteDataSet createFromFlags(boolean resp, boolean err) {
        return new CmsDeleteDataSet(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsDeleteDataSet result = AsduTestUtil.roundTripViaApdu(
                new CmsDeleteDataSet(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsDeleteDataSet result = AsduTestUtil.roundTripViaApdu(
                new CmsDeleteDataSet(MessageType.RESPONSE_POSITIVE).reqId(2));

        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsDeleteDataSet result = AsduTestUtil.roundTripViaApdu(
                new CmsDeleteDataSet(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(3));

        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsDeleteDataSet result = AsduTestUtil.roundTripViaAsdu(
                new CmsDeleteDataSet(MessageType.REQUEST)
                        .datasetReference("IED1.AP1.LD1.LN1.DS1")
                        .reqId(10));

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datasetReference().get());
    }
}
