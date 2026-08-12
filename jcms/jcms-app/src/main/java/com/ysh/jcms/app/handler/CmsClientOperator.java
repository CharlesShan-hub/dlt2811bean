package com.ysh.jcms.app.handler;

import com.ysh.jcms.core.data.core.CmsType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 全局分页工具类，提供分页相关的辅助方法。
 * <p>
 * 封装了 {@link PaginationContext} 和 {@link CmsContent#res()} 的 map 操作， 使得 Client
 * 无需直接处理类型强转、ctx 状态管理等脏活。
 * <p>
 * 用法（在 {@link BaseClientHandler#onSuccess} 中）：
 *
 * <pre>
 * {
 *     &#64;code
 *     CmsContent<?> c = content();
 *
 *     // 初始化结果 map（在 beforeAll 中）
 *     CmsClientOperator.initResult(c, "reference");
 *
 *     // 每页处理（在 onSuccess 中）
 *     CmsClientOperator.page(c).add("reference", resp.reference).moreFollows(resp.moreFollows.value()).lastRef(resp.reference,
 *             CmsObjectReference::value);
 *
 *     // 获取结果（在 afterAll 中）
 *     List<CmsObjectReference> refs = CmsClientOperator.getResultList(c, "reference");
 * }
 * </pre>
 */
public final class CmsClientOperator {

    private CmsClientOperator() {
    }

    /**
     * 初始化 {@link CmsContent#res()} 的 map，包含一个指定字段的空列表。 等价于 {@code beforeAll} 中创建
     * {@code {"field": []}} 的结构。
     */
    public static void initResult(CmsContent<?> content, String dataField) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(dataField, new ArrayList<>());
        content.res(map);
    }

    /**
     * 从 {@link CmsContent#res()} 的 map 中获取指定字段的列表（带类型转换）。
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getResultList(CmsContent<?> content, String field) {
        return (List<T>) ((Map<String, Object>) content.res()).get(field);
    }

    /**
     * 创建 {@link PaginationHelper} 实例，用于在 {@code onSuccess} 中 以 fluent 链式调用记录分页数据。
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
     *     CmsGetServerDirectoryResponse resp = decodeResp(frame, new CmsGetServerDirectoryResponse());
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
