package com.ysh.jcms.data.sequence.block;

import com.ysh.jcms.data.sequence.block.CmsSgcb;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSgcbTest {
    @Test
    public void roundup() {
        CmsSgcb a = new CmsSgcb().numOfSG(5).actSG(3).editSG(1);
        byte[] encoded = a.encode();
        CmsSgcb b = new CmsSgcb();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
