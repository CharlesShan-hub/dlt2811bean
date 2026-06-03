package com.ysh.jcms.datatypes.enumerated;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsServiceError")
class CmsServiceErrorTest {

    @Test
    void roundtrip() {
        CmsServiceError original = new CmsServiceError(1);
        byte[] data = original.encode();
        CmsServiceError decoded = CmsServiceError.from(data);
        assertEquals(original.get(), decoded.get());
    }

    @Test
    void is() {
        CmsServiceError err = new CmsServiceError(3);
        assertTrue(err.is(3));
        assertFalse(err.is(0));
    }

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsServiceError().get());
    }
}
