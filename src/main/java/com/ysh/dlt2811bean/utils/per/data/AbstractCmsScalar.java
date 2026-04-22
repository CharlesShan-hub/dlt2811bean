package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import com.ysh.dlt2811bean.utils.per.types.PerReal;

import java.math.BigInteger;

/**
 * Abstract base class for all CMS basic types.
 * Provides complete implementation including encode, decode, get/set and validation.
 *
 * <p>Subclasses only need to provide constructor parameters:
 * type name, min/max range, and default value.
 *
 * <pre>
 * // Bean usage
 * CmsInt8 val = new CmsInt8(42);
 * val.set(100);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt8 val2 = new CmsInt8().set(42).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt8 r = new CmsInt8().decode(pis);
 * int i = r.get();
 * </pre>
 *
 * @param <T> the concrete type implementing this class
 */
public abstract class AbstractCmsScalar<T extends AbstractCmsScalar<T>> implements CmsScalar<T> {

    private final String typeName;
    private final Object min;
    private final Object max;
    private Object value;

    /**
     * Constructor with range validation (for integer types).
     */
    protected AbstractCmsScalar(String typeName, Object min, Object max, Object defaultValue) {
        this.typeName = typeName;
        this.min = min;
        this.max = max;
        this.value = defaultValue;
    }

    /**
     * Constructor without range validation (for float types).
     */
    protected AbstractCmsScalar(String typeName, Object defaultValue) {
        this.typeName = typeName;
        this.min = null;
        this.max = null;
        this.value = defaultValue;
    }

    // ==================== Public API ====================

    public T set(Object value) {
        if (value == null) {
            throw new IllegalArgumentException(typeName + " value cannot be null");
        }
        if (min != null) {
            validateRange(value);
        }
        this.value = value;
        return (T) this;
    }

    public <V> V get() {
        return (V) value;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public void encode(PerOutputStream pos) {
        doEncode(pos);
    }

    @Override
    public String toString() {
        return typeName + ": " + String.valueOf(value);
    }

    // ==================== Decode ====================

    @Override
    public T decode(PerInputStream pis) throws Exception {
        this.value = doDecode(pis);
        return (T) this;
    }

    // ==================== Extension Points ====================

    /**
     * Subclasses may override to provide custom encoding logic.
     * Default implementation uses PerInteger with min/max range.
     */
    protected void doEncode(PerOutputStream pos) {
        if (value instanceof BigInteger) {
            encodeBigInteger(pos, (BigInteger) value);
        } else if (value instanceof Boolean) {
            PerInteger.encode(pos, ((Boolean) value) ? 1L : 0L, 0L, 1L);
        } else if (value instanceof Double) {
            PerReal.encodeFloat64(pos, (Double) value);
        } else if (value instanceof Float) {
            PerReal.encodeFloat32(pos, (Float) value);
        } else {
            PerInteger.encode(pos, ((Number) value).longValue(), toLong(min), toLong(max));
        }
    }

    /**
     * Subclasses may override to provide custom decoding logic.
     * Default implementation uses PerInteger with min/max range.
     */
    protected Object doDecode(PerInputStream pis) throws Exception {
        if (value instanceof BigInteger) {
            return decodeBigInteger(pis);
        }
        if (value instanceof Double) {
            return PerReal.decodeFloat64(pis);
        }
        if (value instanceof Float) {
            return PerReal.decodeFloat32(pis);
        }
        long longVal = PerInteger.decode(pis, toLong(min), toLong(max));
        if (value instanceof Boolean) {
            return longVal != 0;
        } else if (value instanceof Long) {
            return longVal;
        } else {
            return (int) longVal;
        }
    }

    // ==================== Validation ====================

    private void validateRange(Object value) {
        if (value instanceof BigInteger) {
            BigInteger bi = (BigInteger) value;
            BigInteger bMin = toBigInteger(min);
            BigInteger bMax = toBigInteger(max);
            if (bi.compareTo(bMin) < 0 || bi.compareTo(bMax) > 0) {
                throw new IllegalArgumentException(
                    String.format("%s out of range [%s, %s]: %s", typeName, min, max, bi));
            }
        } else {
            long longVal = ((Number) value).longValue();
            long lMin = toLong(min);
            long lMax = toLong(max);
            if (longVal < lMin || longVal > lMax) {
                throw new IllegalArgumentException(
                    String.format("%s out of range [%d, %d]: %d", typeName, lMin, lMax, longVal));
            }
        }
    }

    // ==================== Type Conversion Helpers ====================

    private static long toLong(Object obj) {
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        throw new IllegalArgumentException("Cannot convert to long: " + obj);
    }

    private static BigInteger toBigInteger(Object obj) {
        if (obj instanceof BigInteger) {
            return (BigInteger) obj;
        }
        if (obj instanceof Number) {
            return BigInteger.valueOf(((Number) obj).longValue());
        }
        throw new IllegalArgumentException("Cannot convert to BigInteger: " + obj);
    }

    // ==================== BigInteger Encoding/Decoding ====================

    private static void encodeBigInteger(PerOutputStream pos, BigInteger value) {
        byte[] bytes = bigIntegerToBytes(value, 8);
        for (int i = 0; i < 8; i++) {
            pos.writeByteAligned(bytes[i]);
        }
    }

    private static BigInteger decodeBigInteger(PerInputStream pis) throws Exception {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) pis.readByteAligned();
        }
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < bytes.length; i++) {
            int unsignedByte = bytes[i] & 0xFF;
            result = result.multiply(BigInteger.valueOf(256))
                          .add(BigInteger.valueOf(unsignedByte));
        }
        return result;
    }

    private static byte[] bigIntegerToBytes(BigInteger val, int length) {
        byte[] bytes = new byte[length];
        BigInteger remaining = val;
        for (int i = length - 1; i >= 0; i--) {
            BigInteger[] divRem = remaining.divideAndRemainder(BigInteger.valueOf(256));
            bytes[i] = divRem[1].byteValue();
            remaining = divRem[0];
        }
        if (!remaining.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Value requires more than " + length + " bytes: " + val);
        }
        return bytes;
    }
}
