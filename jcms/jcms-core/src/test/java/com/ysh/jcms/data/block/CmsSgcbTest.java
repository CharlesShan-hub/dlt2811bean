package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSgcbTest {
    @Test
    public void roundtrip() {
        CmsSgcb a = new CmsSgcb();
        a.numOfSG.value(5);
        a.actSG.value(3);
        a.editSG.value(1);
        a.tActEdt.seconds_since_epoch.value(1234567890L);
        byte[] encoded = a.encode();
        CmsSgcb b = new CmsSgcb();
        b.decode(encoded);
        assertEquals(5, b.numOfSG.value());
        assertEquals(3, b.actSG.value());
        assertEquals(1, b.editSG.value());
    }
}
