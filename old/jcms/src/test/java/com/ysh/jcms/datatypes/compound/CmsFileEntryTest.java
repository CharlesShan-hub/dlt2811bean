package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFileEntry")
class CmsFileEntryTest {

    @Test
    void roundtrip() {
        String fileName = "/cfg/config.dat";
        long fileSize = 1024;
        byte[] lastModified = {0, 0, 0, 0, 12, 30, 45, 0};
        long checkSum = 0xABCD1234L;

        CmsFileEntry original = new CmsFileEntry(fileName, fileSize, lastModified, checkSum);
        byte[] data = original.encode();
        CmsFileEntry decoded = CmsFileEntry.from(data);

        assertEquals(fileName, decoded.fileName);
        assertEquals(fileSize, decoded.fileSize);
        assertArrayEquals(lastModified, decoded.lastModified);
        assertEquals(checkSum, decoded.checkSum);
    }

    @Test
    void constructor() {
        CmsFileEntry entry = new CmsFileEntry("test.txt", 42, new byte[8], 0);
        assertEquals("test.txt", entry.fileName());
        assertEquals(42, entry.fileSize());
    }
}
