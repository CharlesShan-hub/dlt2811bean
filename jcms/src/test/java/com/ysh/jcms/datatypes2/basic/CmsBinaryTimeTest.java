package com.ysh.jcms.datatypes2.basic;

import com.ysh.jcms.datatypes2.data.extended.CmsBinaryTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBinaryTime")
class CmsBinaryTimeTest {

    @Test
    void roundtrip() {
        CmsBinaryTime original = new CmsBinaryTime();
        original.msOfDay = new com.ysh.jcms.datatypes2.data.basic.CmsInt32U(10 * 3600000 + 30 * 60000 + 45 * 1000 + 500);
        original.daysSince1984 = new com.ysh.jcms.datatypes2.data.basic.CmsInt16U(0);

        byte[] data = original.encode();
        CmsBinaryTime decoded = CmsBinaryTime.from(data);
        assertEquals(original.msOfDay.longValue(), decoded.msOfDay.longValue());
        assertEquals(original.daysSince1984.intValue(), decoded.daysSince1984.intValue());
    }
}
