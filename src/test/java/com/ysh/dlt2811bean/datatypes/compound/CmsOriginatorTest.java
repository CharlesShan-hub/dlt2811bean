package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsOrCat;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOriginator")
class CmsOriginatorTest {

    @Test
    @DisplayName("encode and decode roundtrip")
    void encodeDecode_roundtrip() throws Exception {
        CmsOriginator orig = new CmsOriginator(CmsOrCat.BAY_CONTROL, new byte[]{0x01, 0x02});

        PerOutputStream pos = new PerOutputStream();
        orig.encode(pos);

        CmsOriginator r = new CmsOriginator().decode(new PerInputStream(pos.toByteArray()));

        assertEquals(CmsOrCat.BAY_CONTROL, r.orCat.get());
        assertArrayEquals(new byte[]{0x01, 0x02}, r.orIdent.get());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsOriginator original = new CmsOriginator(CmsOrCat.BAY_CONTROL, new byte[]{0x01, 0x02});
        CmsOriginator cloned = original.copy();
        assertEquals(original.orCat.get(), cloned.orCat.get());
        assertArrayEquals(original.orIdent.get(), cloned.orIdent.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is deep")
    void copyIsDeep() {
        CmsOriginator original = new CmsOriginator(CmsOrCat.BAY_CONTROL, new byte[]{0x01, 0x02});
        CmsOriginator cloned = original.copy();
        cloned.orCat.set(CmsOrCat.STATION_CONTROL);
        assertEquals(CmsOrCat.BAY_CONTROL, original.orCat.get());
    }

    @Test
    @DisplayName("chain setters via public fields")
    void setters_fields() {
        CmsOriginator orig = new CmsOriginator();
        orig.orCat.set(CmsOrCat.STATION_CONTROL);
        orig.orIdent.set(new byte[]{0x0A, 0x0B, 0x0C});
        assertEquals(CmsOrCat.STATION_CONTROL, orig.orCat.get());
        assertArrayEquals(new byte[]{0x0A, 0x0B, 0x0C}, orig.orIdent.get());
    }

    @Test
    @DisplayName("convenience setter with raw int value")
    void setters_convenience() {
        CmsOriginator orig = new CmsOriginator()
            .orCat(CmsOrCat.STATION_CONTROL)
            .orIdent(new CmsOctetString(new byte[]{0x0A, 0x0B, 0x0C}).max(64));
        assertEquals(CmsOrCat.STATION_CONTROL, orig.orCat.get());
        assertArrayEquals(new byte[]{0x0A, 0x0B, 0x0C}, orig.orIdent.get());
    }
}