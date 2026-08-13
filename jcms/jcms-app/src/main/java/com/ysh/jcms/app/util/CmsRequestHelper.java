package com.ysh.jcms.app.util;

import java.util.function.Consumer;

/**
 * Utility methods for building request objects in DAO classes.
 * <p>
 * Reduces boilerplate like:
 * <pre>{@code
 * if (referenceAfter != null && !referenceAfter.isEmpty())
 *     req.referenceAfter(referenceAfter);
 * }</pre>
 * to:
 * <pre>{@code
 * CmsRequestHelper.setIfNotEmpty(req::referenceAfter, referenceAfter);
 * }</pre>
 */
public final class CmsRequestHelper {

    private CmsRequestHelper() {
    }

    /**
     * Set a value on the request object via the given setter, only if the value
     * is non-null and non-empty.
     */
    public static void setIfNotEmpty(Consumer<String> setter, String value) {
        if (value != null && !value.isEmpty())
            setter.accept(value);
    }

    /**
     * Add a list of string references to the request object.
     * <p>
     * Replaces the common pattern:
     * <pre>{@code
     * if (refs != null) {
     *     for (String ref : refs) {
     *         req.reference.add(new CmsObjectReference(ref));
     *     }
     * }
     * }</pre>
     */
    public static <T> void addAll(java.util.List<String> sources,
                                  java.util.List<T> target,
                                  java.util.function.Function<String, T> factory) {
        if (sources != null) {
            for (String s : sources) {
                target.add(factory.apply(s));
            }
        }
    }
}