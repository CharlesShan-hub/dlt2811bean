package com.ysh.jcms.datatypes.type;

import com.sun.jna.ptr.IntByReference;

public abstract class AbstractCmsScalar<T extends AbstractCmsScalar<T, V>, V>
        extends AbstractCmsType<T> implements CmsScalar<T, V> {

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

    @Override
    public byte[] encode() {
        return ffiEncode(this::ffiEncode);
    }

    protected abstract int ffiEncode(byte[] buf, IntByReference outLen);

    @Override
    @SuppressWarnings("unchecked")
    public T copy() {
        try {
            AbstractCmsScalar<T, V> clone = (AbstractCmsScalar<T, V>) getClass().getDeclaredConstructor().newInstance();
            clone.value = this.value;
            clone.present = this.present;
            return (T) clone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy " + typeName, e);
        }
    }

    @Override
    public String toString() {
        return "(" + getClass().getSimpleName() + ") " + value;
    }
}
