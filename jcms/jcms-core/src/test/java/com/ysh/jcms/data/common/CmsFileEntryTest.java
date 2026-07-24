package com.ysh.jcms.data.common;

import com.ysh.jcms.data.time.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsFileEntryTest {
    @Test
    public void roundup() {
        CmsFileEntry a = new CmsFileEntry().fileName("test.txt").fileSize(1024L)
                .lastModified(new CmsUtcTime().secondsSinceEpoch(1234567890L)).checkSum(0xDEADBEEFL);
        byte[] encoded = a.encode();
        CmsFileEntry b = new CmsFileEntry();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
