package com.ysh.dlt2811bean.service.svc.control;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsSelectWithValueTest implements ServiceNameTest<CmsSelectWithValue> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.SELECT_WITH_VALUE;
    }

    @Override
    public CmsSelectWithValue createAsdu() {
        return new CmsSelectWithValue(MessageType.REQUEST);
    }

    @Test
    void requestRoundTrip() throws Exception {
        CmsSelectWithValue result = AsduTestUtil.roundTripViaApdu(
                new CmsSelectWithValue(MessageType.REQUEST)
                        .reference("IED1.AP1.LD1.LN1.DO1")
                        .ctlVal(new CmsInt32(100))
                        .ctlNum(1)
                        .test(false)
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.reference().get());
    }
}
