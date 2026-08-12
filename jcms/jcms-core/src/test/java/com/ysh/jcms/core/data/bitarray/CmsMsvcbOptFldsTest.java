package com.ysh.jcms.core.data.bitarray;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsMsvcbOptFldsTest {
    @Test
    public void roundup() {
        CmsMsvcbOptFlds a = new CmsMsvcbOptFlds().refresh_time(true).sample_rate(true);
        byte[] encoded = a.encode();
        CmsMsvcbOptFlds b = new CmsMsvcbOptFlds();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
