package com.ysh.jcms.datatypes.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsVisibleString")
class CmsVisibleStringTest {

    @Test
    void constructWithValue() {
        CmsVisibleString str = new CmsVisibleString("test");
        assertEquals("test", str.get());
    }

    @Test
    void defaultConstructor() {
        CmsVisibleString str = new CmsVisibleString();
        assertEquals("", str.get());
    }

    @Test
    void set() {
        CmsVisibleString str = new CmsVisibleString();
        str.set("hello");
        assertEquals("hello", str.get());
    }

    @Test
    void setNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CmsVisibleString().set(null));
    }

    @Test
    void copy() {
        CmsVisibleString original = new CmsVisibleString("HelloCMS");
        CmsVisibleString cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    void roundtrip() {
        CmsVisibleString original = new CmsVisibleString("HelloCMS");
        byte[] data = original.encode();
        CmsVisibleString decoded = CmsVisibleString.decode(data);
        assertEquals(original.get(), decoded.get());
    }
}
