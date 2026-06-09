package com.ysh.jcms.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsFloat32Test {
    @Test
    public void roundtrip() {
        CmsFloat32 a = new CmsFloat32(3.14f);
        byte[] encoded = a.encode();
        CmsFloat32 b = new CmsFloat32();
        b.decode(encoded);
        assertEquals(3.14f, b.value(), 1e-6f);
    }
}
