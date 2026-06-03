package com.ysh.jcms.services.connect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAbortReason")
class CmsAbortReasonTest {

    @Test
    void roundtrip() {
        CmsAbortReason original = new CmsAbortReason(CmsAbortReason.INVALID_ARGUMENT);
        byte[] data = original.encode();
        CmsAbortReason decoded = CmsAbortReason.decode(data);
        assertTrue(decoded.is(CmsAbortReason.INVALID_ARGUMENT));
    }

    @Test
    void allValues() {
        for (int v = 0; v <= 5; v++) {
            byte[] data = new CmsAbortReason(v).encode();
            CmsAbortReason decoded = CmsAbortReason.decode(data);
            assertEquals(v, decoded.get());
        }
    }

    @Test
    void toStringTest() {
        CmsAbortReason r = new CmsAbortReason(CmsAbortReason.UNRECOGNIZED_SERVICE);
        assertEquals("(CmsAbortReason) UNRECOGNIZED_SERVICE(1)", r.toString());
    }

    @Test
    void copy() {
        CmsAbortReason original = new CmsAbortReason(CmsAbortReason.MAX_SERV_OUTSTANDING_EXCEEDED);
        CmsAbortReason cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }
}
