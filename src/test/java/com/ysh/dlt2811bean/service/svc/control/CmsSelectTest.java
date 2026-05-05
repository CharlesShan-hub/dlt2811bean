package com.ysh.dlt2811bean.service.svc.control;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSelect")
class CmsSelectTest implements ServiceNameTest<CmsSelect>, FromFlagsTest<CmsSelect> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.SELECT;
    }

    @Override
    public CmsSelect createAsdu() {
        return new CmsSelect(MessageType.REQUEST);
    }

    @Override
    public CmsSelect createFromFlags(boolean resp, boolean err) {
        return new CmsSelect(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSelect result = AsduTestUtil.roundTripViaApdu(
                new CmsSelect(MessageType.REQUEST)
                        .reference("IED1.AP1.LD1.LN1.DO1")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.reference().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSelect result = AsduTestUtil.roundTripViaApdu(
                new CmsSelect(MessageType.RESPONSE_POSITIVE)
                        .reference("IED1.AP1.LD1.LN1.DO1")
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.reference().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSelect result = AsduTestUtil.roundTripViaApdu(
                new CmsSelect(MessageType.RESPONSE_NEGATIVE)
                        .reference("IED1.AP1.LD1.LN1.DO1")
                        .reqId(3));

        assertEquals(3, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.reference().get());
    }
}
