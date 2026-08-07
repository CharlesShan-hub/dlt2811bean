package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.PaginationContext;

/**
 * Pagination context for {@code ln-dir} requests.
 * <p>
 * Carries the ACSI class in addition to standard pagination state, so that
 * {@link LnDirClient} can pass it through without ThreadLocal.
 */
public class LnDirContext extends PaginationContext {

    private int acsiClass;

    public int getAcsiClass() {
        return acsiClass;
    }

    public void setAcsiClass(int acsiClass) {
        this.acsiClass = acsiClass;
    }
}
