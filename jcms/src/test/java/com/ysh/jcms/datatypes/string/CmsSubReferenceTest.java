package com.ysh.jcms.datatypes.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSubReference")
class CmsSubReferenceTest {

    @Test
    void roundtrip() {
        CmsSubReference original = new CmsSubReference("SubRef1");
        byte[] data = original.encode();
        CmsSubReference decoded = CmsSubReference.from(data);
        assertEquals(original.get(), decoded.get());
    }

    @Test
    void empty() {
        byte[] data = new CmsSubReference("").encode();
        CmsSubReference r = CmsSubReference.from(data);
        assertEquals("", r.get());
    }

    @Test
    void defaultValue() {
        assertEquals("", new CmsSubReference().get());
    }

    @Test
    void copy() {
        CmsSubReference original = new CmsSubReference("SubRef1");
        CmsSubReference cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
