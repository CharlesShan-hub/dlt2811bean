package com.ysh.jcms.data.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsFileEntryTest {
    @Test
    public void roundtrip() {
        CmsFileEntry a = new CmsFileEntry();
        a.fileName.value("test.txt".getBytes());
        a.fileSize.value(1024L);
        a.lastModified.seconds_since_epoch.value(1234567890L);
        a.checkSum.value(0xDEADBEEFL);
        byte[] encoded = a.encode();
        CmsFileEntry b = new CmsFileEntry();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
