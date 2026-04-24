package com.ysh.dlt2811bean.utils.per.data;

/**
 * Interface for DL/T 2811 PACKED LIST type (§7.1.8).
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
 * @param <T> the concrete type implementing this interface
 */
public interface CmsPackedList<T extends CmsPackedList<T>> extends CmsScalar<T, Long> {

    /** Get the maximum bit count (SIZE(0..max)). */
    int getMax();

    /** Set the maximum bit count. */
    T setMax(int max);

    /** Get the actual bit count (how many bits are actually used). */
    int getBitLength();

    /** Tests whether the bit at the given position (0-based, LSB-first) is set. */
    boolean testBit(int pos);

    /** Sets or clears the bit at the given position (0-based, LSB-first). */
    T setBit(int pos, boolean value);

    /** Gets a multi-bit field starting at pos (LSB-first), width bits wide. */
    long getBits(int pos, int width);

    /** Tests whether a multi-bit field matches the given value. */
    boolean testBits(int pos, int width, int fieldValue);

    /** Sets a multi-bit field starting at pos (LSB-first), width bits wide. */
    T setBits(int pos, int width, int fieldValue);
}