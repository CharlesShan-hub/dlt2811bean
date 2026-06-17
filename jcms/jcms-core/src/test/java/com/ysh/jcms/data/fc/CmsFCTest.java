package com.ysh.jcms.data.fc;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsFCTest {
    @Test
    public void roundup() {
        CmsFC a = new CmsFC(CmsFC.ST);
        byte[] encoded = a.encode();
        CmsFC b = new CmsFC();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundup_mx() {
        CmsFC a = new CmsFC(CmsFC.MX);
        byte[] encoded = a.encode();
        CmsFC b = new CmsFC();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void roundup_xx() {
        CmsFC a = new CmsFC(CmsFC.XX);
        byte[] encoded = a.encode();
        CmsFC b = new CmsFC();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
