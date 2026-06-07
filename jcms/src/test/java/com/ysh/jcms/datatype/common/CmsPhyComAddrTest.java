package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsPhyComAddr")
class CmsPhyComAddrTest {

    private final byte[] MAC = new byte[]{0x01, 0x0C, (byte)0xCD, 0x01, 0x00, 0x01};

    @Test
    void roundtrip() {
        CmsPhyComAddr original = new CmsPhyComAddr();
        original.addr().value(MAC);
        original.priority().value((byte) 4);
        original.vid().value((short) 0);
        original.appid().value((short) 0x4000);

        CmsPhyComAddr decoded = new CmsPhyComAddr().decode(original.encode());
        assertArrayEquals(MAC, decoded.addr().value());
        assertEquals((byte) 4, decoded.priority().value());
        assertEquals((short) 0, decoded.vid().value());
        assertEquals((short) 0x4000, decoded.appid().value());
    }

    @Test
    void allDefault() {
        CmsPhyComAddr original = new CmsPhyComAddr();
        original.addr().value(MAC);

        CmsPhyComAddr decoded = new CmsPhyComAddr().decode(original.encode());
        assertEquals((byte) 0, decoded.priority().value());
        assertEquals((short) 0, decoded.vid().value());
        assertEquals((short) 0, decoded.appid().value());
    }

    @Test
    void emptyAddr() {
        CmsPhyComAddr original = new CmsPhyComAddr();
        original.addr().value(new byte[6]);
        original.priority().value((byte) 7);
        original.vid().value((short) 100);
        original.appid().value((short) 0x1000);

        CmsPhyComAddr decoded = new CmsPhyComAddr().decode(original.encode());
        assertArrayEquals(new byte[6], decoded.addr().value());
        assertEquals((byte) 7, decoded.priority().value());
        assertEquals((short) 100, decoded.vid().value());
        assertEquals((short) 0x1000, decoded.appid().value());
    }

    @Test
    void decodeOverwrites() {
        CmsPhyComAddr target = new CmsPhyComAddr();
        target.addr().value(new byte[]{1, 2, 3, 4, 5, 6});
        target.priority().value((byte) 4);
        target.vid().value((short) 10);
        target.appid().value((short) 20);

        CmsPhyComAddr source = new CmsPhyComAddr();
        source.addr().value(MAC);
        source.priority().value((byte) 0);
        source.vid().value((short) 0);
        source.appid().value((short) 0);

        target.decode(source.encode());
        assertArrayEquals(MAC, target.addr().value());
        assertEquals((byte) 0, target.priority().value());
        assertEquals((short) 0, target.vid().value());
        assertEquals((short) 0, target.appid().value());
    }
}
