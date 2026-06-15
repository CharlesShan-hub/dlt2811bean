package com.ysh.dlt2811bean.transport.app.file;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetFile Loopback Test")
class SetFileLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("SetFile write a single chunk")
    void writeSingleChunk() throws Exception {
        associate();

        CmsApdu response = client.setFile("/upload.txt", 1, "hello".getBytes(), true);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("SetFile cancel write with startPosition=0")
    void cancelWrite() throws Exception {
        associate();

        CmsApdu response = client.setFile("/upload.txt", 0, new byte[0], false);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("SetFile empty filename returns negative")
    void emptyFilename() throws Exception {
        associate();

        CmsApdu response = client.setFile("", 1, "data".getBytes(), true);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
