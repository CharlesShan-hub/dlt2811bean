package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigInteger;

/**
 * DL/T 2811 INT64U type — unsigned 64-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT64U   │ 0 .. 2^64-1          │ 64   │ BigInteger│
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Uses 8-byte big-endian byte-aligned encoding.
 *
 * <pre>
 * // Bean usage
 * CmsInt64U val = new CmsInt64U(new BigInteger("12345678901234567890"));
 * CmsInt64U.encode(pos, val);
 *
 * // Quick usage — pass BigInteger directly
 * CmsInt64U.encode(pos, new BigInteger("12345678901234567890"));
 *
 * // Decode always returns a bean
 * CmsInt64U r = CmsInt64U.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsInt64U {

    /** Minimum value for INT64U. */
    public static final BigInteger MIN = BigInteger.ZERO;
    /** Maximum value for INT64U (2^64-1). */
    public static final BigInteger MAX = new BigInteger("18446744073709551615"); // 2^64-1

    private BigInteger value;

    /** Validates that the given value is within INT64U range (0 to 2^64-1). Throws if invalid. */
    public static void validateValue(BigInteger value) {
        if (value.compareTo(MIN) < 0 || value.compareTo(MAX) > 0) {
            throw new IllegalArgumentException("INT64U out of range [0, 18446744073709551615]: " + value);
        }
    }

    /** Validates that the given long value is within INT64U range (0 to 2^64-1). Throws if invalid. */
    public static void validateValue(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("INT64U out of range [0, 18446744073709551615]: " + value);
        }
    }

    /** Validates that the given int value is within INT64U range (0 to 2^64-1). Throws if invalid. */
    public static void validateValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("INT64U out of range [0, 18446744073709551615]: " + value);
        }
    }

    public CmsInt64U() {
        this.value = BigInteger.ZERO;
    }

    public CmsInt64U(BigInteger value) {
        validateValue(value);
        this.value = value;
    }

    public CmsInt64U(long value) {
        validateValue(value);
        this.value = BigInteger.valueOf(value);
    }

    public CmsInt64U(int value) {
        validateValue(value);
        this.value = BigInteger.valueOf(value);
    }

    public CmsInt64U(String value) {
        BigInteger bigIntValue;
        try {
            bigIntValue = new BigInteger(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid INT64U string value: " + value, e);
        }
        validateValue(bigIntValue);
        this.value = bigIntValue;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsInt64U bean. */
    public static void encode(PerOutputStream pos, CmsInt64U val) {
        encode(pos, val.value);
    }

    /** Encodes a raw BigInteger value (with range validation). */
    public static void encode(PerOutputStream pos, BigInteger val) {
        validateValue(val);
        
        // Convert BigInteger to 8-byte array
        byte[] bytes = bigIntegerToBytes(val, 8);
        
        // Write all 8 bytes
        for (int i = 0; i < 8; i++) {
            pos.writeByteAligned(bytes[i]);
        }
    }

    /** Encodes a raw long value (with range validation). */
    public static void encode(PerOutputStream pos, long val) {
        validateValue(val);
        encode(pos, BigInteger.valueOf(val));
    }

    /** Encodes a raw int value (with range validation). */
    public static void encode(PerOutputStream pos, int val) {
        validateValue(val);
        encode(pos, BigInteger.valueOf(val));
    }

    /** Encodes a raw string value (with range validation). */
    public static void encode(PerOutputStream pos, String val) {
        BigInteger bigIntValue;
        try {
            bigIntValue = new BigInteger(val);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid INT64U string value: " + val, e);
        }
        validateValue(bigIntValue);
        encode(pos, bigIntValue);
    }

    public static CmsInt64U decode(PerInputStream pis) throws PerDecodeException {
        // Read 8 bytes
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) pis.readByteAligned();
        }
        
        // Convert bytes to BigInteger (unsigned)
        BigInteger value = bytesToBigInteger(bytes);
        return new CmsInt64U(value);
    }

    // ==================== Helper Methods ====================

    /**
     * Convert BigInteger to byte array with exactly specified length.
     * Big-endian representation.
     */
    private static byte[] bigIntegerToBytes(BigInteger val, int length) {
        byte[] bytes = new byte[length];
        BigInteger remaining = val;
        
        for (int i = length - 1; i >= 0; i--) {
            BigInteger[] divRem = remaining.divideAndRemainder(BigInteger.valueOf(256));
            bytes[i] = divRem[1].byteValue(); // Remainder is the byte value (0-255)
            remaining = divRem[0]; // Quotient for next iteration
        }
        
        // After extracting all bytes, remaining should be 0
        if (!remaining.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Value requires more than " + length + " bytes: " + val);
        }
        
        return bytes;
    }

    /**
     * Convert byte array to BigInteger (unsigned).
     * Big-endian representation.
     */
    private static BigInteger bytesToBigInteger(byte[] bytes) {
        BigInteger result = BigInteger.ZERO;
        
        for (int i = 0; i < bytes.length; i++) {
            // Convert byte to unsigned int (0-255)
            int unsignedByte = bytes[i] & 0xFF;
            result = result.multiply(BigInteger.valueOf(256))
                          .add(BigInteger.valueOf(unsignedByte));
        }
        
        return result;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    // ==================== Convenience Setters ====================

    /** Sets the value from a BigInteger (with range validation). */
    public CmsInt64U setValue(BigInteger value) {
        validateValue(value);
        this.value = value;
        return this;
    }

    /** Sets the value from a long (with range validation). */
    public CmsInt64U setValue(long value) {
        validateValue(value);
        this.value = BigInteger.valueOf(value);
        return this;
    }

    /** Sets the value from an int (with range validation). */
    public CmsInt64U setValue(int value) {
        validateValue(value);
        this.value = BigInteger.valueOf(value);
        return this;
    }

    /** Sets the value from a string (with range validation). */
    public CmsInt64U setValue(String value) {
        BigInteger bigIntValue;
        try {
            bigIntValue = new BigInteger(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid INT64U string value: " + value, e);
        }
        validateValue(bigIntValue);
        this.value = bigIntValue;
        return this;
    }
}