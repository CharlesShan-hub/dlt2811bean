package com.ysh.jcms.datatypetest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTestPair")
class CmsTestPairTest {

    @Test
    void roundtrip() {
        CmsTestPair original = new CmsTestPair();
        original.a().value(123);
        original.b().value(-456);

        byte[] data = original.encode();
        System.out.println("encoded " + data.length + " bytes");

        CmsTestPair decoded = new CmsTestPair().decode(data);
        assertEquals(123, decoded.a().value());
        assertEquals(-456, decoded.b().value());
    }
}
