package com.ysh.jcms.core;

import com.ysh.jcms.core.NativeBridge.Codec;

/**
 * Enumerated ::= Int8 (-128..127) — 7.1.6 PER: constrained integer, 8 bits
 * sizeof = 4
 *
 * Base class for all ENUMERATED / coded-enum types: CmsDbpos, CmsTcmd,
 * CmsServiceError, CmsOrCat, etc.
 */
public class CmsEnumerated extends CmsType {

    private int value;

    public CmsEnumerated() {
        super(Codec.ENUMERATED);
    }
    public CmsEnumerated(int value) {
        super(Codec.ENUMERATED);
        this.value = value;
        write();
    }

    /** Constructor with range validation. Subclasses pass (min, max, value). */
    public CmsEnumerated(int min, int max, int value) {
        super(Codec.ENUMERATED);
        if (value < min || value > max)
            throw new IllegalArgumentException(getClass().getSimpleName() + " out of range [" + min + "," + max + "]: " + value);
        this.value = value;
        write();
    }

    public int value() {
        return value;
    }
    public CmsEnumerated value(int v) {
        this.value = v;
        write();
        return this;
    }

    @Override
    protected int calcNativeSize() {
        return 4;
    }

    @Override
    public void write() {
        nativePtr.setInt(0, value);
    }

    @Override
    public void read() {
        this.value = nativePtr.getInt(0);
    }
}
