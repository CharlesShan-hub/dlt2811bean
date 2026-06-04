package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * OctetString as a Structure — maps to C {@code uint8_t[N]}.
 */
public class CmsOctetString extends Structure {
    public byte[] data;

    public CmsOctetString() { this.data = new byte[1]; }
    public CmsOctetString(int structSize) { this.data = new byte[structSize]; }
    public CmsOctetString(int structSize, byte[] value) {
        this.data = new byte[structSize];
        set(value);
    }
    public CmsOctetString(int structSize, String value) {
        this.data = new byte[structSize];
        byte[] bytes = value.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
        System.arraycopy(bytes, 0, data, 0, Math.min(bytes.length, data.length));
    }

    public byte[] get() { return data; }
    public void set(byte[] value) {
        Arrays.fill(data, (byte) 0);
        System.arraycopy(value, 0, data, 0, Math.min(value.length, data.length));
    }
    public String getString() { return new String(data, java.nio.charset.StandardCharsets.ISO_8859_1); }

    @Override protected List<String> getFieldOrder() { return Arrays.asList("data"); }
}
