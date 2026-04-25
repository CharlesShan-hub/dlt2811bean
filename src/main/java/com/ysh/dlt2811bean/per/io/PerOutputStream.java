package com.ysh.dlt2811bean.per.io;

import com.ysh.dlt2811bean.per.types.PerInteger;

/**
 * ASN.1 APER (Aligned Packed Encoding Rules) bit-level output stream.
 *
 * <p>Standard: GB/T 16263.2 / ITU-T X.691
 *
 * <p>Role in DL/T 2811: all service ASDU PER encoding writes through this stream.
 * Each service class creates an instance in {@code encodeAsdu()}, writes PER data
 * field-by-field, then calls {@link #toByteArray()} to get the encoded result.
 *
 * <p>Key characteristics:
 * <ul>
 *   <li>Writes at <strong>bit-level</strong> (not byte-level) — the fundamental difference between PER and BER</li>
 *   <li>APER requires byte alignment ({@link #align()}) at certain positions</li>
 *   <li>Internal byte[] buffer with auto-expansion; pure in-memory, no close() needed</li>
 * </ul>
 *
 * <h3>Example: encode then decode round-trip</h3>
 * <pre>{@code
 *   // --- Encode ---
 *   PerOutputStream pos = new PerOutputStream();
 *   pos.writeBit(true);                   // 1 bit: flag
 *   pos.writeBits(5, 3);                  // 3 bits: value 5
 *   pos.align();                           // pad to byte boundary
 *   pos.writeBytes(new byte[]{0x01, 0x02, 0x03, 0x04}); // 4 raw bytes
 *   byte[] encoded = pos.toByteArray();    // get result: [0b1_101_00000, 0x01, 0x02, 0x03, 0x04]
 *
 *   // --- Decode ---
 *   PerInputStream pis = new PerInputStream(encoded);
 *   boolean flag = pis.readBit();          // true
 *   long val   = pis.readBits(3);          // 5
 *   pis.align();
 *   byte[] raw = pis.readBytes(4);         // [0x01, 0x02, 0x03, 0x04]
 * }</pre>
 *
 * @see PerInputStream
 * @see PerInteger
 */
public final class PerOutputStream {

    /** Internal buffer, initial capacity 256 bytes. */
    private byte[] buffer;

    /** Number of bits written so far (not bytes). */
    private int bitPosition;

    /** Default initial capacity. */
    private static final int DEFAULT_CAPACITY = 256;

