package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerBitString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 PACKED LIST type — generic bean (§7.1.8).
 *
 * <p>A PACKED LIST is a variable-length bit string where each bit represents
 * a named flag/semantics defined by the data model. Bit 0 is the first member.
 *
 * <p>Semantically different from {@link CmsCodedEnum}:
 * <ul>
 *   <li><b>CmsCodedEnum</b> — fixed-size, always n bits</li>
 *   <li><b>CmsPackedList</b> — variable-size, 0..max bits, length prefix encoded</li>
 * </ul>
 *
 * <pre>
 * ┌─────────────────┬───────────────────────┬───────────┬───────────┐
 * │ 2811            │ Range                │ Bits      │ Java type │
 * ├─────────────────┼───────────────────────┼───────────┼───────────┤
 * │ PACKED LIST     │ 0..n bits (variable) │ variable  │ long      │
 * └─────────────────┴───────────────────────┴───────────┴───────────┘
 * </pre>
 *
 * <pre>
 * // Bean mode — chain setters
 * CmsPackedList list = new CmsPackedList()
 *     .setValue(0b00000101L)
 *     .setBitLength(4)
 *     .setMax(8);
 * CmsPackedList.encode(pos, list);
 *
 * // Check individual bits
 * list.testBit(0);  // bit0 is set?
 * list.testBit(1);  // bit1 is set?
 *
 * // Quick mode — raw value + explicit bitLength and max
 * CmsPackedList.encode(pos, 0b00000101L, 4, 8);
 * CmsPackedList.decode(pis, 8);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsPackedList {

    /** Bit pattern stored in low bits of a long. */
    private long value;
    /** Actual bit count (how many bits are actually used). */
    private Integer bitLength;
    /** Maximum bit count (SIZE(0..max)). Must be set before encode/decode. */
    private Integer max;

    public CmsPackedList() {
        this.value = 0L;
    }

    public CmsPackedList(long value, int bitLength, int max) {
        this.value = value;
        this.bitLength = bitLength;
        this.max = max;
    }

    /**
     * Validates that max and bitLength are set, and value fits within the bit width.
     *
     * @throws IllegalStateException if max or bitLength is null
     * @throws IllegalArgumentException if value exceeds the bit width
     */
    public void validate() {
        if (max == null) {
            throw new IllegalStateException("max is not set");
        }
        if (bitLength == null) {
            throw new IllegalStateException("bitLength is not set");
        }
        if (max < 0 || max > 64) {
            throw new IllegalArgumentException("max must be 0..64, got: " + max);
        }
        if (bitLength < 0 || bitLength > max) {
            throw new IllegalArgumentException(
                    String.format("bitLength must be 0..%d, got: %d", max, bitLength));
        }
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative, got: " + value);
        }
        if (bitLength > 0 && value >= (1L << bitLength)) {
            throw new IllegalArgumentException(
                    String.format("value 0x%X exceeds %d-bit width", value, bitLength));
        }
    }

    /** Tests whether the bit at the given position (0-based, LSB-first) is set. */
    public boolean testBit(int pos) {
        return (value & (1L << pos)) != 0;
    }

    /** Sets the bit at the given position (0-based, LSB-first). */
    public CmsPackedList setBit(int pos) {
        value |= (1L << pos);
        // Auto-extend bitLength if needed
        if (bitLength != null && pos >= bitLength) {
            bitLength = pos + 1;
        }
        return this;
    }

    /** Clears the bit at the given position (0-based, LSB-first). */
    public CmsPackedList clearBit(int pos) {
        value &= ~(1L << pos);
        return this;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsPackedList bean. Writes: [bitLength constrained 0..max][bit content]. */
    public static void encode(PerOutputStream pos, CmsPackedList bean) {
        bean.validate();
        PerBitString.encodeConstrained(pos, longToBytes(bean.value, bean.bitLength),
                bean.bitLength, 0, bean.max);
    }

    /** Encodes a raw long value with explicit bitLength and max. */
    public static void encode(PerOutputStream pos, long value, int bitLength, int max) {
        PerBitString.encodeConstrained(pos, longToBytes(value, bitLength), bitLength, 0, max);
    }

    /** Decodes with explicit max bit count. Returns bean with actual bitLength. */
    public static CmsPackedList decode(PerInputStream pis, int max) throws PerDecodeException {
        byte[] data = PerBitString.decodeConstrained(pis, 0, max);
        int actualBits = data.length * 8;
        return new CmsPackedList(bytesToLong(data, actualBits), actualBits, max);
    }

    @Override
    public String toString() {
        if (max == null) return "PackedList[?] = 0x" + Long.toHexString(value).toUpperCase();
        if (bitLength == null) return String.format("PackedList[0..%d] = 0x%X", max, value);
        return String.format("PackedList[%d/0..%d] = 0b%s", bitLength, max,
                bitLength > 0 ? Long.toBinaryString(value | (1L << bitLength)).substring(1) : "0");
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
