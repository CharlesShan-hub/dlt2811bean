package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsDbposTest {

    @Test
    void encodeDecode_intermediate() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsDbpos.encode(pos, new CmsDbpos(CmsDbpos.INTERMEDIATE));
        assertEquals(CmsDbpos.INTERMEDIATE, CmsDbpos.decode(new PerInputStream(pos.toByteArray())).getValue());
    }

    @Test
    void encodeDecode_off() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsDbpos.encode(pos, new CmsDbpos(CmsDbpos.OFF));
        assertEquals(CmsDbpos.OFF, CmsDbpos.decode(new PerInputStream(pos.toByteArray())).getValue());
    }

    @Test
    void encodeDecode_on() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsDbpos.encode(pos, new CmsDbpos(CmsDbpos.ON));
        assertEquals(CmsDbpos.ON, CmsDbpos.decode(new PerInputStream(pos.toByteArray())).getValue());
    }

    @Test
    void encodeDecode_bad() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsDbpos.encode(pos, new CmsDbpos(CmsDbpos.BAD));
        assertEquals(CmsDbpos.BAD, CmsDbpos.decode(new PerInputStream(pos.toByteArray())).getValue());
    }

    @Test
    void encodeDecode_allValues() throws Exception {
        for (int v = 0; v <= 3; v++) {
            PerOutputStream pos = new PerOutputStream();
            CmsDbpos.encode(pos, new CmsDbpos(v));
            assertEquals(v, CmsDbpos.decode(new PerInputStream(pos.toByteArray())).getValue());
        }
    }

    @Test
    void constructor_defaultIsIntermediate() {
        CmsDbpos d = new CmsDbpos();
        assertEquals(CmsDbpos.INTERMEDIATE, d.getValue());
    }

    @Test
    void constructor_rejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new CmsDbpos(-1));
        assertThrows(IllegalArgumentException.class, () -> new CmsDbpos(4));
    }

    @Test
    void semanticSetters_chain() {
        CmsDbpos d = new CmsDbpos();
        d.setOn();
        assertEquals(CmsDbpos.ON, d.getValue());
        d.setOff();
        assertEquals(CmsDbpos.OFF, d.getValue());
    }
}
