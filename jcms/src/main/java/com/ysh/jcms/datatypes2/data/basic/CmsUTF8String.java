package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.Structure;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * UTF8String as a Structure — maps to C {@code uint8_t[N]}.
 */
public class CmsUTF8String extends Structure {
    public byte[] data;

    public CmsUTF8String() { this.data = new byte[1]; }
    public CmsUTF8String(int structSize) { this.data = new byte[structSize]; }
    public CmsUTF8String(int structSize, String value) {
        this.data = new byte[structSize];
        set(value);
    }

    public String get() {
        int len = 0;
        while (len < data.length && data[len] != 0) len++;
        return new String(data, 0, len, StandardCharsets.UTF_8);
    }
    public void set(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        Arrays.fill(data, (byte) 0);
        System.arraycopy(bytes, 0, data, 0, Math.min(bytes.length, data.length));
    }

    @Override protected List<String> getFieldOrder() { return Arrays.asList("data"); }
    @Override public String toString() { return get(); }
}
