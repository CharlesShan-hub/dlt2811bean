package com.ysh.jcms.datatypes.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsObjectName")
class CmsObjectNameTest {

    @Test
    void roundtrip() {
        CmsObjectName original = new CmsObjectName("MyObject");
        byte[] data = original.encode();
        CmsObjectName decoded = CmsObjectName.decode(data);
        assertEquals(original.get(), decoded.get());
    }

    @Test
    void empty() {
        byte[] data = new CmsObjectName("").encode();
        CmsObjectName r = CmsObjectName.decode(data);
        assertEquals("", r.get());
    }

    @Test
    void defaultValue() {
        assertEquals("", new CmsObjectName().get());
    }

    @Test
    void copy() {
        CmsObjectName original = new CmsObjectName("MyObject");
        CmsObjectName cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
