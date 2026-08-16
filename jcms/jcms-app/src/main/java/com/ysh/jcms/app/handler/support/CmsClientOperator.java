package com.ysh.jcms.app.handler.support;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.data.core.CmsType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Global pagination helper providing utilities for paginated client handlers.
 * <p>
 * Wraps {@link PaginationContext} and {@link CmsContent#res()} map operations so
 * the Client handlers don't deal with raw casts or ctx state management.
 * <p>
 * Usage (inside {@link BaseClientHandler#onSuccess}):
 *
 * <pre>
 * {
 *     &#64;code
 *     CmsContent<?> c = content();
 *
 *     // Initialize the result map (in beforeAll)
 *     CmsClientOperator.initResult(c, "reference");
 *
 *     // Handle each page (in onSuccess)
 *     CmsClientOperator.page(c).add("reference", resp.reference).moreFollows(resp.moreFollows.value()).lastRef(resp.reference,
 *             CmsObjectReference::value);
 *
 *     // Read the result (in afterAll)
 *     List<CmsObjectReference> refs = CmsClientOperator.getResultList(c, "reference");
 * }
 * </pre>
 */
public final class CmsClientOperator {

    private CmsClientOperator() {
    }

    /**
     * Initialize {@link CmsContent#res()} as a map containing an empty list for
     * the given field. Equivalent to creating {@code {"field": []}} in
     * {@code beforeAll}.
     */
    public static void initResult(CmsContent<?> content, String dataField) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(dataField, new ArrayList<>());
        content.res(map);
    }

    /**
     * Get the typed list for a field from the {@link CmsContent#res()} map.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getResultList(CmsContent<?> content, String field) {
        return (List<T>) ((Map<String, Object>) content.res()).get(field);
    }

    /**
     * Create a {@link PaginationHelper} to record pagination data fluently
     * inside {@code onSuccess}.
     */
    public static PaginationHelper page(CmsContent<?> content) {
        return new PaginationHelper(content);
    }

    /**
     * Accumulate a paginated response page into the content's result.
     * <p>
     * First call initializes the result map; subsequent calls merge data. Uses
     * {@code resp.inner.toJsonValue()} to obtain the page data as a Map, then:
     * <ul>
     * <li>Appends the list field items to the accumulated list</li>
     * <li>Tracks moreFollows</li>
     * <li>Sets the last cursor (last item in the list) in the pagination
     * context</li>
     * </ul>
     * <p>
     * Usage in {@code onSuccess}:
     *
     * <pre>
     * {
     *     &#64;code
     *     CmsGetServerDirectoryResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetServerDirectoryResponse());
     *     CmsClientOperator.accumulatePage(content(), resp, "reference");
     * }
     * </pre>
     *
     * @param content
     *            the current execution content
     * @param resp
     *            the response PDU (must have a public {@code inner} field of type
     *            {@code InnerBase})
     * @param listField
     *            the field name in the JSON that contains the list of items
     */
    @SuppressWarnings("unchecked")
    public static void accumulatePage(CmsContent<?> content, CmsType resp, String listField) {
        Map<String, Object> pageMap = (Map<String, Object>) resp.inner.toJsonValue();
        List<Object> pageList = (List<Object>) pageMap.getOrDefault(listField, Collections.emptyList());
        Object mf = pageMap.get("moreFollows");
        boolean moreFollows = mf instanceof Number && ((Number) mf).longValue() != 0;

        // First page: initialize the result map
        if (content.res() == null) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(listField, new ArrayList<>(pageList));
            map.put("moreFollows", moreFollows ? 1 : 0);
            content.res(map);
        } else {
            // Subsequent pages: merge into existing result
            ((List<Object>) ((Map<String, Object>) content.res()).get(listField)).addAll(pageList);
            ((Map<String, Object>) content.res()).put("moreFollows", moreFollows ? 1 : 0);
        }

        // Update pagination context
        PaginationContext ctx = content.paginationContext();
        ctx.setLastMoreFollows(moreFollows);
        if (!pageList.isEmpty()) {
            Object lastItem = pageList.get(pageList.size() - 1);
            if (lastItem instanceof String) {
                ctx.setLastReference((String) lastItem);
            } else if (lastItem instanceof Map) {
                Object ref = ((Map<?, ?>) lastItem).get("reference");
                ctx.setLastReference(ref != null ? ref.toString() : null);
            }
        }
    }

    /**
     * Fluent helper for pagination bookkeeping inside
     * {@link BaseClientHandler#onSuccess}.
     * <p>
     * Wraps the {@link CmsContent#res()} map (data accumulation) and
     * {@link CmsContent#paginationContext()} (cursor tracking) so subclasses don't
     * need to deal with raw casts and state management.
     */
    public static class PaginationHelper {
        private final PaginationContext ctx;
        private final Map<String, Object> map;

        PaginationHelper(CmsContent<?> content) {
            this.ctx = content.paginationContext();
            this.map = (Map<String, Object>) content.res();
        }

        /**
         * Append data to the accumulated list for the given field name. The map must
         * have been initialized with an empty list via
         * {@link #initResult(CmsContent, String)}.
         */
        @SuppressWarnings("unchecked")
        public <T> PaginationHelper add(String field, List<T> data) {
            ((List<T>) map.get(field)).addAll(data);
            return this;
        }

        /**
         * Set the moreFollows flag in both the result map and the pagination context.
         * The map stores {@code 0} or {@code 1} (integer) for JSON output.
         */
        public PaginationHelper moreFollows(boolean value) {
            map.put("moreFollows", value ? 1 : 0);
            ctx.setLastMoreFollows(value);
            return this;
        }

        /**
         * Set the last reference cursor from the last item in the list. No-op if the
         * list is empty.
         */
        public <T> PaginationHelper lastRef(List<T> items, Function<T, String> extractor) {
            if (!items.isEmpty()) {
                ctx.setLastReference(extractor.apply(items.get(items.size() - 1)));
            }
            return this;
        }
    }
}