    public PerOutputStream() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates a stream with the specified initial capacity.
     *
     * @param initialCapacity initial byte capacity
     */
    public PerOutputStream(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be > 0");
        }
        this.buffer = new byte[initialCapacity];
        this.bitPosition = 0;
    }

    // ==================== Core write methods ====================

    /**
     * Writes a single bit.
     *
     * @param value true=1, false=0
     */
    public void writeBit(boolean value) {
        ensureCapacity(bitPosition + 1);
        if (value) {
            buffer[bitPosition / 8] |= (byte) (0x80 >> (bitPosition % 8));
        }
        bitPosition++;
    }

    /**
     * Writes multiple bits as an unsigned integer (MSB first).
     *
     * @param value   value to write (must be >= 0)
     * @param numBits number of bits (1 ~ 64)
     * @throws IllegalArgumentException if value exceeds the range representable by numBits
     */
    public void writeBits(long value, int numBits) {
        if (numBits < 1 || numBits > 64) {
            throw new IllegalArgumentException("numBits must be in range [1, 64], got: " + numBits);
        }
        long max = (1L << numBits) - 1;
        if (value < 0 || value > max) {
            throw new IllegalArgumentException(
                String.format("value %d cannot be represented in %d bits (max=%d)", value, numBits, max));
        }
        ensureCapacity(bitPosition + numBits);

        // Write from MSB to LSB
        for (int i = numBits - 1; i >= 0; i--) {
            boolean bit = ((value >> i) & 1L) != 0;
            if (bit) {
                buffer[bitPosition / 8] |= (byte) (0x80 >> (bitPosition % 8));
            }
            bitPosition++;
        }
    }

    /**
     * Writes a signed integer (two's complement form).
     *
     * <p>For constrained INTEGER, prefer {@link #writeBits(long, int)} with offset value for better efficiency.
     * This method is for unconstrained or semi-constrained full-value encoding.
     *
     * @param value    signed integer value
     * @param numBytes number of bytes (1~8)
     */
    public void writeSignedInteger(long value, int numBytes) {
        align();
        ensureCapacity(bitPosition + numBytes * 8);

        for (int i = numBytes - 1; i >= 0; i--) {
            int shift = i * 8;
            byte b = (byte) ((value >> shift) & 0xFF);
            writeByteAligned(b);
        }
    }

    /**
     * Writes a single byte at the byte-aligned position (auto-aligns first).
     *
     * @param value byte value
     */
    public void writeByteAligned(byte value) {
        align();
        int byteIndex = bitPosition / 8;
        ensureCapacity((byteIndex + 1) * 8);
        buffer[byteIndex] = value;
        bitPosition += 8;
    }

    /**
     * Writes raw bytes after alignment.
     *
     * @param data byte array
     */
    public void writeBytes(byte[] data) {
        if (data == null || data.length == 0) return;
        align();
        ensureCapacity(bitPosition + data.length * 8);
        System.arraycopy(data, 0, buffer, bitPosition / 8, data.length);
        bitPosition += data.length * 8;
    }

    // ==================== Alignment ====================

    /**
     * Byte alignment: advances to the next byte boundary (high bits padded with zeros).
     *
     * <p>This is a key APER operation — certain types require alignment before/after encoding:
     * <ul>
     *   <li>Unconstrained/semi-constrained INTEGER encoding</li>
     *   <li>OCTET STRING contents</li>
     *   <li>Length determinant above certain threshold</li>
     * </ul>
     */
    public void align() {
        int remainder = bitPosition % 8;
        if (remainder != 0) {
            bitPosition += (8 - remainder);
            // Padding bits are already 0 by default
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
     * Returns the total number of bits written.
     */
    public int getBitLength() {
        return bitPosition;
    }

    /**
     * Returns the total number of bytes written (rounded up).
     */
    public int getByteLength() {
        return (bitPosition + 7) / 8;
    }

    /**
     * Returns a copy of the internal buffer (trimmed to valid length).
     *
     * @return byte array of the exact valid length
     */
    public byte[] toByteArray() {
        byte[] result = new byte[getByteLength()];
        System.arraycopy(buffer, 0, result, 0, result.length);
        return result;
    }

    /**
     * Returns the internal buffer reference (no copy, for high-performance scenarios).
     *
     * @return buffer and valid length
     */
    public BufferResult getBuffer() {
        return new BufferResult(buffer, getByteLength());
    }

    // ==================== Internal ====================

    private void ensureCapacity(int minBitCapacity) {
        int minByteCapacity = (minBitCapacity + 7) / 8;
        if (minByteCapacity <= buffer.length) return;

        // Expand to 1.5x or minimum needed (whichever is larger)
        int newCapacity = Math.max(buffer.length + (buffer.length >> 1), minByteCapacity);
        byte[] newBuffer = new byte[newCapacity];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        buffer = newBuffer;
    }

    /**
     * Resets the stream (clears all written data, reuses buffer).
     */
    public void reset() {
        bitPosition = 0;
    }

    @Override
    public String toString() {
        return "PerOutputStream{bits=" + bitPosition + ", bytes=" + getByteLength() + "}";
    }

    // ==================== Inner classes ====================

    /** Buffer result wrapper to avoid array copy. */
    public static final class BufferResult {
        public final byte[] data;
        public final int length;

        public BufferResult(byte[] data, int length) {
            this.data = data;
            this.length = length;
        }
    }
}
