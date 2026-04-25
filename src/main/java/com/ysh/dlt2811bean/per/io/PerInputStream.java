package com.ysh.dlt2811bean.per.io;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;

/**
 * ASN.1 APER (Aligned Packed Encoding Rules) bit-level input stream.
 *
 * <p>Standard: GB/T 16263.2 / ITU-T X.691
 *
 * <p>Role in DL/T 2811: all service ASDU PER decoding reads through this stream.
 * Each service class extracts the ASDU byte array from a received frame,
 * creates an instance, then reads PER data field-by-field in {@code decodeAsdu()}.
 *
 * <p>Key characteristics:
 * <ul>
 *   <li>Reads at <strong>bit-level</strong> (not byte-level), symmetric to {@link PerOutputStream}</li>
 *   <li>Supports byte alignment ({@link #align()}), consistent with APER rules</li>
 *   <li>Throws {@link PerDecodeException} on underflow</li>
 * </ul>
 *
 * <h3>Example: round-trip decode</h3>
 * <pre>{@code
 *   byte[] encoded = ...;  // from PerOutputStream.toByteArray()
 *   PerInputStream pis = new PerInputStream(encoded);
 *
 *   boolean flag = pis.readBit();          // 1 bit
 *   long val   = pis.readBits(3);          // 3 bits
 *   pis.align();                           // pad to byte boundary
 *   byte[] data = pis.readBytes(4);        // 4 bytes
 * }</pre>
 *
 * @see PerOutputStream
 * @see PerDecodeException
 */
public final class PerInputStream {

    /** Source data. */
    private final byte[] data;

    /** Total number of bits available. */
    private final int totalBits;

    /** Current read position (bit index). */
    private int bitPosition;

    /**
     * Creates an input stream from a byte array.
     *
     * @param data encoded byte array
     * @throws IllegalArgumentException if data is null
     */
    public PerInputStream(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        this.data = data;
        this.totalBits = data.length * 8;
        this.bitPosition = 0;
    }

    // ==================== Core read methods ====================

    /**
     * Reads a single bit.
     *
     * @return true=1, false=0
     * @throws PerDecodeException if at end of data
     */
    public boolean readBit() throws PerDecodeException {
        ensureAvailable(1);
        boolean value = ((data[bitPosition / 8] & (0x80 >> (bitPosition % 8))) != 0);
        bitPosition++;
        return value;
    }

    /**
     * Reads the specified number of bits as an unsigned integer.
     *
     * @param numBits number of bits (1 ~ 64)
     * @return unsigned integer value
     * @throws PerDecodeException if insufficient data or invalid parameter
     */
    public long readBits(int numBits) throws PerDecodeException {
        if (numBits < 1 || numBits > 64) {
            throw new PerDecodeException("numBits must be in range [1, 64], got: " + numBits);
        }
        ensureAvailable(numBits);

        long result = 0;
        for (int i = 0; i < numBits; i++) {
            result <<= 1;
            if ((data[bitPosition / 8] & (0x80 >> (bitPosition % 8))) != 0) {
                result |= 1L;
            }
            bitPosition++;
        }
        return result;
    }

    /**
     * Reads a signed integer (two's complement) after alignment.
     *
     * @param numBytes number of bytes
     * @return signed long value
     * @throws PerDecodeException if insufficient data
     */
    public long readSignedInteger(int numBytes) throws PerDecodeException {
        align();
        ensureAvailable(numBytes * 8);

        long result = 0;
        for (int i = 0; i < numBytes; i++) {
            result = (result << 8) | (readByteAligned() & 0xFFL);
        }

        // Sign-extend
        int shift = 64 - (numBytes * 8);
        return (result << shift) >> shift;
    }

    /**
     * Reads an unsigned integer after alignment.
     *
     * @param numBytes number of bytes
     * @return unsigned long value
     * @throws PerDecodeException if insufficient data
     */
    public long readUnsignedInteger(int numBytes) throws PerDecodeException {
        align();
        ensureAvailable(numBytes * 8);

        long result = 0;
        for (int i = 0; i < numBytes; i++) {
            result = (result << 8) | (readByteAligned() & 0xFFL);
        }
        return result;
    }

    /**
     * Reads a single byte at the byte-aligned position (auto-aligns first).
     *
     * @return byte value (0~255)
     * @throws PerDecodeException if insufficient data
     */
    public int readByteAligned() throws PerDecodeException {
        align();
        ensureAvailable(8);
        int value = data[bitPosition / 8] & 0xFF;
        bitPosition += 8;
        return value;
    }

    /**
     * Reads raw bytes after alignment.
     *
     * @param length number of bytes to read
     * @return byte array
     * @throws PerDecodeException if insufficient data
     */
    public byte[] readBytes(int length) throws PerDecodeException {
        align();
        ensureAvailable(length * 8);
        byte[] result = new byte[length];
        System.arraycopy(data, bitPosition / 8, result, 0, length);
        bitPosition += length * 8;
        return result;
    }

    // ==================== Alignment ====================

    /**
     * Advances to the next byte boundary (APER alignment operation).
     *
     * @throws PerDecodeException if alignment would exceed data bounds
     */
    public void align() throws PerDecodeException {
        int remainder = bitPosition % 8;
        if (remainder != 0) {
            int skipBits = 8 - remainder;
            ensureAvailable(skipBits);
            bitPosition += skipBits;
        }
    }

    /**
     * Returns whether the current position is on a byte boundary.
     *
     * @return true if at byte boundary
     */
    public boolean isAligned() {
        return bitPosition % 8 == 0;
    }

    // ==================== Status queries ====================

    /**
     * Returns the current bit offset.
     */
    public int getBitPosition() {
        return bitPosition;
    }

    /**
     * Returns the number of remaining readable bits.
     */
    public int getRemainingBits() {
        return totalBits - bitPosition;
    }

    /**
     * Returns the number of remaining readable bytes (rounded down).
     */
    public int getRemainingBytes() {
        return getRemainingBits() / 8;
    }

    /**
     * Returns whether there is unread data.
     */
    public boolean hasRemaining() {
        return bitPosition < totalBits;
    }

    /**
     * Returns whether all data has been consumed.
     *
     * @param alignedEnd true to require ending at byte boundary; false allows trailing padding bits
     */
    public boolean isAtEnd(boolean alignedEnd) {
        if (alignedEnd) {
            return bitPosition >= totalBits || (bitPosition == totalBits && isAligned());
        } else {
            return bitPosition >= totalBits;
        }
    }

    // ==================== Internal ====================

    private void ensureAvailable(int bitsNeeded) throws PerDecodeException {
        if (bitPosition + bitsNeeded > totalBits) {
            throw new PerDecodeException(
                String.format("Insufficient data: need %d more bits at position %d, but only %d bits remain",
                    bitsNeeded, bitPosition, totalBits - bitPosition));
        }
    }

    @Override
    public String toString() {
        return "PerInputStream{pos=" + bitPosition + "/" + totalBits + "bits, remaining=" + getRemainingBits() + "}";
    }
}
