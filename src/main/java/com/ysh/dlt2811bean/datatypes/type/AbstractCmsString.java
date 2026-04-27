package com.ysh.dlt2811bean.datatypes.type;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import lombok.Getter;

/**
 * Abstract base class for all CMS string types.
 * Provides common structure for string-like types with size/max constraints.
 *
 * <p>String types support two constraint modes:
 * <ul>
 *   <li><b>FIXED</b> (SIZE(n)): fixed length, padded/trimmed as needed</li>
 *   <li><b>VARIABLE</b> (SIZE(0..max)): variable length with length prefix</li>
 * </ul>
 *
 * @param <T> the concrete string type
 * @param <V> the value type (String, byte[], etc.)
 */
@Getter
public abstract class AbstractCmsString<T extends AbstractCmsString<T, V>, V> extends AbstractCmsScalar<T, V> implements CmsString<T, V> {

    /** Encoding mode: FIXED for SIZE(n), VARIABLE for SIZE(0..max). */
    public enum Mode {
        /** Fixed-size: SIZE(n), padded/trimmed as needed. */
        FIXED,
        /** Variable-size: SIZE(0..max), length prefix encoded. */
        VARIABLE
    }

    protected Integer size;
    protected Integer max;

    /**
     * Constructor for string types.
     *
     * @param typeName the DL/T 2811 type name (e.g., "OCTET STRING")
     * @param value initial value (empty string/array for default)
     */
    protected AbstractCmsString(String typeName, V value) {
        super(typeName, value);
    }

    // ==================== Public API ====================

    /**
     * Set fixed size (SIZE(n)). Clears max.
     */
    public T size(int size) {
        this.size = size;
        this.max = null;
        return self();
    }

    /**
     * Set maximum size (SIZE(0..max)). Clears size.
     */
    public T max(int max) {
        this.max = max;
        this.size = null;
        return self();
    }

    @Override
    public T set(V value) {
        super.set(value);
        validateConstraints();
        return self();
    }

    private void validateConstraints() {
        if (value == null) return;
        int len;
        if (value instanceof byte[]) {
            len = ((byte[]) value).length;
        } else if (value instanceof String) {
            len = ((String) value).length();
        } else {
            return;
        }
        if (size != null && len > size) {
            throw new IllegalArgumentException(
                typeName + " value length " + len + " exceeds fixed size " + size);
        }
        if (max != null && len > max) {
            throw new IllegalArgumentException(
                typeName + " value length " + len + " exceeds maximum " + max);
        }
    }

    // ==================== Encode / Decode ====================

    @Override
    public void encode(PerOutputStream pos) {
        if (size != null && max == null) {
            encodeFixedSize(pos);
        } else if (size == null && max != null) {
            encodeConstrained(pos);
        } else {
            throw new IllegalStateException("String must have either size or max constraint");
        }
    }

    @Override
    public T decode(PerInputStream pis) throws Exception {
        if (size != null && max == null) {
            set(decodeValueFixedSize(pis));
        } else if (size == null && max != null) {
            set(decodeValueConstrained(pis));
        } else {
            throw new IllegalStateException("String must have either size or max constraint");
        }
        return self();
    }
    
    protected abstract void encodeFixedSize(PerOutputStream pos);

    protected abstract void encodeConstrained(PerOutputStream pos);

    protected abstract V decodeValueFixedSize(PerInputStream pis) throws Exception;

    protected abstract V decodeValueConstrained(PerInputStream pis) throws Exception;
}
