package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base for INTEGER / BIT STRING enumeration types whose Inner* stores a value
 * in {@code _v}.
 *
 * <p>
 * CmsEnum provides {@link #value()} / {@link #value(int)} with optional range
 * validation via {@link ValueRange}. Subclasses just declare named constants.
 *
 * <p>
 * For BIT STRING types (e.g. Dbpos, Tcmd) the hex ↔ int conversion is handled
 * automatically — the bit size is derived from {@link ValueRange#max()}.
 *
 * <pre>
 * {
 *     &#64;code
 *     &#64;ValueRange(min = 0, max = 12)
 *     public class CmsServiceError extends CmsEnum<CmsServiceError> {
 *         public static final int NO_ERROR = 0;
 *         public static final int INSTANCE_NOT_AVAILABLE = 1;
 *         // ...
 *         public CmsServiceError() {
 *             super(new InnerServiceError());
 *         }
 *     }
 * }
 * </pre>
 */
public abstract class CmsEnum<T extends CmsEnum<T>> extends CmsScalar {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ValueRange {
        int min();
        int max();
    }

    protected CmsEnum() {
    }

    protected CmsEnum(InnerBase inner) {
        super(inner);
    }

    /**
     * Per-class {@link ValueRange} annotation, cached (avoids reflection on every
     * access).
     */
    private static final ClassValue<ValueRange> VALUE_RANGE = new ClassValue<ValueRange>() {
        @Override
        protected ValueRange computeValue(Class<?> type) {
            return type.getAnnotation(ValueRange.class);
        }
    };

    /** Minimum bits needed to represent values 0..max. */
    private static int bitsForMax(int max) {
        return 32 - Integer.numberOfLeadingZeros(max);
    }

    /**
     * Get the current integer value. Handles both INTEGER and BIT STRING hex
     * storage.
     */
    @SuppressWarnings("unchecked")
    public int value() {
        Object v = innerGet();
        if (v instanceof String) {
            ValueRange range = VALUE_RANGE.get(getClass());
            int bits = bitsForMax(range != null ? range.max() : 0);
            return InnerBase.parseBitStringHex((String) v, bits);
        }
        if (v instanceof Number)
            return ((Number) v).intValue();
        return 0;
    }

    /** Set value with range validation from {@link ValueRange} if present. */
    public T value(int v) {
        ValueRange range = VALUE_RANGE.get(getClass());
        if (range != null && (v < range.min() || v > range.max()))
            throw new IllegalArgumentException(
                    getClass().getSimpleName() + " out of range [" + range.min() + "," + range.max() + "]: " + v);
        // BIT STRING types store hex strings, INTEGER types store integers directly
        Object current = innerGet();
        if (current instanceof String) {
            int bits = bitsForMax(range != null ? range.max() : 0);
            innerSet(InnerBase.bitStringHex(v, bits));
        } else {
            innerSet(v);
        }
        return (T) this;
    }
}
