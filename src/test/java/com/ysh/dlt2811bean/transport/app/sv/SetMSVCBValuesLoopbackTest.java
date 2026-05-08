package com.ysh.dlt2811bean.transport.app.sv;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetMSVCBValues Loopback Test")
class SetMSVCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("SetMSVCBValues with valid entry returns OK")
    void setValid() throws Exception {
        associate();

        CmsSetMSVCBValuesEntry entry = new CmsSetMSVCBValuesEntry();
        entry.reference.set("C1/LLN0.Volt");
        entry.svEna.set(true);

        CmsApdu response = client.setMSVCBValues(entry);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("SetMSVCBValues with empty entries returns failure")
    void setEmpty() throws Exception {
        associate();

        CmsApdu response = client.setMSVCBValues();

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
