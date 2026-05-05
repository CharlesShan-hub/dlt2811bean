package com.ysh.dlt2811bean.service.svc.setting;

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

@DisplayName("CmsSetEditSGValue")
class CmsSetEditSGValueTest implements ServiceNameTest<CmsSetEditSGValue>, CopyTest<CmsSetEditSGValue> {

    @Override public ServiceName expectedServiceName() { return ServiceName.SET_EDIT_SG_VALUE; }
    @Override public CmsSetEditSGValue createAsdu() { return new CmsSetEditSGValue(MessageType.REQUEST); }
    @Override public CmsSetEditSGValue createCopyableAsdu() {
        return new CmsSetEditSGValue(MessageType.REQUEST)
                .addData("IED1.AP1.LD1.LN1.DO1", new CmsInt32(42)).reqId(10);
    }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsSetEditSGValue asdu = new CmsSetEditSGValue(MessageType.REQUEST).reqId(1);
        asdu.addData("IED1.AP1.LD1.LN1.DO1", new CmsInt32(100));
        CmsSetEditSGValue result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(1, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetEditSGValue result = AsduTestUtil.roundTripViaApdu(
                new CmsSetEditSGValue(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetEditSGValue asdu = new CmsSetEditSGValue(MessageType.RESPONSE_NEGATIVE).reqId(3);
        asdu.addResult(CmsServiceError.ACCESS_VIOLATION);
        CmsSetEditSGValue result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(3, result.reqId().get());
    }
}
