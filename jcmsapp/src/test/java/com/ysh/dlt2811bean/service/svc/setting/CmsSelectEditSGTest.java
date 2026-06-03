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

@DisplayName("CmsSelectEditSG")
class CmsSelectEditSGTest implements ServiceNameTest<CmsSelectEditSG>, CopyTest<CmsSelectEditSG> {

    @Override public ServiceName expectedServiceName() { return ServiceName.SELECT_EDIT_SG; }
    @Override public CmsSelectEditSG createAsdu() { return new CmsSelectEditSG(MessageType.REQUEST); }
    @Override public CmsSelectEditSG createCopyableAsdu() {
        return new CmsSelectEditSG(MessageType.REQUEST)
                .sgcbReference("IED1.AP1.LD1.LN1.SGCB1").settingGroupNumber(3).reqId(10);
    }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsSelectEditSG result = AsduTestUtil.roundTripViaApdu(
                new CmsSelectEditSG(MessageType.REQUEST)
                        .sgcbReference("IED1.AP1.LD1.LN1.SGCB1").settingGroupNumber(3).reqId(1));
        assertEquals(1, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsSelectEditSG result = AsduTestUtil.roundTripViaApdu(
                new CmsSelectEditSG(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsSelectEditSG result = AsduTestUtil.roundTripViaApdu(
                new CmsSelectEditSG(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
    }
}
