package com.ysh.jcms.core.data.core;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    // ── JSON — Internal (JER) ────────────────────────────────────────

    /**
     * Build a CmsType instance from its JER JSON representation (internal, for
     * Rust FFI interchange). Most callers should use {@link #fromJson(Class, String)}
     * instead, which works with the human-friendly domain JSON format.
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
    public static <T extends CmsType> T fromInnerJson(Class<?> type, String json) {
        try {
            T cms = (T) type.getDeclaredConstructor().newInstance();
            InnerBase inner = InnerBase.MAPPER.readValue(json, cms.inner.getClass());
            cms.inner = inner;
            cms.rebind();
            cms.syncFromInner();
            return cms;
        } catch (Exception e) {
            throw new RuntimeException("fromInnerJson failed for " + type.getSimpleName() + ": " + json, e);
        }
    }

    // ── JSON — Domain (human-friendly) ───────────────────────────────

    private static final ObjectMapper DOMAIN_MAPPER = new ObjectMapper();

    /**
     * Serialize this CmsType to a human-friendly domain JSON string.
     * <p>
     * Unlike {@link #fromInnerJson(Class, String)} which uses JER format
     * (ASN.1 types), domain JSON presents Java-field-level values:
     * <pre>
     * CmsUtcTime → {"secondsSinceEpoch": 1234567890, "fractionOfSecond": 500000, "timeQuality": {...}}
     * CmsFileEntry → {"fileName": "a.txt", "fileSize": 1024, ...}
     * CmsData → {"int32": 42}
     * </pre>
     */
    public String toJson() {
        try {
            return DOMAIN_MAPPER.writeValueAsString(toJsonValue());
        } catch (Exception e) {
            throw new RuntimeException("toJson failed for " + getClass().getSimpleName(), e);
        }
    }

    /**
     * Build a CmsType instance from a human-friendly domain JSON string.
     *
     * @param type
     *            the concrete CmsType subclass (must have a no-arg constructor)
     * @param json
     *            domain JSON (e.g. {@code {"int32": 42}} for a CmsData,
     *            {@code 42} for a scalar, {@code {"fileName": "a.txt"}} for a
     *            CmsFileEntry)
     * @return the populated instance
     * @throws RuntimeException
     *             if construction or deserialization fails
     */
    @SuppressWarnings("unchecked")
    public static <T extends CmsType> T fromJson(Class<?> type, String json) {
        try {
            T cms = (T) type.getDeclaredConstructor().newInstance();
            Object value = DOMAIN_MAPPER.readValue(json, Object.class);
            cms.fromJsonValue(value);
            return cms;
        } catch (Exception e) {
            throw new RuntimeException("fromJson failed for " + type.getSimpleName() + ": " + json, e);
        }
    }

    /**
     * Return the value to serialize in domain JSON. Subclasses override this
     * to produce their field-level representation.
     */
    public Object toJsonValue() {
        return toString();
    }

    /**
     * Restore field values from a parsed domain JSON value. Subclasses override
     * this to unpack their field-level representation.
     */
    public void fromJsonValue(Object value) {
        // no-op by default
    }

    /**
     * Rebind wrapper _v after inner is replaced (e.g. after decode or fromJson).
     * CmsSequence and CmsChoice override this to re-establish _v sharing.
     */
    public void rebind() {
        // no-op for scalar / leaf types
    }

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
