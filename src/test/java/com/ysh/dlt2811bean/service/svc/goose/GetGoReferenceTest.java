package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GetGoReferenceTest {

    @Test
    void requestRoundTrip() throws Exception {
        CmsGetGoReference asdu = new CmsGetGoReference(MessageType.REQUEST)
            .gocbReference("IED1.AP1.LD1.LN1.GOCB1")
            .addMemberOffset(1)
            .addMemberOffset(2)
            .reqId(1);

        PerOutputStream pos = new PerOutputStream();
        CmsGetGoReference.write(pos, asdu);

        CmsGetGoReference result = CmsGetGoReference.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);  
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.GOCB1", result.gocbReference().get());
        assertEquals(2, result.memberOffset().size());
    }

    @Test
    void positiveResponseRoundTrip() throws Exception {
        CmsGetGoReference asdu = new CmsGetGoReference(MessageType.RESPONSE_POSITIVE)
            .gocbRefResp("IED1.AP1.LD1.LN1.GOCB1")
            .confRev(1L)
            .datSet("DS1")
            .addMemberData("DO1", "ST")
            .addMemberData("DO2", "MX")
            .reqId(2);

        PerOutputStream pos = new PerOutputStream();
        CmsGetGoReference.write(pos, asdu);

        CmsGetGoReference result = CmsGetGoReference.read(new PerInputStream(pos.toByteArray()), MessageType.RESPONSE_POSITIVE);
        assertEquals(2, result.reqId().get());
        assertEquals(2, result.memberData().size());
        assertEquals("DO1", result.memberData().get(0).reference().get());
        assertEquals("ST", result.memberData().get(0).fc().get());
    }

    @Test
    void negativeResponseRoundTrip() throws Exception {
        CmsGetGoReference asdu = new CmsGetGoReference(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        PerOutputStream pos = new PerOutputStream();
        CmsGetGoReference.write(pos, asdu);

        CmsGetGoReference result = CmsGetGoReference.read(new PerInputStream(pos.toByteArray()), MessageType.RESPONSE_NEGATIVE);
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    void serviceCode() {
        assertEquals(ServiceName.Get_Go_Reference, new CmsGetGoReference(MessageType.REQUEST).getServiceName());
    }
}
