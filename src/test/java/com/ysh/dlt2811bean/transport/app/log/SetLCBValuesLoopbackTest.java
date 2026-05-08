package com.ysh.dlt2811bean.transport.app.log;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetLCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsSetLCBValuesEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetLCBValues Loopback Test")
class SetLCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("empty sequence returns Response+ (§8.8.3.2c)")
    void emptySequence() throws Exception {
        associate();

        CmsSetLCBValues asdu = new CmsSetLCBValues(MessageType.REQUEST);
        CmsApdu response = client.setLCBValues(asdu);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("valid LCB reference with no optional fields returns Response+")
    void validRef() throws Exception {
        associate();

        CmsSetLCBValuesEntry entry = new CmsSetLCBValuesEntry();
        entry.reference.set("C1/LLN0.Log");

        CmsSetLCBValues asdu = new CmsSetLCBValues(MessageType.REQUEST);
        asdu.addLcb(entry);
        CmsApdu response = client.setLCBValues(asdu);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("invalid reference returns Response- with error")
    void invalidRef() throws Exception {
        associate();

        CmsSetLCBValuesEntry entry = new CmsSetLCBValuesEntry();
        entry.reference.set("C1/LLN0.Unknown");

        CmsSetLCBValues asdu = new CmsSetLCBValues(MessageType.REQUEST);
        asdu.addLcb(entry);
        CmsApdu response = client.setLCBValues(asdu);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
