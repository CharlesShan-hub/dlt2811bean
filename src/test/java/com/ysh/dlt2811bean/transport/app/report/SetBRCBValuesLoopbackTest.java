package com.ysh.dlt2811bean.transport.app.report;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetBRCBValuesEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetBRCBValues Loopback Test")
class SetBRCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("empty sequence returns Response+ (§8.7.3.2c)")
    void emptySequence() throws Exception {
        associate();

        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.REQUEST);
        CmsApdu response = client.setBRCBValues(asdu);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("unknown BRCB reference returns Response- with error")
    void invalidRef() throws Exception {
        associate();

        CmsSetBRCBValuesEntry entry = new CmsSetBRCBValuesEntry();
        entry.reference.set("C1/LLN0.Unknown");

        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.REQUEST);
        asdu.addBrcb(entry);
        CmsApdu response = client.setBRCBValues(asdu);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
