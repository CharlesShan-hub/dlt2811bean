package com.ysh.jcms.core.data.sequence.common;

import com.ysh.jcms.core.data.scalar.CmsEntryId;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsEntryIdTest {
    @Test
    public void roundup() {
        byte[] id = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        CmsEntryId a = new CmsEntryId(id);
        byte[] encoded = a.encode();
        CmsEntryId b = new CmsEntryId();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
