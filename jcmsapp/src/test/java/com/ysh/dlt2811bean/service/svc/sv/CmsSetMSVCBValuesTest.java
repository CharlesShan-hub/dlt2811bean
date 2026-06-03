package com.ysh.dlt2811bean.service.svc.sv;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesResultEntry;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSetMSVCBValues")
class CmsSetMSVCBValuesTest implements ServiceNameTest<CmsSetMSVCBValues> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.SET_MSVCB_VALUES;
    }

    @Override
    public CmsSetMSVCBValues createAsdu() {
        return new CmsSetMSVCBValues(MessageType.REQUEST);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSetMSVCBValues asdu = new CmsSetMSVCBValues(MessageType.REQUEST).reqId(1);
        CmsSetMSVCBValuesEntry entry = new CmsSetMSVCBValuesEntry();
        entry.reference.set("IED1.AP1.LD1.LN1.MSVCB1");
        entry.svEna.set(true);
        asdu.addMsvcb(entry);

        CmsSetMSVCBValues result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(1, result.reqId().get());
        assertEquals(1, result.msvcb().size());
        assertEquals("IED1.AP1.LD1.LN1.MSVCB1", result.msvcb().get(0).reference().get());
        assertTrue(result.msvcb().get(0).isFieldPresent("svEna"));
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetMSVCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetMSVCBValues(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetMSVCBValues asdu = new CmsSetMSVCBValues(MessageType.RESPONSE_NEGATIVE).reqId(3);
        CmsSetMSVCBValuesResultEntry entry = new CmsSetMSVCBValuesResultEntry();
        entry.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addResult(entry);

        CmsSetMSVCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(3, result.reqId().get());
        assertEquals(1, result.result().size());
    }
}
