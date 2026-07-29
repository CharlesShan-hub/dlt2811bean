package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.scalar.CmsObjectReference;
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
