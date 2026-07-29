package com.ysh.jcms.data.sequence.control;

import com.ysh.jcms.data.enumerate.CmsOrCat;
import com.ysh.jcms.data.sequence.control.CmsOriginator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsOriginatorTest {
    @Test
    public void roundup() {
        CmsOriginator a = new CmsOriginator();
        a.orCat.value(CmsOrCat.BAY_CONTROL);
        a.orIdent("testIdent".getBytes());
        byte[] encoded = a.encode();
        CmsOriginator b = new CmsOriginator();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
