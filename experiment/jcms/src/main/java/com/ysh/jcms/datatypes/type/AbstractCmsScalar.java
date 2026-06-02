package com.ysh.jcms.datatypes.type;

import com.sun.jna.ptr.IntByReference;

public abstract class AbstractCmsScalar<V> extends AbstractCmsType implements CmsScalar<V> {

    protected V value;

    protected AbstractCmsScalar(String typeName, V defaultValue) {
        super(typeName);
        this.value = defaultValue;
        this.present = false;
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    public void set(V value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        this.value = value;
        this.present = true;
    }

    @SuppressWarnings("unchecked")
    protected <T extends AbstractCmsScalar<V>> T copyTo(T clone) {
        clone.value = this.value;
        clone.present = this.present;
        return clone;
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        doEncode(buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    protected abstract void doEncode(byte[] buf, IntByReference outLen);
}
