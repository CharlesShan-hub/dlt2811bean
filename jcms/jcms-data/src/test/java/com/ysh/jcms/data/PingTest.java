// Simple diagnostic test for native library loading.
package com.ysh.jcms.data;

import org.junit.Test;
import static org.junit.Assert.*;

public class PingTest {
    @Test
    public void testPing() {
        String result = CmsNative.ping();
        assertEquals("pong", result);
    }

    @Test
    public void testEncodeInt32() {
        byte[] data = CmsNative.encode("Int32", "uper", "42");
        assertNotNull(data);
        assertTrue(data.length > 0);
    }

    @Test
    public void testDecodeInt32() {
        byte[] data = CmsNative.encode("Int32", "uper", "42");
        String json = CmsNative.decode("Int32", "uper", data);
        assertNotNull(json);
        System.out.println("Decoded JSON: " + json);
    }
}
