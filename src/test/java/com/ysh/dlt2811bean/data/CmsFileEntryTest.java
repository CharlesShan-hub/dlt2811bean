package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.compound.CmsFileEntry;
import com.ysh.dlt2811bean.data.compound.CmsUtcTime;
import com.ysh.dlt2811bean.data.numeric.CmsInt32U;
import com.ysh.dlt2811bean.data.string.CmsVisibleString;
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
    @DisplayName("chain setters via Lombok fluent setters")
    void setters_fluent() {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 0, 0L);
        CmsFileEntry entry = new CmsFileEntry()
            .fileName(new CmsVisibleString("data.bin").max(129))
            .fileSize(new CmsInt32U(2048))
            .lastModified(utc)
            .checkSum(new CmsInt32U(0x12345678L));
        assertEquals("data.bin", entry.fileName.get());
        assertEquals(2048, entry.fileSize.get());
        assertEquals(0x12345678L, entry.checkSum.get());
    }
}