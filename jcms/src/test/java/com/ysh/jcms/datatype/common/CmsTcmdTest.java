package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTcmd")
class CmsTcmdTest {

    private CmsTcmd get() { return (CmsTcmd)(new CmsTcmd().test()); }

    @Test
    void reserved() {
        CmsTcmd a = get().value(CmsTcmd.RESERVED);
        CmsTcmd b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void select() {
        CmsTcmd a = get().value(CmsTcmd.SELECT);
        CmsTcmd b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void operate() {
        CmsTcmd a = get().value(CmsTcmd.OPERATE);
        CmsTcmd b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void cancel() {
        CmsTcmd a = get().value(CmsTcmd.CANCEL);
        CmsTcmd b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, get().value());
    }

    @Test
    void decodeOverwrites() {
        CmsTcmd src = get().value(CmsTcmd.SELECT);
        CmsTcmd target = get().decode(src.encode());
        assertEquals(src, target);
    }
}
