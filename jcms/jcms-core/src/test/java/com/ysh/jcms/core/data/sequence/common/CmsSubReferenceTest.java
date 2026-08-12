package com.ysh.jcms.core.data.sequence.common;

import com.ysh.jcms.core.data.scalar.CmsSubReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSubReferenceTest {
    @Test
    public void roundup() {
        CmsSubReference a = new CmsSubReference("SubRefTest");
        byte[] encoded = a.encode();
        CmsSubReference b = new CmsSubReference();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
