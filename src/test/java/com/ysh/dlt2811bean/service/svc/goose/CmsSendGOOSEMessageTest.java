package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CmsSendGOOSEMessageTest {

    @Test
    void indicationRoundTrip() throws Exception {
        CmsSendGOOSEMessage asdu = new CmsSendGOOSEMessage()
            .goID("GOCB1")
            .stNum(1L)
            .sqNum(10L)
            .simulation(false)
            .confRev(1L)
            .ndsCom(false)
            .data(new CmsInt32(42));

        PerOutputStream pos = new PerOutputStream();
        CmsSendGOOSEMessage.write(pos, asdu);

        CmsSendGOOSEMessage result = CmsSendGOOSEMessage.read(new PerInputStream(pos.toByteArray()), MessageType.INDICATION);
        assertEquals("GOCB1", result.goID().get());
        assertEquals(1L, result.stNum().get());
        assertEquals(10L, result.sqNum().get());
    }

    @Test
    void serviceCode() {
        assertEquals(ServiceName.Send_GOOSE_Message, new CmsSendGOOSEMessage().getServiceName());
    }
}
