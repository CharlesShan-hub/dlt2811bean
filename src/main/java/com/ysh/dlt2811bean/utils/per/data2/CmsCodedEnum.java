package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerBitString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 CODED ENUM type — generic bean (§7.1.7).
 *
 * <p>A CODED ENUM is a fixed-size bit string where each bit represents
 * a named flag/semantics defined by the data model (DL/T 860.72).
 *
 * <p>Semantically different from {@link CmsBitString}:
 * <ul>
 *   <li><b>CmsBitString</b> — arbitrary bit pattern, interpreted as a whole</li>
 *   <li><b>CmsCodedEnum</b> — each bit is a named flag with specific meaning</li>
 * </ul>
 *
 * <p>Concrete CODED ENUM classes (e.g. CmsQuality) should define
 * their own flag constants and wrap this class.
 *
 * <pre>
 * ┌─────────────────┬───────────────────────┬───────────┬───────────┐
 * │ 2811            │ Range                 │ Bits      │ Java type │
 * ├─────────────────┼───────────────────────┼───────────┼───────────┤
 * │ CODED ENUM      │ n bits (fixed)        │ n         │ long      │
 * └─────────────────┴───────────────────────┴───────────┴───────────┘
 * </pre>
 *
 * <pre>
 * // Bean mode — chain setters
 * CmsCodedEnum flags = new CmsCodedEnum()
 *     .setValue(0b00000101L)
 *     .setSize(6);
 * CmsCodedEnum.encode(pos, flags);
 *
 * // Check individual bits
 * flags.testBit(0);  // bit0 is set?
 * flags.testBit(1);  // bit1 is set?
 *
 * // Quick mode — raw value + explicit bit count
 * CmsCodedEnum.encode(pos, 0b00000101L, 6);
 * CmsCodedEnum.decode(pis, 6);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsCodedEnum {

    /** Bit pattern stored in low bits of a long. */
    private long value;
    /** Fixed bit count. Must be set before encode/decode. */
    private Integer size;

    public CmsCodedEnum() {
        this.value = 0L;
    }

    public CmsCodedEnum(long value, int size) {
        this.value = value;
        this.size = size;
    }

    /**
     * Validates that size is set and value fits within the bit width.
     *
     * @throws IllegalStateException if size is null
     * @throws IllegalArgumentException if value exceeds the bit width
     */
    public void validate() {
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

    /** Tests whether the bit at the given position (0-based, LSB-first) is set. */
    public boolean testBit(int pos) {
        return (value & (1L << pos)) != 0;
    }

    /** Sets the bit at the given position (0-based, LSB-first). */
    public CmsCodedEnum setBit(int pos) {
        value |= (1L << pos);
        return this;
    }

    /** Clears the bit at the given position (0-based, LSB-first). */
    public CmsCodedEnum clearBit(int pos) {
        value &= ~(1L << pos);
        return this;
    }

    /** Sets or clears the bit at the given position (0-based, LSB-first). */
    public CmsCodedEnum setBit(int pos, boolean value) {
        if (value) this.value |= (1L << pos);
        else       this.value &= ~(1L << pos);
        return this;
    }

    /**
     * Gets a multi-bit field starting at pos (LSB-first), width bits wide.
     * For example, getBits(0, 2) extracts bits 0~1 as a 2-bit value.
     */
    public int getBits(int pos, int width) {
        long mask = (1L << width) - 1;
        return (int) ((value >>> pos) & mask);
    }

    /**
     * Sets a multi-bit field starting at pos (LSB-first), width bits wide.
     * For example, setBits(0, 2, 3) sets bits 0~1 to value 3 (binary 11).
     */
    public CmsCodedEnum setBits(int pos, int width, int fieldValue) {
        long mask = (1L << width) - 1;
        value &= ~(mask << pos);            // clear the field
        value |= ((long) (fieldValue & mask) << pos);  // set the new value
        return this;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsCodedEnum bean. */
    public static void encode(PerOutputStream pos, CmsCodedEnum bean) {
        bean.validate();
        PerBitString.encodeFixedSize(pos, bean.value, bean.size);
    }

    /** Encodes a raw long value with explicit bit count. */
    public static void encode(PerOutputStream pos, long value, int size) {
        PerBitString.encodeFixedSize(pos, value, size);
    }

    /** Decodes with explicit bit count. */
    public static CmsCodedEnum decode(PerInputStream pis, int size) throws PerDecodeException {
        return new CmsCodedEnum(PerBitString.decodeFixedSize(pis, size), size);
    }

    @Override
    public String toString() {
        if (size == null) return "CodedEnum[?] = 0x" + Long.toHexString(value).toUpperCase();
        return String.format("CodedEnum[%d] = 0b%s", size,
                Long.toBinaryString(value | (1L << size)).substring(1));
    }
}
