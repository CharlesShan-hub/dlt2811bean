package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsRcbOptFlds")
class CmsRcbOptFldsTest {

    @Test
    void roundtrip() {
        CmsRcbOptFlds original = new CmsRcbOptFlds(0x02ABL);
        byte[] data = original.encode();
        CmsRcbOptFlds decoded = CmsRcbOptFlds.decode(data);
        assertTrue(decoded.testBit(0) == original.testBit(0));
    }

    @Test
    void copy() {
        CmsRcbOptFlds original = new CmsRcbOptFlds(0x02ABL);
        CmsRcbOptFlds cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
