package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerBitString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 BIT STRING type (§7.1.5, Table 6).
 *
 * <pre>
 * ┌─────────────────┬──────────────────────────┬───────────┬───────────┐
 * │ 2811            │ Range                   │ Bits      │ Java type │
 * ├─────────────────┼──────────────────────────┼───────────┼───────────┤
 * │ BIT STRING      │ arbitrary bit pattern   │ variable  │ byte[]    │
 * └─────────────────┴──────────────────────────┴───────────┴───────────┘
 * </pre>
 *
 * <p>Supports two constraint modes via {@code size} (fixed) or {@code max} (variable).
 * Mutually exclusive — setting one clears the other.
 * <ul>
 *   <li><b>FIXED</b> (SIZE(n)): exactly n bits, no length field</li>
 *   <li><b>VARIABLE</b> (SIZE(0..max)): length prefix encoded as constrained integer</li>
 * </ul>
 *
 * <p>For fixed-size bit strings ≤ 64 bits, a {@code longValue} convenience field is provided.
 *
 * <pre>
 * // Bean mode — fixed 10-bit, chain setters
 * CmsBitString opts = new CmsBitString(0b0000001011L, 10)
 *     .setSize(10);
 * CmsBitString.encode(pos, opts);
 *
 * // Bean mode — variable, chain setters
 * CmsBitString packed = new CmsBitString(new byte[]{(byte)0xAB}, 8)
 *     .setMax(65535);
 * CmsBitString.encode(pos, packed);
 *
 * // Quick mode — raw value + explicit Mode
 * CmsBitString.encode(pos, 0b0000001011L, Mode.FIXED, 10);
 * CmsBitString.decode(pis, Mode.FIXED, 10);
 * CmsBitString.encode(pos, new byte[]{0xAB}, 8, Mode.VARIABLE, 65535);
 * CmsBitString.decode(pis, Mode.VARIABLE, 65535);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsBitString {

    /** Encoding mode: FIXED for SIZE(n), VARIABLE for SIZE(0..max). */
    public enum Mode {
        /** Fixed-size: SIZE(n), exactly n bits, no length field. */
        FIXED,
        /** Variable-size: SIZE(0..max), length prefix encoded. */
        VARIABLE
    }

    private byte[] value;
    private int bitLength;
    private Integer size;
    private Integer max;

    public CmsBitString() {
        this.value = new byte[0];
        this.bitLength = 0;
    }

    /**
     * Constructor with long value (for ≤ 64 bits).
     * @param longValue bit pattern stored in low bits
     * @param bitLength number of valid bits
     */
    public CmsBitString(long longValue, int bitLength) {
        this.bitLength = bitLength;
        this.value = longToBytes(longValue, bitLength);
    }

    /**
     * Constructor with byte array.
     * @param value    bit data
     * @param bitLength number of valid bits
     */
    public CmsBitString(byte[] value, int bitLength) {
        this.value = value != null ? value : new byte[0];
        this.bitLength = bitLength;
    }

    /** Set fixed size (SIZE(n)). Clears max. */
    public CmsBitString size(int size) {
        this.size = size;
        this.max = null;
        return this;
    }

    /** Set variable max (SIZE(0..max)). Clears size. */
    public CmsBitString max(int max) {
        this.max = max;
        this.size = null;
        return this;
    }

    /** Returns the effective mode based on which constraint is set. */
    public Mode getMode() {
        if (size != null) return Mode.FIXED;
        if (max != null) return Mode.VARIABLE;
        return null;
    }

    /** Validates that a constraint (size or max) is set. Throws if neither is set. */
    public void validate() {
        if (size == null && max == null) {
            throw new IllegalStateException("BIT STRING constraint not set: call size(n) or max(n)");
        }
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsBitString bean. Uses bean's internal size/max constraint. */
    public static void encode(PerOutputStream pos, CmsBitString bean) {
        bean.validate();
        if (bean.size != null) {
            if (bean.bitLength <= 64) {
                PerBitString.encodeFixedSize(pos, bytesToLong(bean.value, bean.bitLength), bean.size);
            } else {
                PerBitString.encodeFixedSize(pos, bean.value, bean.size);
            }
        } else {
            PerBitString.encodeConstrained(pos, bean.value, bean.bitLength, 0, bean.max);
        }
    }

    /** Encodes a fixed-size bit string from long (≤ 64 bits). */
    public static void encode(PerOutputStream pos, long longValue, Mode mode, int bits) {
        if (mode == Mode.FIXED) {
            PerBitString.encodeFixedSize(pos, longValue, bits);
        } else {
            byte[] data = longToBytes(longValue, bits);
            PerBitString.encodeConstrained(pos, data, bits, 0, bits);
        }
    }

    /** Encodes a byte-array bit string with explicit mode. */
    public static void encode(PerOutputStream pos, byte[] value, int bitLength, Mode mode, int length) {
        if (mode == Mode.FIXED) {
            PerBitString.encodeFixedSize(pos, value, length);
        } else {
            PerBitString.encodeConstrained(pos, value, bitLength, 0, length);
        }
    }

    /** Decodes a fixed-size bit string as long (≤ 64 bits). */
    public static CmsBitString decode(PerInputStream pis, Mode mode, int length) throws PerDecodeException {
        if (mode == Mode.FIXED) {
            long val = PerBitString.decodeFixedSize(pis, length);
            return new CmsBitString(val, length).size(length);
        } else {
            byte[] data = PerBitString.decodeConstrained(pis, 0, length);
            int bits = data.length * 8;
            return new CmsBitString(data, bits).max(length);
        }
    }

    /** Decodes a fixed-size bit string as byte array (> 64 bits or explicit). */
    public static CmsBitString decodeBytes(PerInputStream pis, Mode mode, int length) throws PerDecodeException {
        if (mode == Mode.FIXED) {
            byte[] data = PerBitString.decodeFixedSizeBytes(pis, length);
            return new CmsBitString(data, length).size(length);
        } else {
            byte[] data = PerBitString.decodeConstrained(pis, 0, length);
            int bits = data.length * 8;
            return new CmsBitString(data, bits).max(length);
        }
    }

    /** Convenience: get the long value (only valid for ≤ 64 bits). */
    public long getLongValue() {
        return bytesToLong(value, bitLength);
    }

    @Override
    public String toString() {
        if (bitLength <= 64) {
            return String.format("0b%s (%d bits)", Long.toBinaryString(bytesToLong(value, bitLength)), bitLength);
        }
        return String.format("[%d bits]", bitLength);
    }

    // ==================== Internal ====================

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

    private static byte[] longToBytes(long value, int totalBits) {
        if (totalBits == 0) return new byte[0];
        int bytesNeeded = (totalBits + 7) / 8;
        byte[] result = new byte[bytesNeeded];
        for (int i = bytesNeeded - 1; i >= 0; i--) {
            result[bytesNeeded - 1 - i] = (byte) ((value >> (i * 8)) & 0xFF);
        }
        return result;
    }
}
