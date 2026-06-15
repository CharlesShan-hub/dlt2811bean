package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetGoCBValues")
class CmsGetGoCBValuesTest implements ServiceNameTest<CmsGetGoCBValues>, CopyTest<CmsGetGoCBValues> {

    @Override public ServiceName expectedServiceName() { return ServiceName.GET_GOCB_VALUES; }
    @Override public CmsGetGoCBValues createAsdu() { return new CmsGetGoCBValues(MessageType.REQUEST); }
    @Override public CmsGetGoCBValues createCopyableAsdu() {
        return new CmsGetGoCBValues(MessageType.REQUEST).addGocbReference("IED1.AP1.LD1.LN1.GOCB1").reqId(10);
    }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsGetGoCBValues asdu = new CmsGetGoCBValues(MessageType.REQUEST).reqId(1);
        asdu.addGocbReference("IED1.AP1.LD1.LN1.GOCB1");
        CmsGetGoCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(1, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetGoCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetGoCBValues(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetGoCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetGoCBValues(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
    }
}
