package com.ysh.jcms.datatype.basic;

import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBinaryTime")
class CmsBinaryTimeTest {

    @Test
    void setFromComponents() {
        // 2024-06-06T10:30:45.500Z
        CmsBinaryTime original = new CmsBinaryTime().set(2024, 6, 6, 10, 30, 45, 500);
        assertEquals(original, new CmsBinaryTime().decode(original.encode()));
    }

    @Test
    void setFromComponentsWithoutMillis() {
        // 2024-06-06T10:30:45.000Z
        CmsBinaryTime original = new CmsBinaryTime().set(2024, 6, 6, 10, 30, 45);
        assertEquals(original, new CmsBinaryTime().decode(original.encode()));
    }

    @Test
    void setFromEpochMillis() {
        // 2024-06-06T10:30:45.500Z
        CmsBinaryTime original = new CmsBinaryTime().set(1718015445500L);
        assertEquals(original, new CmsBinaryTime().decode(original.encode()));
    }

    @Test
    void setFromZonedDateTime() {
        CmsBinaryTime original = new CmsBinaryTime()
            .set(ZonedDateTime.parse("2024-06-06T10:30:45.500Z"));
        assertEquals(original, new CmsBinaryTime().decode(original.encode()));
    }

    @Test
    void nowRoundtrip() {
        CmsBinaryTime original = new CmsBinaryTime().now();
        assertEquals(original, new CmsBinaryTime().decode(original.encode()));
    }

    @Test
    void epochStart() {
        // 1970-01-01T00:00:00.000Z → daysSince1984 = -5113
        CmsBinaryTime t = new CmsBinaryTime().set(1970, 1, 1, 0, 0, 0);
        assertEquals(-5113, t.daysSince1984().value());
        assertEquals(0, t.msOfDay().value());
    }

    @Test
    void leapYear() {
        // 2024-12-31T23:59:59.999Z — leap year + max ms of day
        CmsBinaryTime t = new CmsBinaryTime().set(2024, 12, 31, 23, 59, 59, 999);
        assertEquals(t, new CmsBinaryTime().decode(t.encode()));
    }
}
