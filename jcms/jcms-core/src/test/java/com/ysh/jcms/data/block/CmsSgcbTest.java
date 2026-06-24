package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSgcbTest {
    @Test
    public void roundup() {
        CmsSgcb a = new CmsSgcb()
            .numOfSG(5)
            .actSG(3)
            .editSG(1);
        a.tActEdt.secondsSinceEpoch.value(1234567890L);
        byte[] encoded = a.encode();
        CmsSgcb b = new CmsSgcb();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
