package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsErrorGocbChoice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsGetGoCBValuesTest {

    @Test
    void requestRoundTrip() throws Exception {
        CmsGetGoCBValues asdu = new CmsGetGoCBValues(MessageType.REQUEST)
            .reqId(1);
        asdu.addGocbReference("IED1.AP1.LD1.LN1.GOCB1");

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetGoCBValues result = (CmsGetGoCBValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(1, result.gocbReference().size());
    }

    @Test
    void positiveResponseRoundTrip() throws Exception {
        CmsGetGoCBValues asdu = new CmsGetGoCBValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsErrorGocbChoice errorChoice = new CmsErrorGocbChoice().selectError();
        errorChoice.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addErrorGocbChoice(errorChoice);

        CmsErrorGocbChoice gocbChoice = new CmsErrorGocbChoice().selectGocb();
        gocbChoice.gocb.goCBName.set("GOCB1");
        asdu.addErrorGocbChoice(gocbChoice);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetGoCBValues result = (CmsGetGoCBValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(2, result.errorGocb().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.errorGocb().get(0).error.get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    void negativeResponseRoundTrip() throws Exception {
        CmsGetGoCBValues asdu = new CmsGetGoCBValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetGoCBValues result = (CmsGetGoCBValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    void serviceCode() {
        assertEquals(ServiceName.GET_GOCBVALUES, new CmsGetGoCBValues(MessageType.REQUEST).getServiceName());
    }
}
