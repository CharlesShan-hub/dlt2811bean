package com.ysh.jcms.datatypes.enumerated;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTcmd")
class CmsTcmdTest {

    @Test
    void roundtrip() {
        CmsTcmd original = new CmsTcmd(1);
        byte[] data = original.encode();
        CmsTcmd decoded = CmsTcmd.decode(data);
        assertEquals(original.get(), decoded.get());
    }

    @Test
    void copy() {
        CmsTcmd original = new CmsTcmd(2);
        CmsTcmd cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
