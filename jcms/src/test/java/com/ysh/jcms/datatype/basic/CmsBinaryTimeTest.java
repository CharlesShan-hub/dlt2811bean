package com.ysh.jcms.datatype.basic;

import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBinaryTime")
class CmsBinaryTimeTest {

    @Test
    void roundtrip() {
        CmsBinaryTime original = new CmsBinaryTime();
        original.msOfDay.value(10 * 3600000 + 30 * 60000 + 45 * 1000 + 500);
        original.daysSince1984.value((short) 0);

        byte[] data = original.encode();
        CmsBinaryTime decoded = new CmsBinaryTime().decode(data);
        assertEquals(original.msOfDay.value(), decoded.msOfDay.value());
        assertEquals(original.daysSince1984.value(), decoded.daysSince1984.value());
    }
}
