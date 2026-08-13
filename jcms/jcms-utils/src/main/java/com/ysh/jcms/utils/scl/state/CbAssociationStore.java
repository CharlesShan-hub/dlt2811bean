package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.core.data.core.CmsSequence;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Control block state store at the ASSOCIATION layer: concurrent storage isolated by session (sessionId).
 * <p>
 * Lifecycle: cleared when the connection is closed ({@link #removeSession(String)}), corresponding to per-association
 * instance fields such as URCB ({@code @CbField(scope = CbFieldScope.ASSOCIATION)}).
 */
public final class CbAssociationStore<T extends CmsSequence> {

    private final ConcurrentMap<String, ConcurrentMap<String, T>> bySession = new ConcurrentHashMap<>();

    private ConcurrentMap<String, T> bucket(String sessionId) {
        return bySession.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
    }

    /** Gets the state of the given reference under the given session; returns {@code null} if not set. */
    public T get(String sessionId, String ref) {
        ConcurrentMap<String, T> b = bySession.get(sessionId);
        return b != null ? b.get(ref) : null;
    }

    /** Gets or creates the state of the given reference under the given session. */
    public T getOrCreate(String sessionId, String ref, Supplier<T> factory) {
        return bucket(sessionId).computeIfAbsent(ref, k -> factory.get());
    }

    /** Writes/replaces the state of the given reference under the given session. */
    public void put(String sessionId, String ref, T cb) {
        bucket(sessionId).put(ref, cb);
    }

    /** Removes the state of the given reference under the given session. */
    public void remove(String sessionId, String ref) {
        ConcurrentMap<String, T> b = bySession.get(sessionId);
        if (b != null) {
            b.remove(ref);
        }
    }

    /** Removes all association-level state of the whole session (association release hook). */
    public void removeSession(String sessionId) {
        bySession.remove(sessionId);
    }

    /** Clears the state of all sessions. */
    public void clear() {
        bySession.clear();
    }
}
