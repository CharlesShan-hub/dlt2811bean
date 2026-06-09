package com.ysh.jcms.data.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsEntryIdTest {
    @Test
    public void roundtrip() {
        byte[] id = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        CmsEntryId a = new CmsEntryId(id);
        byte[] encoded = a.encode();
        CmsEntryId b = new CmsEntryId();
        b.decode(encoded);
        assertArrayEquals(id, b.value());
    }
}
