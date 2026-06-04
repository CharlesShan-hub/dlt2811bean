package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.Structure;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * VisibleString as a Structure — maps to C {@code char[N]}.
 *
 * <p>Use in Compounds: {@code public CmsVisibleString rptID = new CmsVisibleString(130);}
 */
public class CmsVisibleString extends Structure {
    public byte[] data;

    public CmsVisibleString() { this.data = new byte[1]; }
    public CmsVisibleString(int structSize) { this.data = new byte[structSize]; }
    public CmsVisibleString(int structSize, String value) {
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

    @Override public String toString() { return get(); }
}
