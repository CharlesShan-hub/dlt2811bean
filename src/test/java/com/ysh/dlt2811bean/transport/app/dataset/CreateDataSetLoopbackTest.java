package com.ysh.dlt2811bean.transport.app.dataset;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreateDataSet Loopback Test")
class CreateDataSetLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("create new data set returns Response+")
    void createDataSet() throws Exception {
        associate();

        CmsApdu response = client.createDataSet(
                "C1/LLN0.TestDs",
                "C1/CSWI1.Pos", "ST");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty reference returns Response-")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.createDataSet(
                "",
                "C1/CSWI1.Pos", "ST");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
