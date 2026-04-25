package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.enumerated.CmsTcmd;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTcmd")
class CmsTcmdTest {

    @Test
    @DisplayName("construct, set, is, encode/decode")
    void testAll() throws Exception {
        CmsTcmd cmd = new CmsTcmd();
        assertEquals(CmsTcmd.STOP, cmd.get());

        cmd.set(CmsTcmd.HIGHER);
        assertTrue(cmd.is(CmsTcmd.HIGHER));
        assertFalse(cmd.is(CmsTcmd.LOWER));

        PerOutputStream pos = new PerOutputStream();
        cmd.encode(pos);

        CmsTcmd decoded = new CmsTcmd().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsTcmd.HIGHER, decoded.get());
        assertTrue(decoded.is(CmsTcmd.HIGHER));
    }

    @Test
    @DisplayName("static write and read")
    void staticWriteRead() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsTcmd.write(pos, CmsTcmd.LOWER);

        CmsTcmd decoded = CmsTcmd.read(new PerInputStream(pos.toByteArray()));
        assertTrue(decoded.is(CmsTcmd.LOWER));
    }
}
