package com.ysh.dlt2811bean.datatypes.type;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

import java.math.BigInteger;

/**
 * Abstract base class for all CMS basic types.
 * Provides get/set, validation, and encoding helpers.
 * Subclasses implement their own encode/decode logic.
 *
 * @param <T> the concrete type implementing this class
 * @param <V> the value type (e.g. Integer, Long, Boolean, BigInteger)
 */
public abstract class AbstractCmsNumeric<T extends AbstractCmsNumeric<T, V>, V> extends AbstractCmsScalar<T, V> implements CmsNumeric<T, V> {
    
    private final BigInteger min;
    private final BigInteger max;

    /**
     * Constructor with range validation (for integer types).
     */
    @SuppressWarnings("unchecked")
    protected AbstractCmsNumeric(String typeName, Object min, Object max, Object defaultValue) {
        super(typeName, (V) defaultValue);
        this.min = toBigInteger(min);
        this.max = toBigInteger(max);
        validateRange(defaultValue);
    }

    /**
     * Constructor without range validation (for float types).
     */
    protected AbstractCmsNumeric(String typeName, Object defaultValue) {
        this(typeName, null, null, defaultValue);
    }

    // ==================== Public API ====================

    @Override
    public T set(V value) {
        if (value == null) {
            throw new IllegalArgumentException(typeName + " value cannot be null");
        }
        if (min != null) {
            validateRange(value);
        }
        this.value = value;
        return self();
    }

    // ==================== Encode / Decode ====================

    @Override
    public T decode(PerInputStream pis) throws Exception {
        V decodedValue = decodeValue(pis);
        set(decodedValue);
        return self();
    }

    public abstract void encode(PerOutputStream pos);

    protected abstract V decodeValue(PerInputStream pis) throws Exception;

    // ==================== Private Helpers ====================

    private void validateRange(Object value) {
        if (value instanceof Boolean) return;
        if (min == null || max == null) return;
        BigInteger bi = toBigInteger(value);
        if (bi.compareTo(min) < 0 || bi.compareTo(max) > 0) {
            throw new IllegalArgumentException(
                    String.format("%s out of range [%s, %s]: %s", typeName, min, max, bi));
        }
    }

    private static BigInteger toBigInteger(Object obj) {
        if (obj == null) return null;
        if (obj instanceof BigInteger) return (BigInteger) obj;
        if (obj instanceof Number) return BigInteger.valueOf(((Number) obj).longValue());
        throw new IllegalArgumentException("Unsupported type: " + obj.getClass());
    }
}
