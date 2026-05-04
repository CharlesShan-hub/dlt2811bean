package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesResultEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsSetGoCBValuesTest {

    @Test
    void requestRoundTrip() throws Exception {
        CmsSetGoCBValues asdu = new CmsSetGoCBValues(MessageType.REQUEST)
            .reqId(1);

        CmsSetGoCBValuesEntry entry = new CmsSetGoCBValuesEntry();
        entry.reference.set("IED1.AP1.LD1.LN1.GOCB1");
        entry.goEna.set(true);
        asdu.addGocb(entry);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetGoCBValues result = (CmsSetGoCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(1, result.gocb().size());
        assertEquals("IED1.AP1.LD1.LN1.GOCB1", result.gocb().get(0).reference().get());
        assertTrue(result.gocb().get(0).isFieldPresent("goEna"));
    }

    @Test
    void positiveResponseRoundTrip() throws Exception {
        CmsSetGoCBValues asdu = new CmsSetGoCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetGoCBValues result = (CmsSetGoCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    void negativeResponseRoundTrip() throws Exception {
        CmsSetGoCBValues asdu = new CmsSetGoCBValues(MessageType.RESPONSE_NEGATIVE)
            .reqId(3);

        CmsSetGoCBValuesResultEntry entry = new CmsSetGoCBValuesResultEntry();
        entry.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addResult(entry);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSetGoCBValues result = (CmsSetGoCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(1, result.result().size());
    }

    @Test
    void serviceCode() {
        assertEquals(ServiceName.SET_GOCBVALUES, new CmsSetGoCBValues(MessageType.REQUEST).getServiceName());
    }
}
