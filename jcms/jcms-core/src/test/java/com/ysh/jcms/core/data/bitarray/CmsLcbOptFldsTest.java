package com.ysh.jcms.core.data.bitarray;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsLcbOptFldsTest {
    @Test
    public void roundup() {
        CmsLcbOptFlds a = new CmsLcbOptFlds().bit0(true);
        byte[] encoded = a.encode();
        CmsLcbOptFlds b = new CmsLcbOptFlds();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
