package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSelectActiveSG")
class CmsSelectActiveSGTest implements ServiceNameTest<CmsSelectActiveSG>, CopyTest<CmsSelectActiveSG> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.SELECT_ACTIVE_SG;
    }

    @Override
    public CmsSelectActiveSG createAsdu() {
        return new CmsSelectActiveSG(MessageType.REQUEST);
    }

    @Override
    public CmsSelectActiveSG createCopyableAsdu() {
        return new CmsSelectActiveSG(MessageType.REQUEST)
                .sgcbReference("IED1.AP1.LD1.LN1.SGCB1").settingGroupNumber(3).reqId(10);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSelectActiveSG result = AsduTestUtil.roundTripViaApdu(
                new CmsSelectActiveSG(MessageType.REQUEST)
                        .sgcbReference("IED1.AP1.LD1.LN1.SGCB1").settingGroupNumber(3).reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.SGCB1", result.sgcbReference().get());
        assertEquals(3, result.settingGroupNumber().get());
    }

    @Test
    @DisplayName("REQUEST with default settingGroupNumber")
    void requestDefaultGroupNumber() throws Exception {
        CmsSelectActiveSG result = AsduTestUtil.roundTripViaApdu(
                new CmsSelectActiveSG(MessageType.REQUEST)
                        .sgcbReference("IED1.AP1.LD1.LN1.SGCB1").reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals(1, result.settingGroupNumber().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSelectActiveSG result = AsduTestUtil.roundTripViaApdu(
                new CmsSelectActiveSG(MessageType.RESPONSE_POSITIVE).reqId(3));
        assertEquals(3, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSelectActiveSG result = AsduTestUtil.roundTripViaApdu(
                new CmsSelectActiveSG(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(4));
        assertEquals(4, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }
}
