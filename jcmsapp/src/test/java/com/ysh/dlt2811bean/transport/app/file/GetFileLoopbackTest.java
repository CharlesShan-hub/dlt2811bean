package com.ysh.dlt2811bean.transport.app.file;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFile;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetFile Loopback Test")
class GetFileLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("GetFile read start of /README.txt")
    void readReadme() throws Exception {
        associate();

        CmsApdu response = client.getFile("/README.txt", 1);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetFile file = (CmsGetFile) response.getAsdu();
        assertNotNull(file.fileData.get());
        assertTrue(file.fileData.get().length > 0);
    }

    @Test
    @DisplayName("GetFile unknown file returns negative")
    void unknownFile() throws Exception {
        associate();

        CmsApdu response = client.getFile("/nonexistent.txt", 1);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }

    @Test
    @DisplayName("GetFile startPosition 0 cancels")
    void cancelRead() throws Exception {
        associate();

        CmsApdu response = client.getFile("/README.txt", 0);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
