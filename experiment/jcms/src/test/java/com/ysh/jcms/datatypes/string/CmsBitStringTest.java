package com.ysh.jcms.datatypes.string;

import com.ysh.jcms.datatypes.type.AbstractCmsString.Mode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBitString")
class CmsBitStringTest {

    @Test
    void roundtrip() {
        byte[] bits = {(byte) 0xAB, (byte) 0xCD};
        byte[] data = new CmsBitString(bits).max(65535).encode();
        CmsBitString decoded = CmsBitString.decode(data, Mode.VARIABLE, 65535);
        assertArrayEquals(bits, decoded.get());
    }

    @Test
    void empty() {
        byte[] bits = new byte[0];
        byte[] data = new CmsBitString(bits).max(65535).encode();
        CmsBitString decoded = CmsBitString.decode(data, Mode.VARIABLE, 65535);
        assertArrayEquals(bits, decoded.get());
    }

    @Test
    void copy() {
        byte[] bits = {(byte) 0xAB, (byte) 0xCD};
        CmsBitString original = new CmsBitString(bits).max(65535);
        CmsBitString cloned = original.copy();
        assertArrayEquals(original.get(), cloned.get());
    }
}
