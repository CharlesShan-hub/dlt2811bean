package com.ysh.jcms2.data.enumerated;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.core.NativeBridge;

/**
 * Enumerated ::= Int8 (-128..127)  —  7.1.6
 * PER: constrained integer, 8 bits
 * sizeof = 4
 *
 * Base class for all ENUMERATED / coded-enum types:
 *   CmsDbpos, CmsTcmd, CmsServiceError, CmsOrCat, etc.
 */
public class CmsEnumerated extends CmsType {

    private int value;

    public CmsEnumerated() {}
    public CmsEnumerated(int value) { this.value = value; write(); }

    public int value() { return value; }
    public CmsEnumerated value(int v) { this.value = v; write(); return this; }

    @Override
    protected int calcNativeSize() { return 4; }

    @Override
    public void write() { nativePtr.setInt(0, value); }

    @Override
    public void read() { this.value = nativePtr.getInt(0); }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeEnumerated(nativePtr); }

    @Override
    public void decode(byte[] data) { NativeBridge.decodeEnumerated(nativePtr, data); read(); }
}
