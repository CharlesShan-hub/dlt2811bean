package com.ysh.jcms.core.data.bitarray;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsCheckTest {
    @Test
    public void roundup() {
        CmsCheck a = new CmsCheck().syncheck(true).interlock_check(false);
        byte[] encoded = a.encode();
        CmsCheck b = new CmsCheck();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
