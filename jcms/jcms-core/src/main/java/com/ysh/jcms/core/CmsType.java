package com.ysh.jcms.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Base class for all CMS wrapper types that use Rust-based PER encoding.
 *
 * <p>Subclasses fall into two categories:
 * <ul>
 *   <li><b>PDU types</b> — wrap an auto-generated Inner*PDU (from {@code jcms-data})
 *       via {@code super(new InnerXxxPDU())}. These have real
 *       {@link #encode()} / {@link #decode(byte[])} backed by Rust FFI.</li>
 *   <li><b>Container types</b> — use the no-arg {@code CmsType()} constructor
 *       which defaults to {@link InnerEmpty}. These are field holders within
 *       larger PDUs; encode/decode is handled by the parent PDU.</li>
 * </ul>
 *
 * <p>{@link #equals(Object)} and {@link #hashCode()} use reflection to
 * compare all public (non-static, non-{@code inner}) fields automatically.
 * Nested {@code CmsType} fields recurse; {@code byte[]} fields use
 * {@link java.util.Arrays#equals(byte[], byte[])}; {@link List} fields
 * are compared element-by-element with the same rules.
 */
public abstract class CmsType {
    /** The Inner* instance backing this wrapper. */
    public InnerBase inner;

    /** Unified data cache: {@code "value" → inner} for scalars,
     *  {@code fieldName → value/wrapper} for sequences. */
    public final java.util.Map<String, Object> innerCache = new java.util.HashMap<>();

    /** Cached reflection handle for inner.encode(). */
    private final Method encodeMethod;
    /** Cached reflection handle for InnerXxx.decode(byte[]). */
    private final Method staticDecodeMethod;

    /**
     * Creates a CmsType with an {@link InnerEmpty} placeholder.
     * Used by container types that do not have a standalone ASN.1 PDU.
     */
    protected CmsType() {
        this(new InnerEmpty());
    }

    /**
     * Creates a CmsType backed by the given Inner* instance.
     *
     * @param inner the auto-generated Inner* PDU type (must have
     *              {@code encode()} instance method and
     *              {@code static decode(byte[])} method)
     * @throws RuntimeException if the required methods are not found
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

    // ── encode / decode ──────────────────────────────────────────────────

    /**
     * Encodes this wrapper to APER bytes.
     * Calls {@link #syncToInner()} first to push field values into the
     * backing Inner*, then delegates to {@code Inner*.encode()} via Rust FFI.
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
     * Calls {@code InnerXxx.decode(data)} via Rust FFI to populate the
     * backing Inner*, then {@link #syncFromInner()} to pull values out.
     */
    public void decode(byte[] data) {
        try {
            inner = (InnerBase) staticDecodeMethod.invoke(null, data);
            syncFromInner();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ── sync hooks ───────────────────────────────────────────────────────

    /** Push field values from this wrapper into the backing Inner* PDU. */
    public void syncToInner() {}

    /** Pull field values from the backing Inner* PDU into this wrapper. */
    public void syncFromInner() {}

    // ── equals / hashCode ────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return fieldsEqual(this, o);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Field f : getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            if (f.getName().equals("inner")) continue;
            Object val;
            try { val = f.get(this); } catch (Exception e) { continue; }
            if (val == null) continue;
            h = 31 * h + fieldHash(val);
        }
        return h;
    }

    // ── private helpers ─────────────────────────────────────────────────

    private static boolean fieldsEqual(Object a, Object b) {
        for (Field f : a.getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            if (f.getName().equals("inner")) continue;
            Object va, vb;
            try { va = f.get(a); vb = f.get(b); } catch (Exception e) { return false; }
            if (!fieldEquals(va, vb)) return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private static boolean fieldEquals(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a instanceof CmsType && b instanceof CmsType)
            return fieldsEqual(a, b);
        if (a instanceof byte[] && b instanceof byte[])
            return java.util.Arrays.equals((byte[]) a, (byte[]) b);
        if (a instanceof List && b instanceof List) {
            List<Object> la = (List<Object>) a, lb = (List<Object>) b;
            if (la.size() != lb.size()) return false;
            for (int i = 0; i < la.size(); i++) {
                if (!fieldEquals(la.get(i), lb.get(i))) return false;
            }
            return true;
        }
        return a.equals(b);
    }

    private static int fieldHash(Object val) {
        if (val instanceof CmsType) return ((CmsType) val).hashCode();
        if (val instanceof byte[]) return java.util.Arrays.hashCode((byte[]) val);
        if (val instanceof List) {
            int h = 0;
            for (Object v : (List<?>) val) h = 31 * h + (v == null ? 0 : fieldHash(v));
            return h;
        }
        return val.hashCode();
    }

    // ── toString ─────────────────────────────────────────────────────────

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
                sb.append(bytesToHex((byte[]) val)).append("\n");
            } else if (val instanceof List && !((List<?>) val).isEmpty()
                    && ((List<?>) val).get(0) instanceof CmsType) {
                sb.append("[").append(((List<?>) val).size()).append("]\n");
            } else {
                sb.append(val).append("\n");
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
