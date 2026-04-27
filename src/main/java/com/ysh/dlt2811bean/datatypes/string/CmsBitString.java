package com.ysh.dlt2811bean.datatypes.string;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsString;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerBitString;
import lombok.Getter;

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
 *     .size(10);
 * opts.encode(pos);
 *
 * // Decode (returns self for chaining) — must set size or max first
 * CmsBitString r = new CmsBitString().size(10).decode(pis);
 * byte[] bits = r.get();
 *
 * // Or use static read method
 * CmsBitString r2 = CmsBitString.read(pis, Mode.FIXED, 10);
 * </pre>
 */
@Getter
public class CmsBitString extends AbstractCmsString<CmsBitString, byte[]> {

    /**
     * -- GETTER --
     * Get the bit length.
     */
    private int bitLength;

    public CmsBitString() {
        this("BIT STRING", new byte[0], 0);
    }

    /**
     * Constructor with long value (for ≤ 64 bits).
     * @param longValue bit pattern stored in low bits
     * @param bitLength number of valid bits
     */
    public CmsBitString(long longValue, int bitLength) {
        this("BIT STRING", longToBytes(longValue, bitLength), bitLength);
    }

    public CmsBitString(byte[] value, int bitLength) {
        this("BIT STRING", value, bitLength);
    }

    public CmsBitString(String typeName, byte[] value, int bitLength) {
        super(typeName, value != null ? value : new byte[0]);
        this.bitLength = bitLength;
    }

    /** Set the bit length. */
    public CmsBitString bitLength(int bitLength) {
        this.bitLength = bitLength;
        return this;
    }

    @Override
    public CmsBitString set(byte[] value) {
        super.set(value);
        if (value != null) {
            this.bitLength = value.length * 8;
        } else {
            this.bitLength = 0;
        }
        return this;
    }

    @Override
    protected void encodeFixedSize(PerOutputStream pos) {
        PerBitString.encodeFixedSize(pos, get(), size);
    }

    @Override
    protected void encodeConstrained(PerOutputStream pos) {
        PerBitString.encodeConstrained(pos, get(), bitLength, 0, max);
    }

    @Override
    protected byte[] decodeValueFixedSize(PerInputStream pis) throws Exception {
        long longValue = PerBitString.decodeFixedSize(pis, size);
        this.bitLength = size;
        return longToBytes(longValue, size);
    }

    @Override
    protected byte[] decodeValueConstrained(PerInputStream pis) throws Exception {
        byte[] result = PerBitString.decodeConstrained(pis, 0, max);
        this.bitLength = result.length * 8;
        return result;
    }

    /** Static write with raw value and explicit mode. */
    public static void write(PerOutputStream pos, byte[] value, int bitLength, Mode mode, int length) {
        if (mode == Mode.FIXED) {
            PerBitString.encodeFixedSize(pos, value, length);
        } else {
            PerBitString.encodeConstrained(pos, value, bitLength, 0, length);
        }
    }

    /** Static decode with explicit mode. */
    public static CmsBitString read(PerInputStream pis, Mode mode, int length) throws Exception {
        CmsBitString result = new CmsBitString();
        if (mode == Mode.FIXED) {
            result.size(length);
        } else {
            result.max(length);
        }
        return result.decode(pis);
    }

    /** Convenience: get the long value (only valid for ≤ 64 bits). */
    public long getLongValue() {
        return bytesToLong(get(), bitLength);
    }

    @Override
    public CmsBitString copy() {
        CmsBitString clone = new CmsBitString(get().clone(), bitLength);
        if (size != null && size != 0) clone.size(size);
        if (max != null && max != 0) clone.max(max);
        return clone;
    }

    @Override
    public String toString() {
        if (bitLength <= 64) {
            return String.format("BIT STRING: 0b%s (%d bits)", Long.toBinaryString(bytesToLong(get(), bitLength)), bitLength);
        }
        return String.format("BIT STRING: [%d bits]", bitLength);
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

    private static byte[] longToBytes(long value, int bitLength) {
        if (bitLength == 0) return new byte[0];
        int byteLength = (bitLength + 7) / 8;
        byte[] bytes = new byte[byteLength];
        for (int i = byteLength - 1; i >= 0; i--) {
            bytes[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        if (bitLength % 8 != 0) {
            int shift = 8 - (bitLength % 8);
            bytes[0] &= 0xFF >>> shift;
        }
        return bytes;
    }
}
