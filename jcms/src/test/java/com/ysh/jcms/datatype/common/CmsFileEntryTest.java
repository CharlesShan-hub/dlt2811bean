package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFileEntry")
class CmsFileEntryTest {

    private CmsFileEntry get() { return (CmsFileEntry)(new CmsFileEntry().test()); }

    @Test
    void roundtrip() {
        CmsFileEntry original = get();
        original.fileName().value("ConfigFile.cfg");
        original.fileSize().value(1024);
        original.checkSum().value(0x12345678);
        original.lastModified().set(1718015445500L);

        CmsFileEntry decoded = get().decode(original.encode());
        assertEquals("ConfigFile.cfg", new String(decoded.fileName().value()).trim());
        assertEquals(1024, decoded.fileSize().value());
        assertEquals(0x12345678, decoded.checkSum().value());
        assertEquals(original.lastModified().seconds_since_epoch().value(),
                     decoded.lastModified().seconds_since_epoch().value());
    }

    @Test
    void directoryNameEndsWithSlash() {
        CmsFileEntry original = get();
        original.fileName().value("backup/");
        original.lastModified().now();

        CmsFileEntry decoded = get().decode(original.encode());
        assertEquals("backup/", new String(decoded.fileName().value()).trim());
    }

    @Test
    void fileNameMaxLength() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 129; i++) sb.append('a');
        String longName = sb.toString();

        CmsFileEntry original = get();
        original.fileName().value(longName);
        original.fileSize().value(99999);
        original.checkSum().value(0xDEADBEEF);
        original.lastModified().set(0);

        CmsFileEntry decoded = get().decode(original.encode());
        assertEquals(longName, new String(decoded.fileName().value()).trim());
        assertEquals(99999, decoded.fileSize().value());
        assertEquals(0xDEADBEEF, decoded.checkSum().value());
    }

    @Test
    void emptyFileName() {
        CmsFileEntry original = get();
        original.fileName().value("");
        original.fileSize().value(0);
        original.lastModified().now();

        CmsFileEntry decoded = get().decode(original.encode());
        assertEquals("", new String(decoded.fileName().value()).trim());
    }

    @Test
    void lastModifiedPreserved() {
        CmsFileEntry original = get();
        original.fileName().value("report.log");
        original.fileSize().value(42);
        original.lastModified().set(1718015445500L);

        CmsFileEntry decoded = get().decode(original.encode());
        assertEquals(original.lastModified().seconds_since_epoch().value(),
                     decoded.lastModified().seconds_since_epoch().value());
        assertTrue(decoded.lastModified().time_quality().leap_seconds_known().value());
    }

    @Test
    void lastModifiedNow() {
        CmsFileEntry original = get();
        original.fileName().value("now.bin");
        original.fileSize().value(1);
        original.lastModified().now();

        CmsFileEntry decoded = get().decode(original.encode());
        assertEquals(original.lastModified().seconds_since_epoch().value(),
                     decoded.lastModified().seconds_since_epoch().value());
    }

    @Test
    void allFieldsDefault() {
        CmsFileEntry original = get();
        original.fileName().value("default");
        CmsFileEntry decoded = get().decode(original.encode());
        assertEquals(0, decoded.fileSize().value());
        assertEquals(0, decoded.checkSum().value());
    }
}
