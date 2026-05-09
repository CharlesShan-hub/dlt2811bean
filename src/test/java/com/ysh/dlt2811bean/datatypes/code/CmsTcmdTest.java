package com.ysh.dlt2811bean.datatypes.code;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTcmd")
class CmsTcmdTest {

    @Test
    @DisplayName("construct, set, encode/decode")
    void testAll() throws Exception {
        CmsTcmd cmd = new CmsTcmd();
        assertEquals(CmsTcmd.STOP, (long) cmd.get());

        cmd.set((long) CmsTcmd.HIGHER);
        assertEquals(CmsTcmd.HIGHER, (long) cmd.get());

        PerOutputStream pos = new PerOutputStream();
        cmd.encode(pos);

        CmsTcmd decoded = new CmsTcmd().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsTcmd.HIGHER, (long) decoded.get());
    }

    @Test
    @DisplayName("static write and read")
    void staticWriteRead() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsTcmd.write(pos, (long) CmsTcmd.LOWER);

        CmsTcmd decoded = CmsTcmd.read(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsTcmd.LOWER, (long) decoded.get());
    }
}
