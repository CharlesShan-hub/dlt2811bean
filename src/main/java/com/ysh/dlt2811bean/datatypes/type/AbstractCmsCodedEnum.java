package com.ysh.dlt2811bean.datatypes.type;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerBitString;

public abstract class AbstractCmsCodedEnum<T extends AbstractCmsCodedEnum<T>> extends AbstractCmsScalar<T, Long> implements CmsCodedEnum<T> {

    /** Fixed bit count. Must be set before encode/decode. */
    private final Integer size;

    public AbstractCmsCodedEnum(String typeName, long value, int size) {
        super(typeName, value);
        this.size = size;
    }

    // ==================== Public API ====================

    @Override
    public T set(Long value) {
        super.set(value);
        validate();
        return self();
    }

    /** Tests whether the bit at the given position (0-based, LSB-first) is set. */
    @Override
    public boolean testBit(int pos) {
        checkBitPos(pos);
        return (value & (1L << pos)) != 0;
    }

    /** Sets or clears the bit at the given position (0-based, LSB-first). */
    @Override
    public T setBit(int pos, boolean value) {
        checkBitPos(pos);
        if (value) this.value |= (1L << pos);
        else       this.value &= ~(1L << pos);
        this.present = true;
        return self();
    }

    private void checkBitPos(int pos) {
        if (pos < 0 || pos >= size) {
            throw new IllegalArgumentException("bit position out of range [0, " + (size - 1) + "]: " + pos);
        }
    }

    @Override
    public long getBits(int pos, int width) {
        long mask = (1L << width) - 1;
        return (value >>> pos) & mask;
    }

    @Override
    public boolean testBits(int pos, int width, int fieldValue) {
        long mask = (1L << width) - 1;
        return ((value >>> pos) & mask) == (fieldValue & mask);
    }

    /**
     * Sets a multi-bit field starting at pos (LSB-first), width bits wide.
     * For example, setBits(0, 2, 3) sets bits 0~1 to value 3 (binary 11).
     */
    @Override
    public T setBits(int pos, int width, int fieldValue) {
        long mask = (1L << width) - 1;
        value &= ~(mask << pos);
        value |= ((long) (fieldValue & mask) << pos);
        this.present = true;
        return self();
    }

    // ==================== Encode / Decode ====================

    @Override
    public void encode(PerOutputStream pos) {
        PerBitString.encodeFixedSize(pos, value, size);
    }

    @Override
    public T decode(PerInputStream pis) throws PerDecodeException {
        set(PerBitString.decodeFixedSize(pis, size));
        return self();
    }

    // ==================== Private Helpers ====================

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