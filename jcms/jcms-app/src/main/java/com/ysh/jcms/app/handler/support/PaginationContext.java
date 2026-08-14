package com.ysh.jcms.app.handler.support;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination context for request-level state passing.
 * <p>
 * Each request creates its own context, eliminating the need for instance
 * variables or ThreadLocal in Client handlers. This makes handlers stateless
 * and thread-safe.
 */
public class PaginationContext {

    /** Accumulated references (used by SvrDir, LdDir, LnDir, etc.). */
    private final List<String> accumulatedRefs = new ArrayList<>();

    /** Whether the last response has more pages. */
    private boolean lastMoreFollows;

    /** Reference cursor for the next page request. */
    private String lastReference;

    /**
     * Generic result holder for subclasses that need to pass accumulated result
     * data (e.g. CbEntry list, DirEntry list). Caller is responsible for the
     * correct type cast.
     */
    private Object result;

    // ── accumulatedRefs ──

    public List<String> getAccumulatedRefs() {
        return accumulatedRefs;
    }

    // ── lastMoreFollows ──

    public boolean isLastMoreFollows() {
        return lastMoreFollows;
    }

    public void setLastMoreFollows(boolean lastMoreFollows) {
        this.lastMoreFollows = lastMoreFollows;
    }

    // ── lastReference ──

    public String getLastReference() {
        return lastReference;
    }

    public void setLastReference(String lastReference) {
        this.lastReference = lastReference;
    }

    // ── result ──

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /** Reset all state for reuse. */
    public void reset() {
        accumulatedRefs.clear();
        lastMoreFollows = false;
        lastReference = null;
        result = null;
    }
}
