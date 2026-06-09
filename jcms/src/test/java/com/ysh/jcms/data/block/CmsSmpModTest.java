package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSmpModTest {
    @Test
    public void roundtrip() {
        CmsSmpMod a = new CmsSmpMod(CmsSmpMod.SAMPLES_PER_SECOND);
        byte[] encoded = a.encode();
        CmsSmpMod b = new CmsSmpMod();
        b.decode(encoded);
        assertEquals(CmsSmpMod.SAMPLES_PER_SECOND, b.value());
    }
}
