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
 * Subclasses <b>must</b> override {@link #toRequest()} to build the
 * corresponding request object, enabling
 * {@code send(ServiceName.XXX, dao.toRequest())} in client handlers.
 *
 * <p>
 * This class is intentionally minimal — only request-building logic lives here.
 * Execution parameters ({@code autoPull}, response data, pagination state) are
 * carried by {@link CmsContent}.
 */
public abstract class BaseDao {

    /**
     * Build a request object from this DAO's parameters.
     *
     * @return the request object (never {@code null})
     */
    public abstract CmsType toRequest();
}
