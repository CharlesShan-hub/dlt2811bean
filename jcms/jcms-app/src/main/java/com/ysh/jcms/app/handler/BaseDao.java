package com.ysh.jcms.app.handler;

import com.ysh.jcms.data.core.CmsType;

/**
 * Base class for all DAO (Data Access Object) types used by client handlers.
 *
 * <p>
 * Provides a common supertype so that {@link BaseClientHandler} can declare a
 * generic {@code execute(D dao)} method with a bounded type parameter.
 *
 * <p>
 * Subclasses may override {@link #toRequest()} to build the corresponding
 * request object, enabling {@code send(ServiceName.XXX, dao.toRequest())} in
 * client handlers.
 */
public abstract class BaseDao {

    /** Whether the client should automatically follow moreFollows pagination. */
    private boolean autoPull = false;

    /**
     * Result holder for passing decoded response data back to the caller. Each DAO
     * subclass decides what type goes in here.
     */
    private Object result;

    /**
     * Pagination context for auto-pull support. When non-null,
     * {@link BaseClientHandler#send} will automatically follow {@code moreFollows}
     * pagination.
     */
    private PaginationContext paginationContext;

    public boolean autoPull() {
        return autoPull;
    }

    public void autoPull(boolean autoPull) {
        this.autoPull = autoPull;
    }

    public Object result() {
        return result;
    }

    public BaseDao result(Object result) {
        this.result = result;
        return this;
    }

    public PaginationContext paginationContext() {
        return paginationContext;
    }

    public void paginationContext(PaginationContext paginationContext) {
        this.paginationContext = paginationContext;
    }

    /**
     * Build a request object from this DAO's parameters.
     *
     * <p>
     * The default implementation throws {@link UnsupportedOperationException} so
     * that existing DAOs continue to compile without implementing this method.
     * Override to enable the {@code dao.toRequest()} pattern.
     *
     * @return the request object (never {@code null})
     */
    public CmsType toRequest() {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not implement toRequest()");
    }
}
