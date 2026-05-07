package com.ysh.dlt2811bean.transport.app.file;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DeleteFile Loopback Test")
class DeleteFileLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("DeleteFile empty filename returns negative")
    void emptyFilename() throws Exception {
        associate();

        CmsApdu response = client.deleteFile("");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }

    @Test
    @DisplayName("DeleteFile non-existent file returns negative")
    void nonExistent() throws Exception {
        associate();

        CmsApdu response = client.deleteFile("/nonexistent.tmp");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
