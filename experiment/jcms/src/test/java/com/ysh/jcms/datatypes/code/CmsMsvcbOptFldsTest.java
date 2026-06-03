package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsMsvcbOptFlds")
class CmsMsvcbOptFldsTest {

    @Test
    void roundtrip() {
        CmsMsvcbOptFlds original = new CmsMsvcbOptFlds(0x15);
        byte[] data = original.encode();
        CmsMsvcbOptFlds decoded = CmsMsvcbOptFlds.decode(data);
        assertTrue(decoded.testBit(0) == original.testBit(0));
    }

    @Test
    void copy() {
        CmsMsvcbOptFlds original = new CmsMsvcbOptFlds(0x15);
        CmsMsvcbOptFlds cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
