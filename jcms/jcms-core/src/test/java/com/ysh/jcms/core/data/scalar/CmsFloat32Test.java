package com.ysh.jcms.core.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsFloat32Test {
    @Test
    public void roundup() {
        CmsFloat32 a = new CmsFloat32(3.14f);
        byte[] encoded = a.encode();
        CmsFloat32 b = new CmsFloat32();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
