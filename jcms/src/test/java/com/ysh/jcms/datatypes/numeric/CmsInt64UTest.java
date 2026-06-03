package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64U")
class CmsInt64UTest {

    @Test
    void positive() {
        byte[] data = new CmsInt64U(BigInteger.valueOf(18000000000000L)).encode();
        CmsInt64U r = CmsInt64U.decode(data);
        assertEquals(0, BigInteger.valueOf(18000000000000L).compareTo(r.get()));
    }

    @Test
    void largeValue() {
        BigInteger large = new BigInteger("12345678901234567890");
        byte[] data = new CmsInt64U(large).encode();
        CmsInt64U r = CmsInt64U.decode(data);
        assertEquals(0, large.compareTo(r.get()));
    }

    @Test
    void zero() {
        byte[] data = new CmsInt64U(BigInteger.ZERO).encode();
        CmsInt64U r = CmsInt64U.decode(data);
        assertEquals(0, BigInteger.ZERO.compareTo(r.get()));
    }

    @Test
    void roundtrip() {
        CmsInt64U original = new CmsInt64U(BigInteger.valueOf(18000000000000L));
        byte[] data = original.encode();
        CmsInt64U decoded = CmsInt64U.decode(data);
        assertEquals(0, original.get().compareTo(decoded.get()));
    }
}
