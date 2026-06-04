package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

/** VisibleString fixed-length (no PER length prefix). C: {@code char[N]}. */
public class CmsVisibleStringFix extends Structure {
    public byte[] data;
    public CmsVisibleStringFix() { this.data = new byte[1]; }
    public CmsVisibleStringFix(int structSize) { this.data = new byte[structSize]; }
    public CmsVisibleStringFix(int structSize, String value) { this.data = new byte[structSize]; set(value); }
    public String get() { int n = 0; while (n < data.length && data[n] != 0) n++; return new String(data, 0, n, java.nio.charset.StandardCharsets.US_ASCII); }
    public void set(String v) { byte[] b = v.getBytes(java.nio.charset.StandardCharsets.US_ASCII); Arrays.fill(data, (byte)0); System.arraycopy(b, 0, data, 0, Math.min(b.length, data.length)); }
    @Override protected List<String> getFieldOrder() { return Arrays.asList("data"); }
    @Override public String toString() { return get(); }
}
