package com.ysh.dlt2811bean.datatypes.type;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerBitString;

/**
 * Abstract base for DL/T 2811 PACKED LIST type (§7.1.8).
 *
 * <p>Variable-length bit string (0..max bits) with length prefix encoded
 * as a constrained integer. Symmetric to {@link AbstractCmsCodedEnum}
 * which handles fixed-size bit strings.
 *
 * @param <T> the concrete type
 */
public abstract class AbstractCmsPackedList<T extends AbstractCmsPackedList<T>>
        extends AbstractCmsScalar<T, Long> implements CmsPackedList<T> {

    /** Maximum bit count (SIZE(0..max)). */
    private int max;

    /** Actual bit count (how many bits are actually used). */
    private int bitLength;

    protected AbstractCmsPackedList(String typeName, long value, int max) {
        super(typeName, value);
        this.max = max;
        this.bitLength = 0;
    }

    // ==================== Public API ====================

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public T setMax(int max) {
        this.max = max;
        return self();
    }

    @Override
    public int getBitLength() {
        return bitLength;
    }

    @Override
    public T set(Long value) {
        super.set(value);
        validate();
        return self();
    }

    /** Set the actual bit count (for use by subclasses in constructors). */
    protected void setBitLength(int bitLength) {
        this.bitLength = bitLength;
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
        if (value && pos >= bitLength) {
            bitLength = pos + 1;
        }
        return self();
    }

    private void checkBitPos(int pos) {
        if (pos < 0 || pos >= max) {
            throw new IllegalArgumentException(
                    "bit position out of range [0, " + (max - 1) + "]: " + pos);
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

    @Override
    public T setBits(int pos, int width, int fieldValue) {
        long mask = (1L << width) - 1;
        value &= ~(mask << pos);
        value |= ((long) (fieldValue & mask) << pos);
        this.present = true;
        int end = pos + width;
        if (end > bitLength) {
            bitLength = end;
        }
        return self();
    }

    // ==================== Encode / Decode ====================

    @Override
    public void encode(PerOutputStream pos) {
        validate();
        PerBitString.encodeConstrained(pos, longToBytes(value, bitLength), bitLength, 0, max);
    }

    @Override
    public T decode(PerInputStream pis) throws PerDecodeException {
        byte[] data = PerBitString.decodeConstrained(pis, 0, max);
        bitLength = data.length * 8;
        value = bytesToLong(data, bitLength);
        validate();
        return self();
    }

    // ==================== Validation ====================

    private void validate() {
        if (max < 0 || max > 64) {
            throw new IllegalArgumentException("max must be 0..64, got: " + max);
        }
        if (bitLength < 0 || bitLength > max) {
            throw new IllegalArgumentException(
                    "bitLength must be 0.." + max + ", got: " + bitLength);
        }
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative, got: " + value);
        }
        if (bitLength > 0 && value >= (1L << bitLength)) {
            throw new IllegalArgumentException(
                    String.format("value 0x%X exceeds %d-bit width", value, bitLength));
        }
    }

    // ==================== Internal ====================

    private static byte[] longToBytes(long value, int totalBits) {
        if (totalBits == 0) return new byte[0];
        int bytesNeeded = (totalBits + 7) / 8;
        byte[] result = new byte[bytesNeeded];
        for (int i = bytesNeeded - 1; i >= 0; i--) {
            result[bytesNeeded - 1 - i] = (byte) ((value >> (i * 8)) & 0xFF);
        }
        return result;
    }

    private static long bytesToLong(byte[] data, int totalBits) {
        if (data == null || data.length == 0 || totalBits == 0) return 0L;
        long result = 0;
        for (byte b : data) {
            result = (result << 8) | (b & 0xFFL);
        }
        if (totalBits % 8 != 0) {
            result &= (1L << totalBits) - 1;
        }
        return result;
    }
}