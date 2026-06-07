package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTcmd")
class CmsTcmdTest {

    @Test
    void reserved() {
        CmsTcmd original = new CmsTcmd().value(CmsTcmd.RESERVED);
        assertEquals(original, new CmsTcmd().decode(original.encode()));
    }

    @Test
    void select() {
        CmsTcmd original = new CmsTcmd().value(CmsTcmd.SELECT);
        assertEquals(original, new CmsTcmd().decode(original.encode()));
    }

    @Test
    void operate() {
        CmsTcmd original = new CmsTcmd().value(CmsTcmd.OPERATE);
        assertEquals(original, new CmsTcmd().decode(original.encode()));
    }

    @Test
    void cancel() {
        CmsTcmd original = new CmsTcmd().value(CmsTcmd.CANCEL);
        assertEquals(original, new CmsTcmd().decode(original.encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsTcmd().value());
    }

    @Test
    void decodeOverwrites() {
        CmsTcmd target = new CmsTcmd().value(CmsTcmd.SELECT);
        target.decode(new CmsTcmd().value(CmsTcmd.OPERATE).encode());
        assertEquals(new CmsTcmd().value(CmsTcmd.OPERATE), target);
    }
}
