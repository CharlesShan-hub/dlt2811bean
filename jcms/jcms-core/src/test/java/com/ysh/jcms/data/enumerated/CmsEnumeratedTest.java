package com.ysh.jcms.data.enumerated;

import com.ysh.jcms.core.CmsEnumerated;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsEnumeratedTest {
    @Test
    public void roundup() {
        CmsEnumerated a = new CmsEnumerated(5);
        byte[] encoded = a.encode();
        CmsEnumerated b = new CmsEnumerated();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
