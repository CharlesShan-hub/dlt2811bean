package com.ysh.jcms.app.handler.support;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;

/**
 * 请求/响应结果容器，用于在 Client 和 Console 之间传递数据。
 * <p>
 * Console 创建 {@code CmsContent} 并传入 DAO，然后调用
 * {@link BaseClientHandler#executeResult(CmsContent)} 执行请求， 执行完成后通过
 * {@link #res()} 获取响应数据。
 * <p>
 * 除 {@code req}（DAO，请求参数）外，所有执行相关状态（autoPull、分页上下文、响应数据）均在此类中管理， DAO 仅负责
 * {@link BaseDao#toRequest()} 构建请求对象。
 *
 * <pre>
 * {
 *     &#64;code
 *     CmsContent<SvrDirDao> c = new CmsContent<>(new SvrDirDao().referenceAfter("..."), "true");
 *     console.getClient(SvrDirClient.class).executeResult(c);
 *     CmsPrinter.outputJson(c.res());
 * }
 * </pre>
 */
public class CmsContent<D extends BaseDao> {

    private final D req;
    private Object res;
    private PaginationContext paginationContext;
    private boolean autoPull;

    public CmsContent(D req) {
        this.req = req;
    }

    /** Create with req and auto-pull flag from a CLI argument string. */
    public CmsContent(D req, String autoPull) {
        this.req = req;
        this.autoPull = "true".equalsIgnoreCase(autoPull);
    }

    /** The request DAO (carries request parameters). */
    public D req() {
        return req;
    }

    /** Response data, populated by {@link BaseClientHandler#executeResult}. */
    public Object res() {
        return res;
    }

    /** Set response data (called by {@code BaseClientHandler}). */
    public void res(Object res) {
        this.res = res;
    }

    /** Pagination context for auto-pull support. */
    public PaginationContext paginationContext() {
        if (paginationContext == null) {
            paginationContext = new PaginationContext();
        }
        return paginationContext;
    }

    /** Set pagination context (called by {@code BaseClientHandler}). */
    void paginationContext(PaginationContext paginationContext) {
        this.paginationContext = paginationContext;
    }

    /** Whether the client should automatically follow moreFollows pagination. */
    public boolean autoPull() {
        return autoPull;
    }

    /** Enable auto-pull. */
    public void autoPull(boolean autoPull) {
        this.autoPull = autoPull;
    }

    /**
     * Enable auto-pull from a CLI argument string. Accepts "true"
     * (case-insensitive); any other value (including null) is treated as false.
     */
    public void autoPull(String autoPull) {
        this.autoPull = "true".equalsIgnoreCase(autoPull);
    }

    /** True if the last pagination response indicated more data follows. */
    public boolean moreFollows() {
        return paginationContext != null && paginationContext.isLastMoreFollows();
    }
}
