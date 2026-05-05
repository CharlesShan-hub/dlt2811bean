package com.ysh.dlt2811bean.service.svc.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOperate")
class CmsOperateTest implements ServiceNameTest<CmsOperate> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.OPERATE;
    }

    @Override
    public CmsOperate createAsdu() {
        return new CmsOperate(MessageType.REQUEST);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsOperate result = AsduTestUtil.roundTripViaApdu(
                new CmsOperate(MessageType.REQUEST)
                        .reference("IED1.AP1.LD1.LN1.DO1")
                        .ctlVal(new CmsInt32(100))
                        .ctlNum(1)
                        .test(false)
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.reference().get());
        assertEquals(100, ((CmsInt32) ((com.ysh.dlt2811bean.datatypes.data.CmsData) result.ctlVal()).get()).get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsOperate result = AsduTestUtil.roundTripViaApdu(
                new CmsOperate(MessageType.RESPONSE_NEGATIVE)
                        .reference("IED1.AP1.LD1.LN1.DO1")
                        .ctlVal(new CmsInt32(100))
                        .ctlNum(1)
                        .test(false)
                        .addCause(CmsAddCause.OBJECT_NOT_SELECTED)
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals(CmsAddCause.OBJECT_NOT_SELECTED, result.addCause().get());
    }
}
