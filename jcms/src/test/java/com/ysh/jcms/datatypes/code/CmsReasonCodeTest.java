package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsReasonCode")
class CmsReasonCodeTest {

    @Test
    void roundtrip() {
        CmsReasonCode original = new CmsReasonCode(0x15);
        byte[] data = original.encode();
        CmsReasonCode decoded = CmsReasonCode.from(data);
        assertTrue(decoded.testBit(0) == original.testBit(0));
    }

    @Test
    void copy() {
        CmsReasonCode original = new CmsReasonCode(0x15);
        CmsReasonCode cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
