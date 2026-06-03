package com.ysh.jcms.datatypes.type;

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
public abstract class AbstractCmsString<T extends AbstractCmsString<T, V>, V>
        extends AbstractCmsScalar<T, V> implements CmsString<T, V> {

    /** Maximum PER encode buffer size for string types (65536 bytes). */
    protected static final int MAX_ENCODE_BUF_SIZE = 65536;

    /** Encoding mode: FIXED for SIZE(n), VARIABLE for SIZE(0..max). */
    public enum Mode {
        FIXED,
        VARIABLE
    }

    protected Integer size;
    protected Integer max;

    protected AbstractCmsString(String typeName, V defaultValue) {
        super(typeName, defaultValue);
    }

    @Override
    public T size(int size) {
        this.size = size;
        this.max = null;
        return self();
    }

    @Override
    public T max(int max) {
        this.max = max;
        this.size = null;
        return self();
    }

    @Override
    public boolean isFixed() {
        return size != null;
    }

    @Override
    public boolean isVariable() {
        return max != null;
    }

    @Override
    public int getConstrainedLen() {
        if (size != null) return size;
        if (max != null) return max;
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T copy() {
        try {
            AbstractCmsString<T, V> clone = (AbstractCmsString<T, V>) getClass().getDeclaredConstructor().newInstance();
            clone.value = this.value;
            clone.present = this.present;
            if (this.size != null) clone.size = this.size;
            if (this.max != null) clone.max = this.max;
            return (T) clone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy " + typeName, e);
        }
    }

    @Override
    public void set(V value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        int len;
        if (value instanceof byte[]) {
            len = ((byte[]) value).length;
        } else if (value instanceof String) {
            len = ((String) value).length();
        } else {
            len = 0;
        }
        if (size != null && len > size) {
            throw new IllegalArgumentException(
                typeName + " value length " + len + " exceeds fixed size " + size);
        }
        if (max != null && len > max) {
            throw new IllegalArgumentException(
                typeName + " value length " + len + " exceeds maximum " + max);
        }
        super.set(value);
    }
}
