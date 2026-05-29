package com.ysh.jcms.datatypes.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsObjectReference")
class CmsObjectReferenceTest {

    @Test
    void roundtrip() {
        CmsObjectReference original = new CmsObjectReference("LD1/LN1.DO1");
        byte[] data = original.encode();
        CmsObjectReference decoded = CmsObjectReference.decode(data);
        assertEquals(original.get(), decoded.get());
    }

    @Test
    void empty() {
        byte[] data = new CmsObjectReference("").encode();
        CmsObjectReference r = CmsObjectReference.decode(data);
        assertEquals("", r.get());
    }

    @Test
    void defaultValue() {
        assertEquals("", new CmsObjectReference().get());
    }

    @Test
    void copy() {
        CmsObjectReference original = new CmsObjectReference("LD1/LN1.DO1");
        CmsObjectReference cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
