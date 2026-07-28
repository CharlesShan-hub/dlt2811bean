package com.ysh.jcms.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.util.CmsEqualUtil;
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
 * <p>{@link #equals(Object)} and {@link #hashCode()} delegate to the
 * backing {@link #inner} object's Lombok-generated {@code equals/hashCode}
 * after calling {@link #syncToInner()} on both sides.
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
        if (o == null || !(o instanceof CmsType)) return false;
        return CmsEqualUtil.equal(this, (CmsType) o);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
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
