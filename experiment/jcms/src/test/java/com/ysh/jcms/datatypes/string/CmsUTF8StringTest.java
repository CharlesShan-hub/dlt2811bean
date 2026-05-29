package com.ysh.jcms.datatypes.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsUTF8String")
class CmsUTF8StringTest {

    @Test
    void ascii() {
        byte[] data = new CmsUTF8String("Hello").encode();
        CmsUTF8String r = CmsUTF8String.decode(data);
        assertEquals("Hello", r.get());
    }

    @Test
    void chinese() {
        byte[] data = new CmsUTF8String("UTF-8测试").encode();
        CmsUTF8String r = CmsUTF8String.decode(data);
        assertEquals("UTF-8测试", r.get());
    }

    @Test
    void empty() {
        byte[] data = new CmsUTF8String("").encode();
        CmsUTF8String r = CmsUTF8String.decode(data);
        assertEquals("", r.get());
    }

    @Test
    void defaultValue() {
        assertEquals("", new CmsUTF8String().get());
    }

    @Test
    void copy() {
        CmsUTF8String original = new CmsUTF8String("UTF-8测试");
        CmsUTF8String cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }

    @Test
    void roundtrip() {
        CmsUTF8String original = new CmsUTF8String("UTF-8测试");
        byte[] data = original.encode();
        CmsUTF8String decoded = CmsUTF8String.decode(data);
        assertEquals(original.get(), decoded.get());
    }
}
