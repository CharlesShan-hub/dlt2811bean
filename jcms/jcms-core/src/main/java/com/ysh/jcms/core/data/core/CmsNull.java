package com.ysh.jcms.core.data.core;

/**
 * A NULL marker type — no payload, no inner fields.
 * <p>
 * Useful as a placeholder return value for
 * {@link com.ysh.jcms.app.handler.BaseDao#toRequest()} when the DAO has no
 * request body to build (e.g. test/ping commands).
 */
public class CmsNull extends CmsType {

    public CmsNull() {
        super();
    }
}
