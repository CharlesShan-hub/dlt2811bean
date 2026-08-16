package com.ysh.jcms.core.data.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;

/**
 * Root of the Cms* wrapper hierarchy.
 *
 * <p>
 * A CmsType wraps an auto-generated {@code Inner*} instance (from jcms-data).
 * The Inner* tree (backed by {@link InnerBase#_v}) is the single source of
 * truth; encode/decode operate directly on it via Rust FFI and JSON
 * interchange.
 *
 * <p>
 * Two kinds of subclasses:
 * <ul>
 * <li><b>PDU types</b> — constructed with {@code super(new InnerXxx())}. Have
 * real {@link #encode()} / {@link #decode(byte[])} backed by Rust.</li>
 * <li><b>Container types</b> — constructed with the no-arg constructor, backed
 * by {@link InnerEmpty}. These are field holders embedded in a parent PDU.</li>
 * </ul>
 */
public abstract class CmsType {

    /** The Inner* instance backing this wrapper. */
    public InnerBase inner;

    /**
     * Static decode(byte[]) method per Inner class — cached (reflection lookup on
     * every decode is wasteful).
     */
    private static final ClassValue<java.lang.reflect.Method> DECODE_METHOD = new ClassValue<java.lang.reflect.Method>() {
        @Override
        protected java.lang.reflect.Method computeValue(Class<?> type) {
            try {
                return type.getMethod("decode", byte[].class);
            } catch (Exception e) {
                throw new IllegalStateException("No static decode(byte[]) on " + type.getName(), e);
            }
        }
    };

    /** Creates a container type backed by {@link InnerEmpty}. */
    protected CmsType() {
        this(new InnerEmpty());
    }

    /** Creates a PDU type backed by the given Inner* instance. */
    protected CmsType(InnerBase inner) {
        this.inner = inner;
    }

    // ── Encode / decode ──────────────────────────────────────────────

    /**
     * Encodes this wrapper to APER bytes via Rust FFI. Delegates directly to
     * {@code Inner*.encode()}.
     */
    public byte[] encode() {
        return inner.encode();
    }

    /**
     * Decodes APER bytes into this wrapper via Rust FFI. Replaces the backing
     * Inner* instance with a freshly decoded one.
     */
    public void decode(byte[] data) {
        try {
            java.lang.reflect.Method m = DECODE_METHOD.get(inner.getClass());
            inner = (InnerBase) m.invoke(null, (Object) data);
        } catch (Exception e) {
            throw new RuntimeException(
                    "decode failed for " + inner.getClass().getSimpleName() + (data != null ? ", dataLen=" + data.length : ""), e);
        }
    }

    /**
     * Build a CmsType instance from its JER JSON representation.
     * <p>
     * The backing Inner* is populated via Jackson deserialization — the
     * generated Inner classes carry the necessary {@code @JsonCreator} /
     * {@code @JsonSetter} hooks, so scalars, sequences and choices all work.
     *
     * @param type
     *            the concrete CmsType subclass (must have a no-arg constructor)
     * @param json
     *            JER JSON of the value (e.g. {@code {"visible-string": "abc"}}
     *            for a CHOICE, {@code 42} for a scalar, {@code {"field": 0}} for
     *            a SEQUENCE)
     * @return the populated instance
     * @throws RuntimeException
     *             if construction or deserialization fails
     */
    @SuppressWarnings("unchecked")
    public static <T extends CmsType> T fromJson(Class<?> type, String json) {
        try {
            T cms = (T) type.getDeclaredConstructor().newInstance();
            InnerBase inner = InnerBase.MAPPER.readValue(json, cms.inner.getClass());
            cms.inner = inner;
            return cms;
        } catch (Exception e) {
            throw new RuntimeException("fromJson failed for " + type.getSimpleName() + ": " + json, e);
        }
    }

    /**
     * Push wrapper state into the Inner* tree before encode. Subclasses with Java
     * fields that need packing (e.g. CmsBits) override this.
     */
    public void syncToInner() {
    }

    /**
     * Pull values from Inner* tree into wrapper fields after decode. Subclasses
     * with Java fields that need unpacking override this.
     */
    public void syncFromInner() {
    }

    // ── Object overrides ─────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !(o instanceof CmsType))
            return false;
        return inner.equals(((CmsType) o).inner);
    }

    @Override
    public int hashCode() {
        return inner.hashCode();
    }

    @Override
    public String toString() {
        return inner.toString();
    }
}
