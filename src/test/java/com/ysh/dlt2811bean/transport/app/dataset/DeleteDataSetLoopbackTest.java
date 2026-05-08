package com.ysh.dlt2811bean.transport.app.dataset;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DeleteDataSet Loopback Test")
class DeleteDataSetLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("create then delete dataset returns Response+")
    void createThenDelete() throws Exception {
        associate();

        client.createDataSet("C1/LLN0.TempDs", "C1/CSWI1.Pos", "ST");

        CmsApdu response = client.deleteDataSet("C1/LLN0.TempDs");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("unknown dataset returns Response-")
    void unknownDataSet() throws Exception {
        associate();

        CmsApdu response = client.deleteDataSet("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty reference returns Response-")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.deleteDataSet("");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
