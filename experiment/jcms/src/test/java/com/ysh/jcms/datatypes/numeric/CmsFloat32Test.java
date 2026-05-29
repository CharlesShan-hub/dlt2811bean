package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFloat32")
class CmsFloat32Test {

    @Test
    void positive() {
        byte[] data = new CmsFloat32(3.14159f).encode();
        CmsFloat32 r = CmsFloat32.decode(data);
        assertEquals(3.14159f, r.get(), 1e-6f);
    }

    @Test
    void zero() {
        byte[] data = new CmsFloat32(0f).encode();
        CmsFloat32 r = CmsFloat32.decode(data);
        assertEquals(0f, r.get(), 1e-6f);
    }

    @Test
    void defaultValue() {
        assertEquals(0.0f, new CmsFloat32().get(), 1e-6f);
    }

    @Test
    void roundtrip() {
        CmsFloat32 original = new CmsFloat32(3.14159f);
        byte[] data = original.encode();
        CmsFloat32 decoded = CmsFloat32.decode(data);
        assertEquals(original.get(), decoded.get(), 1e-6f);
    }
}
