package com.ysh.dlt2811bean.transport.app.file;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileDirectory;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetFileDirectory Loopback Test")
class GetFileDirectoryLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("List all files")
    void all() throws Exception {
        associate();

        CmsApdu response = client.getFileDirectory();

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetFileDirectory dir = (CmsGetFileDirectory) response.getAsdu();
        assertEquals(3, dir.fileEntry.size());
    }

    @Test
    @DisplayName("List files filtered by path /data")
    void filteredByPath() throws Exception {
        associate();

        CmsApdu response = client.getFileDirectory("/data");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetFileDirectory dir = (CmsGetFileDirectory) response.getAsdu();
        assertEquals(1, dir.fileEntry.size());
        assertEquals("/data/log.txt", dir.fileEntry.get(0).fileName.get());
    }

    @Test
    @DisplayName("List files after a reference")
    void afterReference() throws Exception {
        associate();

        CmsApdu response = client.getFileDirectory("/", "/README.txt");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetFileDirectory dir = (CmsGetFileDirectory) response.getAsdu();
        assertEquals(2, dir.fileEntry.size());
        assertEquals("/config.yaml", dir.fileEntry.get(0).fileName.get());
    }
}
