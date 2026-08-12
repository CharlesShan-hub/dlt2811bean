package com.ysh.jcms.core.data.sequence.common;

import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsObjectReferenceTest {
    @Test
    public void roundup() {
        CmsObjectReference a = new CmsObjectReference("ObjRefTest");
        byte[] encoded = a.encode();
        CmsObjectReference b = new CmsObjectReference();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
