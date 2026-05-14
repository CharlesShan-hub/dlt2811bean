package com.ysh.dlt2811bean.transport.app.goose;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetGoCBValues Loopback Test")
class SetGoCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("set valid GoCB returns Response+")
    void validRef() throws Exception {
        associate();

        CmsSetGoCBValuesEntry entry = new CmsSetGoCBValuesEntry();
        entry.reference.set("C1/LLN0.ItlPositions");

        CmsApdu response = client.setGoCBValues(entry);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("set with empty list returns Response-")
    void emptyList() throws Exception {
        associate();

        CmsApdu response = client.setGoCBValues();

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
