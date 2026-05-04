package com.ysh.dlt2811bean.service.svc.control;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CmsCommandTerminationTest {
    @Test
    void requestRoundTrip() throws Exception {
        CmsCommandTermination asdu = new CmsCommandTermination(MessageType.REQUEST)
            .reference("IED1.AP1.LD1.LN1.DO1")
            .ctlVal(new CmsInt32(100))
            .ctlNum(1)
            .test(false)
            .reqId(1);
        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));
        CmsCommandTermination result = (CmsCommandTermination) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.reference().get());
    }

    @Test
    void serviceCode() {
        assertEquals(ServiceName.COMMAND_TERMINATION, new CmsCommandTermination(MessageType.REQUEST).getServiceName());
    }
}
