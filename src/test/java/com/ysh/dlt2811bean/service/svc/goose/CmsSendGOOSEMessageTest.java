package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CmsSendGooseMessageTest {

    @Test
    void indicationRoundTrip() throws Exception {
        CmsSendGooseMessage asdu = new CmsSendGooseMessage()
            .goID("GOCB1")
            .stNum(1L)
            .sqNum(10L)
            .simulation(false)
            .confRev(1L)
            .ndsCom(false)
            .data(new CmsInt32(42));

        PerOutputStream pos = new PerOutputStream();
        asdu.encode(pos);

        CmsSendGooseMessage result = new CmsSendGooseMessage().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("GOCB1", result.goID().get());
        assertEquals(1L, result.stNum().get());
        assertEquals(10L, result.sqNum().get());
    }

    @Test
    void serviceCode() {
        assertEquals(ServiceName.SEND_GOOSE_MESSAGE, new CmsSendGooseMessage().getServiceName());
    }
}
