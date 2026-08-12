package com.ysh.jcms.core.data.enumerate;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSmpModTest {
    @Test
    public void roundup() {
        CmsSmpMod a = new CmsSmpMod(CmsSmpMod.SAMPLES_PER_SECOND);
        byte[] encoded = a.encode();
        CmsSmpMod b = new CmsSmpMod();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
