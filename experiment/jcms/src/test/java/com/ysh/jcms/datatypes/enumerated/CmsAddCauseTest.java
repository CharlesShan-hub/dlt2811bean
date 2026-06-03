package com.ysh.jcms.datatypes.enumerated;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAddCause")
class CmsAddCauseTest {

    @Test
    void roundtrip() {
        CmsAddCause original = new CmsAddCause(2);
        byte[] data = original.encode();
        CmsAddCause decoded = CmsAddCause.decode(data);
        System.out.println(decoded);
        assertEquals(original.get(), decoded.get());
    }

    @Test
    void is() {
        CmsAddCause ac = new CmsAddCause(5);
        assertTrue(ac.is(5));
        assertFalse(ac.is(1));
    }

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsAddCause().get());
    }
}
