package com.ysh.jcms.app.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 全局分页工具类，提供分页相关的辅助方法。
 * <p>
 * 封装了 {@link PaginationContext} 和 DAO 的 result map 操作，使得 Client 无需 直接处理类型强转、ctx
 * 状态管理等脏活。
 * <p>
 * 用法（在 {@link BaseClientHandler#onSuccess} 中）：
 *
 * <pre>
 * {@code
 * // 初始化结果 map（在 beforeAll 中）
 * CmsClientOperator.initResult(dao, "reference");
 *
 * // 每页处理（在 onSuccess 中）
 * CmsClientOperator.page(dao).add("reference", resp.reference)
 *                           .moreFollows(resp.moreFollows.value())
 *                           .lastRef(resp.reference, CmsObjectReference::value);
 *
 * // 获取结果（在 afterAll 中）
 * List<CmsObjectReference> refs = CmsClientOperator.getResultList(dao, "reference");
 * }
 * </pre>
 */
public final class CmsClientOperator {

    private CmsClientOperator() {
    }

    /**
     * 初始化 DAO 的 result map，包含一个指定字段的空列表。 等价于 {@code beforeAll} 中创建 {@code {"field":
     * []}} 的结构。
     */
    public static void initResult(BaseDao dao, String dataField) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(dataField, new ArrayList<>());
        dao.result(map);
    }

    /**
     * 从 DAO 的 result map 中获取指定字段的列表（带类型转换）。
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getResultList(BaseDao dao, String field) {
        return (List<T>) ((Map<String, Object>) dao.result()).get(field);
    }

    /**
     * 创建 {@link PaginationHelper} 实例，用于在 {@code onSuccess} 中 以 fluent 链式调用记录分页数据。
     */
    public static PaginationHelper page(BaseDao dao) {
        return new PaginationHelper(dao);
    }

    /**
     * Fluent helper for pagination bookkeeping inside
     * {@link BaseClientHandler#onSuccess}.
     * <p>
     * Wraps the DAO's result map (data accumulation) and pagination context (cursor
     * tracking) so subclasses don't need to deal with raw casts and state
     * management.
     */
    public static class PaginationHelper {
        private final PaginationContext ctx;
        private final Map<String, Object> map;

        PaginationHelper(BaseDao dao) {
            this.ctx = dao.paginationContext();
            this.map = (Map<String, Object>) dao.result();
        }

        /**
         * Append data to the accumulated list for the given field name. The map must
         * have been initialized with an empty list via
         * {@link #initResult(BaseDao, String)}.
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
