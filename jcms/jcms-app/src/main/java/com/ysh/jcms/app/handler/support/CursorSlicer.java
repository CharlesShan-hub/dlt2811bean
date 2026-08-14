package com.ysh.jcms.app.handler.support;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;

import java.util.List;
import java.util.function.Function;

/**
 * Generic cursor-based list slicer for {@code referenceAfter} pagination.
 * <p>
 * Replaces the duplicated {@code afterEntries} methods in
 * {@code AllDataValuesServer}, {@code AllDataDefServer},
 * {@code AllCbValuesServer}, etc.
 *
 * <p>
 * Usage:
 *
 * <pre>
 * {@code
 * List<CmsDataValueEntry> entries = CursorSlicer.after(
 *     allEntries, refAfter, e -> e.reference.value(), reqId);
 * }
 * </pre>
 */
public final class CursorSlicer {

    private CursorSlicer() {
    }

    /**
     * Returns the sublist of {@code items} starting <em>after</em> the element
     * whose reference matches {@code cursor}.
     *
     * @param <T>      the element type
     * @param items    the full list (must be sorted by reference)
     * @param cursor   the cursor reference (may be null or empty, in which case the
     *                 full list is returned)
     * @param refExtractor extracts the reference string from each element
     * @param reqId    request ID for error reporting
     * @return the sublist after the cursor
     * @throws ServiceException with {@code INSTANCE_NOT_AVAILABLE} if the cursor is
     *                          not found in the list
     */
    public static <T> List<T> after(List<T> items, String cursor, Function<T, String> refExtractor, int reqId) {
        if (cursor == null || cursor.isEmpty())
            return items;
        for (int i = 0; i < items.size(); i++) {
            String ref = refExtractor.apply(items.get(i));
            if (cursor.equals(ref)) {
                return items.subList(i + 1, items.size());
            }
        }
        throw new ServiceException(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
    }
}