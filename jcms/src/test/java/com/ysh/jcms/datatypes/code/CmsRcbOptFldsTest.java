package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsRcbOptFlds")
class CmsRcbOptFldsTest {

    @Test
    void roundtrip() {
        CmsRcbOptFlds original = new CmsRcbOptFlds(0x02AB);
        byte[] data = original.encode();
        CmsRcbOptFlds decoded = CmsRcbOptFlds.decode(data);
        assertEquals(
            decoded.testBit(CmsRcbOptFlds.SEQUENCE_NUMBER),
            original.testBit(CmsRcbOptFlds.SEQUENCE_NUMBER)
        );
    }

    @Test
    void copy() {
        CmsRcbOptFlds original = new CmsRcbOptFlds(0x02AB);
        CmsRcbOptFlds cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
