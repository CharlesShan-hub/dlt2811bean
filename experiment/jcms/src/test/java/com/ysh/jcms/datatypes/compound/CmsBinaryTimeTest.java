package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBinaryTime")
class CmsBinaryTimeTest {

    @Test
    void roundtrip() {
        CmsBinaryTime original = new CmsBinaryTime(10, 30, 45, 500);
        byte[] data = original.encode();
        CmsBinaryTime decoded = CmsBinaryTime.decode(data);
        assertEquals(original.getHour(), decoded.getHour());
        assertEquals(original.getMinute(), decoded.getMinute());
        assertEquals(original.getSecond(), decoded.getSecond());
        assertEquals(original.getMillisecond(), decoded.getMillisecond());
    }

    @Test
    void copy() {
        CmsBinaryTime original = new CmsBinaryTime(10, 30, 45, 500);
        CmsBinaryTime cloned = original.copy();
        assertEquals(original.getHour(), cloned.getHour());
        assertEquals(original.getMinute(), cloned.getMinute());
        assertEquals(original.getSecond(), cloned.getSecond());
        assertEquals(original.getMillisecond(), cloned.getMillisecond());
    }
}
