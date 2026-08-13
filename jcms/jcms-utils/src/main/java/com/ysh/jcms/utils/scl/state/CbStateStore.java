package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.core.data.core.CmsSequence;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Control block state store at the RUNTIME layer: in-process concurrent storage isolated by ref.
 * <p>
 * Lifecycle: written by Set and read with priority by Get; survives client disconnection, lost on server restart
 * (falls back to the SCL engineering values).
 */
public final class CbStateStore<T extends CmsSequence> {

    private final ConcurrentMap<String, T> state = new ConcurrentHashMap<>();

    /** Gets the runtime state of the given reference; returns {@code null} if not set. */
    public T get(String ref) {
        return state.get(ref);
    }

    /** Gets or creates the runtime state of the given reference. */
    public T getOrCreate(String ref, Supplier<T> factory) {
        return state.computeIfAbsent(ref, k -> factory.get());
    }

    /** Writes/replaces the runtime state of the given reference. */
    public void put(String ref, T cb) {
        state.put(ref, cb);
    }

    /** Removes the runtime state of the given reference. */
    public void remove(String ref) {
        state.remove(ref);
    }

    /** Clears all runtime state. */
    public void clear() {
        state.clear();
    }
}
