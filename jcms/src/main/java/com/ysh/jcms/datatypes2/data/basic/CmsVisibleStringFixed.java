package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * VisibleStringFixed — maps to C {@code cms_visible_string_fixed_t}.
 * Fixed length, no length prefix in PER.
 */
public class CmsVisibleStringFixed extends CmsStructure {
    public byte[] data;

    public CmsVisibleStringFixed() { this.data = new byte[1]; }
    public CmsVisibleStringFixed(int structSize) { this.data = new byte[structSize]; }
    public CmsVisibleStringFixed(int structSize, String value) {
        this.data = new byte[structSize];
        set(value);
    }

    public String get() {
        int len = 0;
        while (len < data.length && data[len] != 0) len++;
        return new String(data, 0, len, StandardCharsets.US_ASCII);
    }
    public void set(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
        Arrays.fill(data, (byte) 0);
        System.arraycopy(bytes, 0, data, 0, Math.min(bytes.length, data.length));
    }

    @Override protected List<String> getFieldOrder() { return Arrays.asList("data"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_visible_string_fixed_encode(this, data.length, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_visible_string_fixed_decode(data, data.length, this.data.length, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsVisibleStringFixed from(byte[] data, int fixedLen) { return new CmsVisibleStringFixed(fixedLen).decode(data); }
    @Override public String toString() { return get(); }
}
