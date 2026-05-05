package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesEntry;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsSetGoCBValuesTest implements ServiceNameTest<CmsSetGoCBValues> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.SET_GOCBVALUES;
    }

    @Override
    public CmsSetGoCBValues createAsdu() {
        return new CmsSetGoCBValues(MessageType.REQUEST);
    }

    @Test
    void requestRoundTrip() throws Exception {
        CmsSetGoCBValues asdu = new CmsSetGoCBValues(MessageType.REQUEST).reqId(1);
        CmsSetGoCBValuesEntry entry = new CmsSetGoCBValuesEntry();
        entry.reference.set("IED1.AP1.LD1.LN1.GOCB1");
        entry.goEna.set(true);
        asdu.addGocb(entry);

        CmsSetGoCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(1, result.reqId().get());
        assertEquals(1, result.gocb().size());
        assertEquals("IED1.AP1.LD1.LN1.GOCB1", result.gocb().get(0).reference().get());
        assertTrue(result.gocb().get(0).isFieldPresent("goEna"));
    }

    @Test
    void positiveResponseRoundTrip() throws Exception {
        CmsSetGoCBValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetGoCBValues(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test
    void negativeResponseRoundTrip() throws Exception {
        CmsSetGoCBValues asdu = new CmsSetGoCBValues(MessageType.RESPONSE_NEGATIVE).reqId(3);
        com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesResultEntry entry =
                new com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesResultEntry();
        entry.error.set(com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError.ACCESS_VIOLATION);
        asdu.addResult(entry);

        CmsSetGoCBValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(3, result.reqId().get());
        assertEquals(1, result.result().size());
    }
}
