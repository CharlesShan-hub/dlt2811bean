package com.ysh.dlt2811bean.service.svc.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetDataSetDirectory")
class CmsGetDataSetDirectoryTest implements
        ServiceNameTest<CmsGetDataSetDirectory>,
        FromFlagsTest<CmsGetDataSetDirectory> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_DATA_SET_DIRECTORY;
    }

    @Override
    public CmsGetDataSetDirectory createAsdu() {
        return new CmsGetDataSetDirectory(MessageType.REQUEST);
    }

    @Override
    public CmsGetDataSetDirectory createFromFlags(boolean resp, boolean err) {
        return new CmsGetDataSetDirectory(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetDataSetDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataSetDirectory(MessageType.REQUEST)
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
        CmsGetDataSetDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataSetDirectory(MessageType.REQUEST)
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
        CmsGetDataSetDirectory asdu = new CmsGetDataSetDirectory(MessageType.RESPONSE_POSITIVE).reqId(3);
        asdu.addMemberData("IED1.AP1.LD1.LN1.DO1", "ST");
        asdu.addMemberData("IED1.AP1.LD1.LN1.DO2", "MX");
        asdu.moreFollows().set(true);

        CmsGetDataSetDirectory result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(3, result.reqId().get());
        assertEquals(2, result.memberData().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.memberData().get(0).reference().get());
        assertEquals("ST", result.memberData().get(0).fc().get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetDataSetDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataSetDirectory(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(4));

        assertEquals(4, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }
}
