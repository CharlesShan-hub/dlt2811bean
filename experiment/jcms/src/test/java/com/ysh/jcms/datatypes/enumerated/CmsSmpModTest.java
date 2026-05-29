package com.ysh.jcms.datatypes.enumerated;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSmpMod")
class CmsSmpModTest {

    @Test
    void roundtrip() {
        CmsSmpMod original = new CmsSmpMod(1);
        byte[] data = original.encode();
        CmsSmpMod decoded = CmsSmpMod.decode(data);
        assertEquals(original.get(), decoded.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsSmpMod().get());
    }
}
