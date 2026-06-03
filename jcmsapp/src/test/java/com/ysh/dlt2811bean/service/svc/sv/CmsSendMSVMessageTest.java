package com.ysh.dlt2811bean.service.svc.sv;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CmsSendMSVMessageTest {

    @Test
    void indicationRoundTrip() throws Exception {
        CmsSendMSVMessage asdu = new CmsSendMSVMessage()
            .msvID("MSVCB1")
            .smpCnt(42)
            .confRev(1L)
            .smpSynch(0)
            .simulation(false)
            .sample(new CmsInt32(100));

        PerOutputStream pos = new PerOutputStream();
        asdu.encode(pos);

        CmsSendMSVMessage result = new CmsSendMSVMessage().decode(new PerInputStream(pos.toByteArray()));
        
        assertEquals("MSVCB1", result.msvID().get());
        assertEquals(42, result.smpCnt().get());
        assertEquals(1L, result.confRev().get());
    }

    @Test
    void serviceCode() {
        assertEquals(ServiceName.SEND_MSV_MESSAGE, new CmsSendMSVMessage().getServiceName());
    }
}
