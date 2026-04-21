package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsOriginatorTest {

    @Test
    void encodeDecode_local() throws Exception {
        CmsOriginator orig = new CmsOriginator();
        orig.setOrCat(CmsOriginator.BAY_CONTROL)
            .setOrIdent(new byte[]{0x01, 0x02, 0x03});

        PerOutputStream pos = new PerOutputStream();
        CmsOriginator.encode(pos, orig);
        CmsOriginator r = CmsOriginator.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(CmsOriginator.BAY_CONTROL, r.getOrCat());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, r.getOrIdent());
    }

    @Test
    void encodeDecode_default() throws Exception {
        CmsOriginator orig = new CmsOriginator();

        PerOutputStream pos = new PerOutputStream();
        CmsOriginator.encode(pos, orig);
        CmsOriginator r = CmsOriginator.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(CmsOriginator.NOT_SUPPORTED, r.getOrCat());
        assertArrayEquals(new byte[0], r.getOrIdent());
    }

    @Test
    void encodeDecode_remote() throws Exception {
        CmsOriginator orig = new CmsOriginator();
        orig.setOrCat(CmsOriginator.REMOTE_CONTROL)
            .setOrIdent(new byte[64]); // max length

        PerOutputStream pos = new PerOutputStream();
        CmsOriginator.encode(pos, orig);
        CmsOriginator r = CmsOriginator.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(CmsOriginator.REMOTE_CONTROL, r.getOrCat());
        assertEquals(64, r.getOrIdent().length);
    }

    @Test
    void encode_nullIdentBecomesEmpty() throws Exception {
        CmsOriginator orig = new CmsOriginator();
        orig.setOrCat(CmsOriginator.BAY_CONTROL)
            .setOrIdent(null);

        PerOutputStream pos = new PerOutputStream();
        CmsOriginator.encode(pos, orig);
        CmsOriginator r = CmsOriginator.decode(new PerInputStream(pos.toByteArray()));

        assertArrayEquals(new byte[0], r.getOrIdent());
    }
}
