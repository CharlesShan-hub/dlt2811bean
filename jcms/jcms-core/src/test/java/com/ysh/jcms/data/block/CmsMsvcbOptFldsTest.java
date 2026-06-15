package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsMsvcbOptFldsTest {
    @Test
    public void roundtrip() {
        CmsMsvcbOptFlds a = new CmsMsvcbOptFlds();
        a.refresh_time.value(true);
        a.sample_rate.value(true);
        byte[] encoded = a.encode();
        CmsMsvcbOptFlds b = new CmsMsvcbOptFlds();
        b.decode(encoded);
        assertTrue(b.refresh_time.value());
        assertTrue(b.sample_rate.value());
    }
}
