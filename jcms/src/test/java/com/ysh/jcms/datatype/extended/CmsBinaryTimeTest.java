package com.ysh.jcms.datatype.extended;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBinaryTime")
class CmsBinaryTimeTest {

    private CmsBinaryTime get() { return (CmsBinaryTime)(new CmsBinaryTime().test()); }

    @Test
    void setFromComponents() {
        CmsBinaryTime original = get().set(2024, 6, 6, 10, 30, 45, 500);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setFromComponentsWithoutMillis() {
        CmsBinaryTime original = get().set(2024, 6, 6, 10, 30, 45);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setFromEpochMillis() {
        CmsBinaryTime original = get().set(1718015445500L);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setFromZonedDateTime() {
        CmsBinaryTime original = get()
            .set(ZonedDateTime.parse("2024-06-06T10:30:45.500Z"));
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void nowRoundtrip() {
        CmsBinaryTime original = get().now();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void epochStart() {
        CmsBinaryTime t = get().set(1970, 1, 1, 0, 0, 0);
        assertEquals(-5113, t.daysSince1984().value());
        assertEquals(0, t.msOfDay().value());
    }

    @Test
    void leapYear() {
        CmsBinaryTime t = get().set(2024, 12, 31, 23, 59, 59, 999);
        assertEquals(t, get().decode(t.encode()));
    }
}
