package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base for INTEGER enumeration types whose Inner* has a {@code value} field.
 *
 * <p>CmsEnum provides {@link #value()} / {@link #value(int)} with optional range
 * validation via {@link ValueRange}. Subclasses just declare named constants.
 *
 * <pre>{@code
 * &#64;ValueRange(min = 0, max = 12)
 * public class CmsServiceError extends CmsEnum<CmsServiceError> {
 *     public static final int NO_ERROR = 0;
 *     public static final int INSTANCE_NOT_AVAILABLE = 1;
 *     // ...
 *     public CmsServiceError() { super(new InnerServiceError()); }
 * }
 * }</pre>
 */
public abstract class CmsEnum<T extends CmsEnum<T>> extends CmsScalar {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ValueRange {
        int min();
        int max();
    }

    protected CmsEnum() {}

    protected CmsEnum(InnerBase inner) {
        super(inner);
    }

    /** Get the current integer value. */
    @SuppressWarnings("unchecked")
    public int value() {
        Integer v = (Integer) innerGet();
        return v != null ? v : 0;
    }

    /** Set value with range validation from {@link ValueRange} if present. */
    public T value(int v) {
        ValueRange range = getClass().getAnnotation(ValueRange.class);
        if (range != null && (v < range.min() || v > range.max()))
            throw new IllegalArgumentException(
                getClass().getSimpleName() + " out of range [" + range.min() + "," + range.max() + "]: " + v);
        innerSet(v);
        return (T) this;
    }
}
