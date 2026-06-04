package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8")
class CmsInt8Test {

    @Test
    void positive() {
        byte[] data = new CmsInt8(42).encode();
        CmsInt8 r = CmsInt8.from(data);
        assertEquals(42, (int) r.get());
    }

    @Test
    void negative() {
        byte[] data = new CmsInt8(-42).encode();
        CmsInt8 r = CmsInt8.from(data);
        assertEquals(-42, (int) r.get());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt8(0).encode();
        CmsInt8 r = CmsInt8.from(data);
        assertEquals(0, (int) r.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsInt8().get());
    }

    @Test
    void set() {
        CmsInt8 val = new CmsInt8();
        val.set(42);
        assertEquals(42, (int) val.get());
    }

    @Test
    void setNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8().set(null));
    }

    @Test
    void copy() {
        CmsInt8 original = new CmsInt8(42);
        CmsInt8 cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    void copyIsDeep() {
        CmsInt8 original = new CmsInt8(42);
        CmsInt8 cloned = original.copy();
        cloned.set(99);
        assertEquals(42, (int) original.get());
    }

    @Test
    void roundtrip() {
        CmsInt8 original = new CmsInt8(-42);
        byte[] data = original.encode();
        CmsInt8 decoded = CmsInt8.from(data);
        assertEquals(original.get(), decoded.get());
    }
}
