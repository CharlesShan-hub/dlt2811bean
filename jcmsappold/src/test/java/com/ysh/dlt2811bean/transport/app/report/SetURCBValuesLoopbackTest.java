package com.ysh.dlt2811bean.transport.app.report;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetURCBValues Loopback Test")
class SetURCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("empty sequence returns Response+ (§8.7.5.2c)")
    void emptySequence() throws Exception {
        associate();

        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.REQUEST);
        CmsApdu response = client.setURCBValues(asdu);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("unknown URCB reference returns Response- with error")
    void invalidRef() throws Exception {
        associate();

        CmsSetURCBValuesEntry entry = new CmsSetURCBValuesEntry();
        entry.reference.set("C1/LLN0.Unknown");

        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.REQUEST);
        asdu.urcb.add(entry);
        CmsApdu response = client.setURCBValues(asdu);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
