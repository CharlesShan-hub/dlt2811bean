package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsTcmdTest {

    @Test
    void encodeDecode_stop() throws Exception {
        CmsTcmd t = new CmsTcmd(CmsTcmd.STOP);
        PerOutputStream pos = new PerOutputStream();
        CmsTcmd.encode(pos, t);
        assertEquals(CmsTcmd.STOP, CmsTcmd.decode(new PerInputStream(pos.toByteArray())).getValue());
    }

    @Test
    void encodeDecode_lower() throws Exception {
        CmsTcmd t = new CmsTcmd(CmsTcmd.LOWER);
        PerOutputStream pos = new PerOutputStream();
        CmsTcmd.encode(pos, t);
        assertEquals(CmsTcmd.LOWER, CmsTcmd.decode(new PerInputStream(pos.toByteArray())).getValue());
    }

    @Test
    void encodeDecode_higher() throws Exception {
        CmsTcmd t = new CmsTcmd(CmsTcmd.HIGHER);
        PerOutputStream pos = new PerOutputStream();
        CmsTcmd.encode(pos, t);
        assertEquals(CmsTcmd.HIGHER, CmsTcmd.decode(new PerInputStream(pos.toByteArray())).getValue());
    }

    @Test
    void semanticSetters() throws Exception {
        CmsTcmd t = new CmsTcmd();
        t.setHigher();
        assertEquals(CmsTcmd.HIGHER, t.getValue());
        t.setStop();
        assertEquals(CmsTcmd.STOP, t.getValue());
    }

    @Test
    void constructor_rejectsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new CmsTcmd(-1));
        assertThrows(IllegalArgumentException.class, () -> new CmsTcmd(4));
    }
}
