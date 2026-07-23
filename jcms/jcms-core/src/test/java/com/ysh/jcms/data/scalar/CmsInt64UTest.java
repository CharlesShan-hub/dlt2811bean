package com.ysh.jcms.data.scalar;

import org.junit.Test;
import java.math.BigInteger;
import static org.junit.Assert.*;

public class CmsInt64UTest {
    @Test
    public void roundup() {
        BigInteger val = new BigInteger("1234567890123456"); // < 2^63 (JER limitation)
        CmsInt64U a = new CmsInt64U(val);
        byte[] encoded = a.encode();
        CmsInt64U b = new CmsInt64U();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
