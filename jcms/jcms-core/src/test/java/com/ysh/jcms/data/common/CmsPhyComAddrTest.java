package com.ysh.jcms.data.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsPhyComAddrTest {
    @Test
    public void roundtrip() {
        CmsPhyComAddr a = new CmsPhyComAddr();
        a.addr.value(new byte[]{1,2,3,4,5,6});
        a.priority.value(3);
        a.vid.value(100);
        a.appid.value(200);
        byte[] encoded = a.encode();
        System.out.println("encoded " + encoded.length + " bytes");
        CmsPhyComAddr b = new CmsPhyComAddr();
        b.decode(encoded);
        assertArrayEquals(new byte[]{1,2,3,4,5,6}, b.addr.value());
        assertEquals(3, b.priority.value());
        assertEquals(100, b.vid.value());
        assertEquals(200, b.appid.value());
    }
}
