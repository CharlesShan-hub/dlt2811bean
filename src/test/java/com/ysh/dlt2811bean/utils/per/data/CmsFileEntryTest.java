package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsFileEntryTest {

    @Test
    void encodeDecode_basic() throws Exception {
        CmsFileEntry entry = new CmsFileEntry();
        entry.setFileName("report.txt")
             .setFileSize(2048)
             .setLastModified(new CmsUtcTime(1715000000L, 0, 0))
             .setCheckSum(0x12345678L);

        PerOutputStream pos = new PerOutputStream();
        CmsFileEntry.encode(pos, entry);
        CmsFileEntry r = CmsFileEntry.decode(new PerInputStream(pos.toByteArray()));

        assertEquals("report.txt", r.getFileName());
        assertEquals(2048, r.getFileSize());
        assertEquals(1715000000L, r.getLastModified().getSecondsSinceEpoch());
        assertEquals(0x12345678L, r.getCheckSum());
    }

    @Test
    void encodeDecode_directory() throws Exception {
        CmsFileEntry entry = new CmsFileEntry();
        entry.setFileName("docs/")
             .setFileSize(0)
             .setLastModified(new CmsUtcTime(0, 0, 0))
             .setCheckSum(0);

        PerOutputStream pos = new PerOutputStream();
        CmsFileEntry.encode(pos, entry);
        CmsFileEntry r = CmsFileEntry.decode(new PerInputStream(pos.toByteArray()));

        assertEquals("docs/", r.getFileName());
        assertEquals(0, r.getFileSize());
    }

    @Test
    void encodeDecode_maxFileSize() throws Exception {
        CmsFileEntry entry = new CmsFileEntry();
        entry.setFileName("big.bin")
             .setFileSize(0xFFFFFFFFL)
             .setLastModified(new CmsUtcTime(0xFFFFFFFFL, 0xFFFFFF, 0xFF))
             .setCheckSum(0xFFFFFFFFL);

        PerOutputStream pos = new PerOutputStream();
        CmsFileEntry.encode(pos, entry);
        CmsFileEntry r = CmsFileEntry.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(0xFFFFFFFFL, r.getFileSize());
        assertEquals(0xFFFFFFFFL, r.getCheckSum());
    }

    @Test
    void encode_nullFileNameBecomesEmpty() throws Exception {
        CmsFileEntry entry = new CmsFileEntry();
        entry.setFileName(null)
             .setFileSize(0)
             .setLastModified(new CmsUtcTime(0, 0, 0))
             .setCheckSum(0);

        PerOutputStream pos = new PerOutputStream();
        CmsFileEntry.encode(pos, entry);
        CmsFileEntry r = CmsFileEntry.decode(new PerInputStream(pos.toByteArray()));

        assertEquals("", r.getFileName());
    }
}
