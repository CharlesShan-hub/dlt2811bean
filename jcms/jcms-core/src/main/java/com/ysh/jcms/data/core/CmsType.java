package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Root of the Cms* wrapper hierarchy.
 *
 * <p>A CmsType wraps an auto-generated {@code Inner*} instance (from jcms-data),
 * forming a two-layer structure:
 * <ul>
 *   <li><b>Inner* tree</b> — the raw data nodes ({@link InnerBase} subclasses).
 *       Rust FFI encodes/decodes this tree directly via JSON interchange.</li>
 *   <li><b>innerCache</b> — a {@code Map<String, Object>} that mirrors the Inner* tree
 *       for fast access. {@link #equals(Object)} now compares Inner* instances directly.</li>
 * </ul>
 *
 * <p>Two kinds of subclasses:
 * <ul>
 *   <li><b>PDU types</b> — constructed with {@code super(new InnerXxxPDU())}.
 *       Have real {@link #encode()} / {@link #decode(byte[])} backed by Rust.</li>
 *   <li><b>Container types</b> — constructed with the no-arg constructor,
 *       backed by {@link InnerEmpty}. These are field holders embedded in a parent PDU.</li>
 * </ul>
 */
public abstract class CmsType {

    // ── Core fields ──────────────────────────────────────────────────

    /** The Inner* instance backing this wrapper. */
    public InnerBase inner;

    /**
     * Data cache that mirrors the Inner* tree.
     *
     * <p>Key semantics by subclass:
     * <ul>
     *   <li>{@link CmsScalar} — {@code "value" → the actual value}</li>
     *   <li>{@link CmsSequence} — {@code fieldName → child wrapper's innerCache}</li>
     *   <li>{@link CmsChoice} — {@code "choice" → current variant index}</li>
     *   <li>OPTIONAL fields — {@code "hasFieldName" → Boolean}</li>
     * </ul>
     */
    public final java.util.Map<String, Object> innerCache = new java.util.HashMap<>();

    /** Cached reflection handle for {@code Inner*.encode()}. */
    private final Method encodeMethod;
    /** Cached reflection handle for {@code Inner*.decode(byte[])}. */
    private final Method staticDecodeMethod;

    // ── Constructors ─────────────────────────────────────────────────

    /** Creates a container type backed by {@link InnerEmpty}. */
    protected CmsType() {
        this(new InnerEmpty());
    }

    /**
     * Creates a PDU type backed by the given Inner* instance.
     *
     * <p>Reflection-caches {@code encode()} and {@code static decode(byte[])}
     * on the Inner* class at construction time.
     */
    protected CmsType(InnerBase inner) {
        this.inner = inner;
        try {
            this.encodeMethod = inner.getClass().getMethod("encode");
            this.staticDecodeMethod = inner.getClass().getMethod("decode", byte[].class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // ── Encode / decode ──────────────────────────────────────────────

    /**
     * Encodes this wrapper to APER bytes.
     *
     * <p>Calls {@link #syncToInner()} first to push cache values into the
     * Inner* tree, then delegates to {@code Inner*.encode()} via Rust FFI.
     */
    public byte[] encode() {
        syncToInner();
        try {
            return (byte[]) encodeMethod.invoke(inner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decodes APER bytes into this wrapper.
     *
     * <p>Rust FFI decodes the bytes, Jackson creates a fresh Inner* tree,
     * then {@link #syncFromInner()} pulls values back into the cache.
     */
    public void decode(byte[] data) {
        try {
            inner = (InnerBase) staticDecodeMethod.invoke(null, data);
            syncFromInner();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ── Sync hooks (subclasses override) ─────────────────────────────

    /** Pushes innerCache values into the Inner* tree before encode. */
    public void syncToInner() {}

    /** Pulls values from a fresh Inner* tree into innerCache after decode. */
    public void syncFromInner() {}

    // ── Object overrides ─────────────────────────────────────────────

    /**
     * <p>{@link #equals(Object)} compares the underlying Inner* instances directly,
     * avoiding the null-vs-default-value problem of Lombok-generated equals.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof CmsType)) return false;
        return inner.equals(((CmsType) o).inner);
    }

    @Override
    public int hashCode() {
        return inner.hashCode();
    }

    /**
     * Returns a human-readable representation of this wrapper and its
     * public CmsType fields, recursively.
     */
    @Override
    public String toString() {
        return toString(0).trim();
    }

    private String toString(int depth) {
        String indent = repeat("    ", depth);
        String indent1 = repeat("    ", depth + 1);
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(getClass().getSimpleName()).append(")\n");
        for (Field f : getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            if (f.getName().equals("inner")) continue;
            Object val;
            try { val = f.get(this); } catch (Exception e) { continue; }
            if (val == null) continue;
            sb.append(indent1).append(f.getName()).append(": ");
            if (val instanceof CmsType) {
                sb.append(((CmsType) val).toString(depth + 1));
            } else if (val instanceof byte[]) {
                sb.append(bytesToHex((byte[]) val)).append('\n');
            } else if (val instanceof List && !((List<?>) val).isEmpty()
                    && ((List<?>) val).get(0) instanceof CmsType) {
                sb.append('[').append(((List<?>) val).size()).append("]\n");
            } else {
                sb.append(val).append('\n');
            }
        }
        return sb.toString();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder("0x");
        for (byte b : bytes) sb.append(String.format("%02X", b & 0xFF));
        return sb.toString();
    }

    private static String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder(s.length() * count);
        for (int i = 0; i < count; i++) sb.append(s);
        return sb.toString();
    }
}
