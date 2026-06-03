package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFileEntry")
class CmsFileEntryTest {

    @Test
    @DisplayName("encode and decode roundtrip")
    void encodeDecode_roundtrip() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 1234567, 0x20L);
        CmsFileEntry entry = new CmsFileEntry("report.txt", 1024, utc, 0xAABBCCDDL);

        PerOutputStream pos = new PerOutputStream();
        entry.encode(pos);

        CmsFileEntry r = new CmsFileEntry().decode(new PerInputStream(pos.toByteArray()));

        assertEquals("report.txt", r.fileName.get());
        assertEquals(1024, r.fileSize.get());
        assertEquals(1715000000L, r.lastModified.secondsSinceEpoch.get());
        assertEquals(0xAABBCCDDL, r.checkSum.get());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 1234567, 0x20L);
        CmsFileEntry original = new CmsFileEntry("report.txt", 1024, utc, 0xAABBCCDDL);
        CmsFileEntry cloned = original.copy();
        assertEquals(original.fileName.get(), cloned.fileName.get());
        assertEquals(original.fileSize.get(), cloned.fileSize.get());
        assertEquals(original.lastModified.secondsSinceEpoch.get(), cloned.lastModified.secondsSinceEpoch.get());
        assertEquals(original.checkSum.get(), cloned.checkSum.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is deep")
    void copyIsDeep() {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 1234567, 0x20L);
        CmsFileEntry original = new CmsFileEntry("report.txt", 1024, utc, 0xAABBCCDDL);
        CmsFileEntry cloned = original.copy();
        cloned.fileName.set("modified.txt");
        assertEquals("report.txt", original.fileName.get());
    }

    @Test
    @DisplayName("chain setters via public fields")
    void setters_fields() {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 0, 0L);
        CmsFileEntry entry = new CmsFileEntry();
        entry.fileName.set("data.bin");
        entry.fileSize.set(2048L);
        entry.lastModified = utc;
        entry.checkSum.set(0x12345678L);
        assertEquals("data.bin", entry.fileName.get());
        assertEquals(2048, entry.fileSize.get());
        assertEquals(0x12345678L, entry.checkSum.get());
    }

    @Test
    @DisplayName("convenience setters with raw values")
    void setters_convenience() {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 0, 0L);
        CmsFileEntry entry = new CmsFileEntry()
            .fileName("data.bin")
            .fileSize(2048)
            .lastModified(utc)
            .checkSum(0x12345678L);
        assertEquals("data.bin", entry.fileName.get());
        assertEquals(2048, entry.fileSize.get());
        assertEquals(0x12345678L, entry.checkSum.get());
    }
}