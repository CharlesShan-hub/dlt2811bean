package com.ysh.jcms.core;

import com.ysh.jcms.data.InnerBase;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Base class for SEQUENCE types backed directly by an Inner* PDU.
 *
 * <p>Subclasses declare public CmsType fields annotated with {@link InnerField}.
 * The base class constructor automatically creates wrapper instances, binds
 * their {@code inner} reference to the corresponding Inner* field, and shares
 * their {@code innerCache} under the parent's cache.
 *
 * <p>{@link #syncToInner()} / {@link #syncFromInner()} automatically sync all
 * injected wrappers and presence flags — subclasses generally do NOT need to
 * override them.
 *
 * <pre>{@code
 * public class CmsBrcb extends CmsSequence {
 *     @InnerField public CmsBoolean rptEna;
 *     @InnerField(optional = true) public CmsInt16 resvTms;
 *     // ...
 *     public CmsBrcb() { super(new InnerBRCB()); }
 * }
 * }</pre>
 */
public abstract class CmsSequence extends CmsType {

    private final Map<String, CmsType> injectedWrappers = new LinkedHashMap<>();
    /** Field names annotated with {@code @InnerField(optional = true)}. */
    private final Set<String> optionalFields = new HashSet<>();

    protected CmsSequence() {}

    @SuppressWarnings("unchecked")
    protected CmsSequence(InnerBase inner) {
        super(inner);
        injectFields();
    }

    // ── @InnerField injection ──────────────────────────────────────────

    private void injectFields() {
        for (Field f : getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            InnerField ann = f.getAnnotation(InnerField.class);
            if (ann == null) continue;
            if (!CmsType.class.isAssignableFrom(f.getType())) continue;

            Class<? extends CmsType> wrapperType = (Class<? extends CmsType>) f.getType();
            String fieldName = f.getName();

            try {
                CmsType wrapper = wrapperType.getDeclaredConstructor().newInstance();
                Field innerField = inner.getClass().getField(fieldName);

                // If the Inner* field is an InnerBase subtype, share the reference
                if (InnerBase.class.isAssignableFrom(innerField.getType())) {
                    wrapper.inner = (InnerBase) innerField.get(inner);
                    // NOTE: syncFromInner is NOT called here — compound types
                    // (e.g. CmsBinaryTime) may not have valid data yet during
                    // construction. syncFromInner is called in rebindWrappers()
                    // after decode, and scalar types populate their cache in
                    // CmsScalar's constructor.
                }

                // share innerCache — parent's innerCache[fieldName] = wrapper.innerCache
                innerCache.put(fieldName, wrapper.innerCache);

                // set the field on this instance
                f.set(this, wrapper);
                injectedWrappers.put(fieldName, wrapper);

                if (ann.optional()) {
                    optionalFields.add(fieldName);
                }
            } catch (NoSuchFieldException e) {
                // Inner* doesn't have this field — skip silently
            } catch (Exception e) {
                throw new RuntimeException("Failed to inject @InnerField " + fieldName, e);
            }
        }
    }

    /** Rebind wrapper inner references after decode creates a new Inner*. */
    private void rebindWrappers() {
        for (Map.Entry<String, CmsType> entry : injectedWrappers.entrySet()) {
            try {
                Field innerField = inner.getClass().getField(entry.getKey());
                CmsType wrapper = entry.getValue();
                if (InnerBase.class.isAssignableFrom(innerField.getType())) {
                    wrapper.inner = (InnerBase) innerField.get(inner);
                    wrapper.syncFromInner();
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to rebind " + entry.getKey(), e);
            }
        }
    }

    // ── presence API ─────────────────────────────────────────────────

    /** Presence key in innerCache for an optional field. */
    private static String presKey(String fieldName) {
        return "has" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    /** Mark an optional field as present/absent. */
    protected void setPresent(String innerFieldName, boolean v) {
        innerCache.put(presKey(innerFieldName), v);
    }

    /** Check if an optional field is present. */
    protected boolean isPresent(String fieldName) {
        return Boolean.TRUE.equals(innerCache.get(presKey(fieldName)));
    }

    /** Look up Inner* {@code _set} field dynamically (declared on each subclass). */
    @SuppressWarnings("unchecked")
    private Set<String> innerSetField() {
        try {
            Field f = inner.getClass().getField("_set");
            return (Set<String>) f.get(inner);
        } catch (NoSuchFieldException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ── field access — direct to inner, no cache ─────────────────

    protected int getInt(String innerField) {
        try { return inner.getClass().getField(innerField).getInt(inner); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected void setInt(String innerField, int v) {
        try { inner.getClass().getField(innerField).setInt(inner, v); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected String getString(String innerField) {
        try { return (String) inner.getClass().getField(innerField).get(inner); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected void setString(String innerField, String v) {
        try { inner.getClass().getField(innerField).set(inner, v); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected byte[] getBytes(String innerField) {
        try { return (byte[]) inner.getClass().getField(innerField).get(inner); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected void setBytes(String innerField, byte[] v) {
        try { inner.getClass().getField(innerField).set(inner, v); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    // ── CmsType wrapper access — lazy, shares inner reference ──────

    /** Get or create a cached CmsType wrapper sharing the inner field. */
    @SuppressWarnings("unchecked")
    protected <T extends CmsType> T getWrapper(String innerField, Class<T> wrapperType) {
        CmsType cached = injectedWrappers.get(innerField);
        if (cached != null) return (T) cached;

        try {
            T wrapper = wrapperType.getDeclaredConstructor().newInstance();
            Field innerFieldRef = inner.getClass().getField(innerField);
            wrapper.inner = (InnerBase) innerFieldRef.get(inner);
            innerCache.put(innerField, wrapper.innerCache);
            injectedWrappers.put(innerField, wrapper);
            return wrapper;
        } catch (NoSuchFieldException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create wrapper for " + innerField, e);
        }
    }

    /**
     * Replace an injected wrapper, keeping the original inner reference so
     * that {@code syncToInner()} writes to the correct Inner* field.
     */
    @SuppressWarnings("unchecked")
    protected <T extends CmsType> T replaceWrapper(String fieldName, T newWrapper) {
        CmsType old = injectedWrappers.get(fieldName);
        if (old != null) {
            newWrapper.inner = old.inner;
            newWrapper.innerCache.clear();
            newWrapper.innerCache.putAll(old.innerCache);
        }
        newWrapper.syncToInner();
        injectedWrappers.put(fieldName, newWrapper);
        innerCache.put(fieldName, newWrapper.innerCache);
        try {
            getClass().getField(fieldName).set(this, newWrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return newWrapper;
    }

    // ── automatic sync ───────────────────────────────────────────

    /**
     * Copy {@code @Bit}-annotated field values from one CmsBits instance to another.
     * Both must be the same concrete class. Used by fluent setters that receive
     * a new CmsBits instance and need to apply its values to the already-bound
     * instance.
     */
    protected static void copyBits(CmsBits src, CmsBits dst) {
        for (Field f : src.getClass().getFields()) {
            if (f.getAnnotation(CmsBits.Bit.class) != null) {
                try { f.set(dst, f.get(src)); } catch (Exception e) {
                    throw new RuntimeException("Failed to copy @" + CmsBits.Bit.class.getSimpleName()
                        + " field " + f.getName(), e);
                }
            }
        }
    }

    @Override
    public void syncToInner() {
        // push cached wrappers → inner
        for (CmsType w : injectedWrappers.values()) {
            w.syncToInner();
        }
        // push optional field presence → inner._set
        Set<String> s = innerSetField();
        if (s != null) {
            for (String opt : optionalFields) {
                if (Boolean.TRUE.equals(innerCache.get(presKey(opt)))) {
                    s.add(opt);
                }
            }
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        // pull inner._set → optional field presence
        Set<String> s = innerSetField();
        if (s != null) {
            for (String opt : optionalFields) {
                innerCache.put(presKey(opt), s.contains(opt));
            }
        }
        // sync inner → cached wrappers
        for (CmsType w : injectedWrappers.values()) {
            w.syncFromInner();
        }
    }

    // ── decode override — rebind wrappers ──────────────────────────────

    @Override
    public void decode(byte[] data) {
        super.decode(data);
        rebindWrappers();
    }

    // ── equals / hashCode — delegates to CmsType's field-based reflection ─
    // @InnerField wrappers are public fields, so CmsType.equals() compares them
    // through CmsScalar's value-based equals.  No override needed.

    @Override
    public String toString() {
        return "(" + getClass().getSimpleName() + ")\n" + inner.toString();
    }
}
