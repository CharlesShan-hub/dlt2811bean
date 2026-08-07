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

    public boolean autoPull() {
        return autoPull;
    }

    public void autoPull(boolean autoPull) {
        this.autoPull = autoPull;
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
