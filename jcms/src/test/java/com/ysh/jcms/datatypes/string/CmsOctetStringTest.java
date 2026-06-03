package com.ysh.jcms.datatypes.string;

import com.ysh.jcms.datatypes.type.AbstractCmsString.Mode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOctetString")
class CmsOctetStringTest {

    @Test
    void roundtrip() {
        byte[] data = {0x01, 0x02, 0x03, 0x04, (byte) 0xFF};
        byte[] encoded = new CmsOctetString(data).max(65535).encode();
        CmsOctetString decoded = CmsOctetString.decode(encoded, Mode.VARIABLE, 65535);
        assertArrayEquals(data, decoded.get());
    }

    @Test
    void empty() {
        byte[] data = new byte[0];
        byte[] encoded = new CmsOctetString(data).max(65535).encode();
        CmsOctetString decoded = CmsOctetString.decode(encoded, Mode.VARIABLE, 65535);
        assertArrayEquals(data, decoded.get());
    }

    @Test
    void defaultValue() {
        assertArrayEquals(new byte[0], new CmsOctetString().get());
    }

    @Test
    void copy() {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString original = new CmsOctetString(data).max(65535);
        CmsOctetString cloned = original.copy();
        assertArrayEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }
}
