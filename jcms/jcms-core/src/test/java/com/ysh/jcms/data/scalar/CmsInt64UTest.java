package com.ysh.jcms.data.scalar;

import org.junit.Test;
import java.math.BigInteger;
import static org.junit.Assert.*;

public class CmsInt64UTest {
    @Test
    public void roundup() {
        BigInteger val = new BigInteger("12345678901234567890");
        CmsInt64U a = new CmsInt64U(val);
        byte[] encoded = a.encode();
        CmsInt64U b = new CmsInt64U();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
