package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBinaryTime")
class CmsBinaryTimeTest {

    @Test
    @DisplayName("full constructor and encode/decode roundtrip")
    void encodeDecode_roundtrip() throws Exception {
        CmsBinaryTime t = new CmsBinaryTime(43200000L, 15000);

        PerOutputStream pos = new PerOutputStream();
        t.encode(pos);

        CmsBinaryTime r = new CmsBinaryTime().decode(new PerInputStream(pos.toByteArray()));

        assertEquals(43200000L, r.msOfDay.get());
        assertEquals(15000, r.daysSince1984.get());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsBinaryTime original = new CmsBinaryTime(43200000L, 15000);
        CmsBinaryTime cloned = original.copy();
        assertEquals(original.msOfDay.get(), cloned.msOfDay.get());
        assertEquals(original.daysSince1984.get(), cloned.daysSince1984.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is deep")
    void copyIsDeep() {
        CmsBinaryTime original = new CmsBinaryTime(43200000L, 15000);
        CmsBinaryTime cloned = original.copy();
        cloned.msOfDay.set(999L);
        assertEquals(43200000L, original.msOfDay.get());
    }

    @Test
    @DisplayName("chain setters via public fields")
    void setters_fields() {
        CmsBinaryTime t = new CmsBinaryTime();
        t.msOfDay.set(43200000L);
        t.daysSince1984.set(15000);
        assertEquals(43200000L, t.msOfDay.get());
        assertEquals(15000, t.daysSince1984.get());
    }

    @Test
    @DisplayName("convenience setters with raw values")
    void setters_convenience() {
        CmsBinaryTime t = new CmsBinaryTime()
            .msOfDay(43200000L)
            .daysSince1984(15000);
        assertEquals(43200000L, t.msOfDay.get());
        assertEquals(15000, t.daysSince1984.get());
    }
}