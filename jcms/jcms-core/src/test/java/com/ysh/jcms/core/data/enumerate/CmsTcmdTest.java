package com.ysh.jcms.core.data.enumerate;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsTcmdTest {
    @Test
    public void roundup() {
        CmsTcmd a = new CmsTcmd(CmsTcmd.SELECT);
        byte[] encoded = a.encode();
        CmsTcmd b = new CmsTcmd();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
