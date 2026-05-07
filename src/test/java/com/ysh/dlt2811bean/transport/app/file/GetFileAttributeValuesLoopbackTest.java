package com.ysh.dlt2811bean.transport.app.file;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileAttributeValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetFileAttributeValues Loopback Test")
class GetFileAttributeValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Get attributes for existing builtin file")
    void existingFile() throws Exception {
        associate();

        CmsApdu response = client.getFileAttributeValues("/README.txt");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetFileAttributeValues attr = (CmsGetFileAttributeValues) response.getAsdu();
        assertEquals("/README.txt", attr.fileEntry.fileName.get());
        assertTrue(attr.fileEntry.fileSize.get() > 0);
    }

    @Test
    @DisplayName("Get attributes for unknown file returns negative")
    void unknownFile() throws Exception {
        associate();

        CmsApdu response = client.getFileAttributeValues("/nonexistent.txt");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
