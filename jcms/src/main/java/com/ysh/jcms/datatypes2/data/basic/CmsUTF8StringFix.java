package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

/** UTF8String fixed-length (no PER length prefix). C: {@code uint8_t[N]}. */
public class CmsUTF8StringFix extends Structure {
    public byte[] data;
    public CmsUTF8StringFix() { this.data = new byte[1]; }
    public CmsUTF8StringFix(int structSize) { this.data = new byte[structSize]; }
    public CmsUTF8StringFix(int structSize, String value) { this.data = new byte[structSize]; set(value); }
    public String get() { int n = 0; while (n < data.length && data[n] != 0) n++; return new String(data, 0, n, java.nio.charset.StandardCharsets.UTF_8); }
    public void set(String v) { byte[] b = v.getBytes(java.nio.charset.StandardCharsets.UTF_8); Arrays.fill(data, (byte)0); System.arraycopy(b, 0, data, 0, Math.min(b.length, data.length)); }
    @Override protected List<String> getFieldOrder() { return Arrays.asList("data"); }
    @Override public String toString() { return get(); }
}
