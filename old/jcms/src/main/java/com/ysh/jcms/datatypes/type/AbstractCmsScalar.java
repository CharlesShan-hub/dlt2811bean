package com.ysh.jcms.datatypes.type;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;

public abstract class AbstractCmsScalar<T extends AbstractCmsScalar<T, V>, V>
        implements CmsScalar<T, V> {

    protected final String typeName;
    protected boolean optional = false;
    protected boolean present = true;
    protected V value;

    protected AbstractCmsScalar(String typeName, V defaultValue) {
        this.typeName = typeName;
        this.value = defaultValue;
        this.present = false;
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    @Override
    public boolean isOptional() { return optional; }

    @Override
    public void setOptional(boolean optional) { this.optional = optional; }

    @Override
    public boolean isPresent() { return present; }

    @Override
    public void setPresent(boolean present) { this.present = present; }

    // ==================== Value ====================

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

    // ==================== Encode ====================

    @Override
    public byte[] encode() {
        if (CmsFFIDatatypes.isAvailable()) {
            return ffiEncode(this::ffiEncode);
        }
        PerOutputStream pos = new PerOutputStream();
        perEncode(pos);
        return pos.toByteArray();
    }

    @FunctionalInterface
    public interface FfiEncoder {
        int encode(byte[] buf, IntByReference outLen);
    }

    protected int encodeBufSize() {
        return 16;
    }

    protected byte[] ffiEncode(int bufSize, FfiEncoder encoder) {
        byte[] buf = new byte[bufSize];
        IntByReference outLen = new IntByReference(buf.length);
        encoder.encode(buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    protected byte[] ffiEncode(FfiEncoder encoder) {
        return ffiEncode(encodeBufSize(), encoder);
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

    // ==================== Decode ====================

    /**
     * FFI decode: decode raw data and set this.value.
     * Override in subclasses with FFI support.
     */
    protected void ffiDecode(byte[] data) {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no FFI decode");
    }

    /**
     * Java PER decode fallback — reads PER-encoded data from {@code pis}
     * and sets this.value. Invoked when FFI library is unavailable.
     */
    protected void perDecode(PerInputStream pis) {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no Java PER decode fallback");
    }

    /**
     * Decode raw data into this instance. Returns self for chaining.
     */
    @SuppressWarnings("unchecked")
    public T decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            ffiDecode(data);
        } else {
            perDecode(new PerInputStream(data));
        }
        return (T) this;
    }

    // ==================== Copy ====================

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
