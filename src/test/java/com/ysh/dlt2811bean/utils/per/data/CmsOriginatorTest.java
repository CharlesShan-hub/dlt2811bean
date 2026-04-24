package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
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
    @DisplayName("chain setters via Lombok fluent setters")
    void setters_fluent() {
        CmsOriginator orig = new CmsOriginator()
            .orCat(new CmsOrCat(CmsOrCat.STATION_CONTROL))
            .orIdent(new CmsOctetString(new byte[]{0x0A, 0x0B, 0x0C}).max(64));
        assertEquals(CmsOrCat.STATION_CONTROL, orig.orCat.get());
        assertArrayEquals(new byte[]{0x0A, 0x0B, 0x0C}, orig.orIdent.get());
    }
}