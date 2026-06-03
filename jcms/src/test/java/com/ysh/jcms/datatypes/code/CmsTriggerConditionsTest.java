package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTriggerConditions")
class CmsTriggerConditionsTest {

    @Test
    void roundtrip() {
        CmsTriggerConditions original = new CmsTriggerConditions(0x2A);
        byte[] data = original.encode();
        CmsTriggerConditions decoded = CmsTriggerConditions.from(data);
        assertTrue(decoded.testBit(0) == original.testBit(0));
    }

    @Test
    void copy() {
        CmsTriggerConditions original = new CmsTriggerConditions(0x2A);
        CmsTriggerConditions cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
