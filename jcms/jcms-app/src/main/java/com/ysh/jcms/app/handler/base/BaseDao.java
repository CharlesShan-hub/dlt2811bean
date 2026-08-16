package com.ysh.jcms.app.handler.base;

import com.ysh.jcms.core.data.core.CmsNull;
import com.ysh.jcms.core.data.core.CmsType;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Base class for all request DAOs used by client handlers.
 * <p>
 * Subclasses implement {@link #toRequest()} to build the request object; all
 * other execution state (autoPull, response data, pagination) is carried by
 * {@link CmsContent}.
 */
public abstract class BaseDao {

    /**
     * Build the request object and guarantee it is never {@code null}.
     * <p>
     * Template method: delegates to {@link #toRequest()} and throws an
     * {@link IllegalStateException} if the subclass returns null.
     */
    public final CmsType request() {
        CmsType req = toRequest();
        if (req == null) {
            throw new IllegalStateException(getClass().getSimpleName() + ".toRequest() returned null");
        }
        return req;
    }

    /** Build a request object from this DAO's parameters. Empty requests use {@link CmsNull}, never a Java null. */
    protected abstract CmsType toRequest();

    // ── Request-building helpers ─────────────────────────────

    /** Set a value via the given setter only if it is non-null and non-empty. */
    protected void setIfNotEmpty(Consumer<String> setter, String value) {
        if (value != null && !value.isEmpty())
            setter.accept(value);
    }

    /** Map string sources into a typed target list via the given factory. */
    protected <T> void addAll(List<String> sources, List<T> target, Function<String, T> factory) {
        if (sources != null) {
            for (String s : sources) {
                target.add(factory.apply(s));
            }
        }
    }
}
