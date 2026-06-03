package com.ysh.jcms.datatypes.type;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.per.io.PerOutputStream;

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
        if (CmsFFIDatatypes.isAvailable()) {
            return ffiEncode(this::ffiEncode);
        }
        PerOutputStream pos = new PerOutputStream();
        perEncode(pos);
        return pos.toByteArray();
    }

    protected abstract int ffiEncode(byte[] buf, IntByReference outLen);

    /**
     * Java PER encode fallback — writes PER-encoded data to {@code pos}.
     * Invoked when FFI library is unavailable. Default throws.
     */
    protected void perEncode(PerOutputStream pos) {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no Java PER encode fallback");
    }

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
