package com.ysh.jcms.core.data.sequence.common;

import com.ysh.jcms.core.data.sequence.common.CmsPhyComAddr;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsPhyComAddrTest {
    @Test
    public void roundup() {
        CmsPhyComAddr a = new CmsPhyComAddr().addr(new byte[]{1, 2, 3, 4, 5, 6}).priority(3).vid(100).appid(200);
        byte[] encoded = a.encode();

        CmsPhyComAddr b = new CmsPhyComAddr();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
