package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsMsvcbOptFldsTest {
    @Test
    public void roundtrip() {
        CmsMsvcbOptFlds a = new CmsMsvcbOptFlds()
            .refresh_time(true)
            .sample_rate(true);
        byte[] encoded = a.encode();
        CmsMsvcbOptFlds b = new CmsMsvcbOptFlds();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
