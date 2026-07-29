package com.ysh.jcms.data.bitarray;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsReasonCodeTest {
    @Test
    public void roundup() {
        CmsReasonCode a = new CmsReasonCode().data_change(true).general_interrogation(true);
        byte[] encoded = a.encode();
        CmsReasonCode b = new CmsReasonCode();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
