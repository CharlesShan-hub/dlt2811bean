package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsPhyComAddrTest {

    @Test
    void encodeDecode_basic() throws Exception {
        byte[] mac = {(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        CmsPhyComAddr addr = new CmsPhyComAddr();
        addr.setAddr(mac).setPriority(4).setVid(100).setAppid(1);

        PerOutputStream pos = new PerOutputStream();
        CmsPhyComAddr.encode(pos, addr);
        CmsPhyComAddr r = CmsPhyComAddr.decode(new PerInputStream(pos.toByteArray()));

        assertArrayEquals(mac, r.getAddr());
        assertEquals(4, r.getPriority());
        assertEquals(100, r.getVid());
        assertEquals(1, r.getAppid());
    }

    @Test
    void encodeDecode_allZeros() throws Exception {
        CmsPhyComAddr addr = new CmsPhyComAddr();
        addr.setAddr(new byte[6]).setPriority(0).setVid(0).setAppid(0);

        PerOutputStream pos = new PerOutputStream();
        CmsPhyComAddr.encode(pos, addr);
        CmsPhyComAddr r = CmsPhyComAddr.decode(new PerInputStream(pos.toByteArray()));

        assertArrayEquals(new byte[6], r.getAddr());
        assertEquals(0, r.getPriority());
        assertEquals(0, r.getVid());
        assertEquals(0, r.getAppid());
    }

    @Test
    void encodeDecode_maxValues() throws Exception {
        byte[] maxAddr = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        CmsPhyComAddr addr = new CmsPhyComAddr();
        addr.setAddr(maxAddr).setPriority(255).setVid(65535).setAppid(65535);

        PerOutputStream pos = new PerOutputStream();
        CmsPhyComAddr.encode(pos, addr);
        CmsPhyComAddr r = CmsPhyComAddr.decode(new PerInputStream(pos.toByteArray()));

        assertArrayEquals(maxAddr, r.getAddr());
        assertEquals(255, r.getPriority());
        assertEquals(65535, r.getVid());
        assertEquals(65535, r.getAppid());
    }
}
