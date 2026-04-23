package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerBitString;

public abstract class AbstractCmsCodedEnum<T extends AbstractCmsCodedEnum<T>> implements CmsCodedEnum<T> {

    /** Bit pattern stored in low bits of a long. */
    private long value;
    /** Fixed bit count. Must be set before encode/decode. */
    private Integer size;

    public AbstractCmsCodedEnum(long value, int size) {
        this.value = value;
        this.size = size;
    }

    /** Tests whether the bit at the given position (0-based, LSB-first) is set. */
    @Override
    public boolean testBit(int pos) {
        return (value & (1L << pos)) != 0;
    }

    /** Sets or clears the bit at the given position (0-based, LSB-first). */
    @Override
    public T setBit(int pos, boolean value) {
        if (value) this.value |= (1L << pos);
        else       this.value &= ~(1L << pos);
        return self();
    }

    /**
     * Gets a multi-bit field starting at pos (LSB-first), width bits wide.
     * For example, getBits(0, 2) extracts bits 0~1 as a 2-bit value.
     */
    @Override
    public long getBits(int pos, int width) {
        long mask = (1L << width) - 1;
        return (Long) ((value >>> pos) & mask);
    }

    /**
     * Sets a multi-bit field starting at pos (LSB-first), width bits wide.
     * For example, setBits(0, 2, 3) sets bits 0~1 to value 3 (binary 11).
     */
    @Override
    public T setBits(int pos, int width, int fieldValue) {
        long mask = (1L << width) - 1;
        value &= ~(mask << pos);            // clear the field
        value |= ((long) (fieldValue & mask) << pos);  // set the new value
        return self();
    }

    // ==================== Encode / Decode ====================

    @Override
    public T set(Long value) {
        this.value = value;
        validate();
        return self();
    }

    @Override
    public Long get() {
        return value;
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerBitString.encodeFixedSize(pos, value, size);
    }

    @Override
    public T decode(PerInputStream pis) throws PerDecodeException {
        set(PerBitString.decodeFixedSize(pis, size));
        return self();
    }

    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

    private void validate() {
        if (size == null) {
            throw new IllegalStateException("size is not set");
        }
        if (size < 0 || size > 64) {
            throw new IllegalArgumentException("size must be 0..64, got: " + size);
        }
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative, got: " + value);
        }
        if (size > 0 && value >= (1L << size)) {
            throw new IllegalArgumentException(
                    String.format("value 0x%X exceeds %d-bit width", value, size));
        }
    }
}