package com.ysh.jcms.core;

import com.ysh.jcms.data.InnerBase;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Base class for SEQUENCE types backed directly by an Inner* PDU.
 *
 * <p>Subclasses declare public CmsType fields annotated with {@link CmsField}.
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
 *     @CmsField public CmsBoolean rptEna;
 *     @CmsField(optional = true) public CmsInt16 resvTms;
 *     // ...
 *     public CmsBrcb() { super(new InnerBRCB()); }
 * }
 * }</pre>
 */
public abstract class CmsSequence extends CmsType {

    private final Map<String, CmsType> injectedWrappers = new LinkedHashMap<>();
    /** Field names annotated with {@code @CmsField(optional = true)}. */
    private final Set<String> optionalFields = new HashSet<>();
    /** Non-InnerBase inner fields — CmsSequence reads/writes inner field directly. */
    private final Map<String, CmsType> directWrappers = new LinkedHashMap<>();
    private final Map<String, Field> directFields = new LinkedHashMap<>();
    /** SEQUENCE OF fields — fieldName → element wrapper type. */
    private final Map<String, Class<? extends CmsType>> sequenceFields = new LinkedHashMap<>();

    protected CmsSequence() {}

    @SuppressWarnings("unchecked")
    protected CmsSequence(InnerBase inner) {
        super(inner);
        injectFields();
    }

    // ── @CmsField injection ──────────────────────────────────────────

    private void injectFields() {
        for (Field f : getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            CmsField ann = f.getAnnotation(CmsField.class);
            if (ann == null) continue;

            String fieldName = f.getName();

            // SEQUENCE OF field
            if (ann.sequenceOf()) {
                try {
                    f.set(this, new ArrayList<>());
                    sequenceFields.put(fieldName, ann.elementType());
                    if (ann.optional()) optionalFields.add(fieldName);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to init sequence field " + fieldName, e);
                }
                continue;
            }

            if (!CmsType.class.isAssignableFrom(f.getType())) continue;

            Class<? extends CmsType> wrapperType = (Class<? extends CmsType>) f.getType();

            try {
                CmsType wrapper = wrapperType.getDeclaredConstructor().newInstance();
                Field innerField = inner.getClass().getField(fieldName);

                // If the Inner* field is an InnerBase subtype, share the reference
                if (InnerBase.class.isAssignableFrom(innerField.getType())) {
                    wrapper.inner = (InnerBase) innerField.get(inner);
                    if (wrapper instanceof CmsChoice)
                        ((CmsChoice) wrapper).rebindChoices();
                    // CmsScalar subclasses cache the value in their constructor
                    // but the Inner* field may have a different default (e.g.
                    // new byte[6] vs new byte[0]), so re-sync after rebinding.
                    if (wrapper instanceof CmsScalar) {
                        wrapper.syncFromInner();
                    }
                } else {
                    // Non-InnerBase field (e.g. Integer): read/write directly
                    directWrappers.put(fieldName, wrapper);
                    directFields.put(fieldName, innerField);
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
                throw new RuntimeException("Failed to inject @CmsField " + fieldName, e);
            }
        }
    }

    /** Rebind wrapper inner references after decode creates a new Inner*. */
    public void rebindWrappers() {
        for (Map.Entry<String, CmsType> entry : injectedWrappers.entrySet()) {
            try {
                String name = entry.getKey();
                Field innerField = inner.getClass().getField(name);
                CmsType wrapper = entry.getValue();
                if (InnerBase.class.isAssignableFrom(innerField.getType())) {
                    wrapper.inner = (InnerBase) innerField.get(inner);
                    if (wrapper instanceof CmsChoice)
                        ((CmsChoice) wrapper).rebindChoices();
                    // Recursively rebind sub-wrappers of CmsSequence wrappers
                    // (e.g. CmsAuthenticationParameter → signatureCertificate/signedTime/signedValue)
                    if (wrapper instanceof CmsSequence) {
                        CmsSequence seq = (CmsSequence) wrapper;
                        seq.rebindWrappers();
                        seq.ensureInnerCacheComplete();
                    }
                    wrapper.syncFromInner();
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to rebind " + entry.getKey(), e);
            }
        }
        // Rebind direct fields: read value from new inner into wrapper cache
        for (Map.Entry<String, CmsType> e : directWrappers.entrySet()) {
            try {
                Field innerField = inner.getClass().getField(e.getKey());
                Object val = innerField.get(inner);
                if (val != null) {
                    e.getValue().innerCache.put("value", val);
                }
            } catch (Exception ignored) {}
        }
    }

    // ── presence API ─────────────────────────────────────────────────

    /** Presence key in innerCache for an optional field. */
    private static String presKey(String fieldName) {
        return "has" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    /** Mark an optional field as present/absent. */
    public void setPresent(String innerFieldName, boolean v) {
        innerCache.put(presKey(innerFieldName), v);
    }

    /** Check if an optional field is present. */
    public boolean isPresent(String fieldName) {
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
        try {
            Field f = inner.getClass().getField(innerField);
            if (f.getType() == int.class) return f.getInt(inner);
            Object val = f.get(inner);
            return val instanceof Integer ? (Integer) val : 0;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    protected void setInt(String innerField, int v) {
        try {
            Field f = inner.getClass().getField(innerField);
            if (f.getType() == int.class) f.setInt(inner, v);
            else f.set(inner, v);
        } catch (Exception e) { throw new RuntimeException(e); }
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
     * Bind an externally-created CmsType wrapper to a field, replacing the
     * auto-injected wrapper.  Updates injectedWrappers and shares innerCache
     * so that sync and equality checks see the new wrapper's data.
     *
     * <p>Also rebinds {@code wrapper.inner} to the parent Inner*'s field so that
     * {@code syncToInner()} writes data into the right place.  Without this, a
     * wrapper created externally (e.g. {@code new CmsUtcTime()}) has its own
     * independent inner that is disconnected from the parent's Inner* tree.
     *
     * <p>Call from subclass setters that replace (not just mutate) a wrapper:
     * <pre>{@code
     * public MySeq authenticationParameter(CmsAuthenticationParameter v) {
     *     bindWrapper("authenticationParameter", v);
     *     setPresent("authenticationParameter", true);
     *     return this;
     * }
     * }</pre>
     */
    protected void bindWrapper(String fieldName, CmsType wrapper) {
        injectedWrappers.put(fieldName, wrapper);
        innerCache.put(fieldName, wrapper.innerCache);
        // Rebind wrapper.inner to the parent Inner*'s field so that
        // syncToInner() pushes data to the right Inner* object.
        try {
            Field innerField = inner.getClass().getField(fieldName);
            if (InnerBase.class.isAssignableFrom(innerField.getType())) {
                wrapper.inner = (InnerBase) innerField.get(inner);
                if (wrapper instanceof CmsChoice)
                    ((CmsChoice) wrapper).rebindChoices();
                if (wrapper instanceof CmsSequence) {
                    CmsSequence seq = (CmsSequence) wrapper;
                    seq.rebindWrappers();
                    // Populate injectedWrappers from @CmsField annotations — needed when
                    // wrapper was created with InnerEmpty (e.g. CmsAuthenticationParameter())
                    // so injectFields found no matching Inner* fields.
                    seq.ensureInnerCacheComplete();
                }
                // Push wrapper's current data → new inner (so it's picked up by encode)
                wrapper.syncToInner();
            }
        } catch (NoSuchFieldException e) {
            // Inner* doesn't have this field — skip silently
        } catch (Exception e) {
            throw new RuntimeException("Failed to rebind inner for " + fieldName, e);
        }
    }

    // ── automatic sync ───────────────────────────────────────────

    @Override
    public void syncToInner() {
        // Detect field-assigned wrapper replacements BEFORE syncing, so that
        // the NEW wrapper's data is pushed to inner instead of the old one's.
        detectWrapperReplacements();
        // push cached wrappers → inner (skip non-presented optional fields)
        for (Map.Entry<String, CmsType> e : injectedWrappers.entrySet()) {
            if (optionalFields.contains(e.getKey()) && !Boolean.TRUE.equals(innerCache.get(presKey(e.getKey())))) {
                continue; // optional field not marked present — leave inner untouched
            }
            e.getValue().syncToInner();
        }
        // push direct field wrappers → inner native field
        for (Map.Entry<String, CmsType> e : directWrappers.entrySet()) {
            try {
                Object val = e.getValue().innerCache.get("value");
                if (val != null) {
                    directFields.get(e.getKey()).set(inner, val);
                }
            } catch (Exception ex) {
                throw new RuntimeException("Failed to sync direct field " + e.getKey(), ex);
            }
        }
        // push SEQUENCE OF fields → inner
        for (Map.Entry<String, Class<? extends CmsType>> e : sequenceFields.entrySet()) {
            try {
                Field f = getClass().getField(e.getKey());
                Field innerF = inner.getClass().getField(e.getKey());
                List<CmsType> list = (List<CmsType>) f.get(this);
                if (list == null) continue;
                List<Object> innerList = new ArrayList<>();
                for (CmsType elem : list) {
                    elem.syncToInner();
                    innerList.add(elem.inner);
                }
                innerF.set(inner, innerList);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to sync sequence field " + e.getKey(), ex);
            }
        }
        // Ensure OPTIONAL presence keys exist in innerCache (for CmsEqualUtil)
        for (String opt : optionalFields) {
            innerCache.putIfAbsent(presKey(opt), false);
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
        // ensure innerCache completeness (SEQUENCE OF lists, InnerEmpty fields)
        ensureInnerCacheComplete();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        // pull inner native field → direct field wrapper cache
        for (Map.Entry<String, CmsType> e : directWrappers.entrySet()) {
            try {
                Object val = directFields.get(e.getKey()).get(inner);
                if (val != null) {
                    e.getValue().innerCache.put("value", val);
                }
            } catch (Exception ex) {
                throw new RuntimeException("Failed to sync direct field " + e.getKey(), ex);
            }
        }
        // pull SEQUENCE OF fields from inner
        for (Map.Entry<String, Class<? extends CmsType>> e : sequenceFields.entrySet()) {
            try {
                Field f = getClass().getField(e.getKey());
                Field innerF = inner.getClass().getField(e.getKey());
                List<Object> innerList = (List<Object>) innerF.get(inner);
                List<CmsType> list = (List<CmsType>) f.get(this);
                if (list == null) { list = new ArrayList<>(); f.set(this, list); }
                list.clear();
                if (innerList != null) {
                    for (Object innerElem : innerList) {
                        CmsType wrapper = e.getValue().getDeclaredConstructor().newInstance();
                        if (innerElem instanceof InnerBase) {
                            wrapper.inner = (InnerBase) innerElem;
                        }
                        wrapper.syncFromInner();
                        list.add(wrapper);
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException("Failed to sync sequence field " + e.getKey(), ex);
            }
        }
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
        // ensure innerCache completeness (SEQUENCE OF lists, InnerEmpty fields)
        ensureInnerCacheComplete();
    }

    /**
     * Scan public @CmsField wrappers and update injectedWrappers/innerCache
     * when the field has been replaced by direct assignment (e.g.
     * {@code p.signedTime = new CmsUtcTime()}).  Also rebinds the new
     * wrapper's {@code inner} to the correct Inner* field so that
     * subsequent {@code syncToInner()} pushes data into the right place.
     */
    private void detectWrapperReplacements() {
        for (Field f : getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            CmsField ann = f.getAnnotation(CmsField.class);
            if (ann == null) continue;
            if (!CmsType.class.isAssignableFrom(f.getType())) continue;
            String name = f.getName();
            try {
                Object val = f.get(this);
                if (!(val instanceof CmsType)) continue;
                CmsType existing = injectedWrappers.get(name);
                if (existing == val) continue; // not replaced

                // Register the new wrapper in injectedWrappers + innerCache
                CmsType wrapper = (CmsType) val;
                innerCache.put(name, wrapper.innerCache);
                injectedWrappers.put(name, wrapper);

                // Rebind the new wrapper's inner to the parent's Inner* field
                // (otherwise syncToInner writes to wrapper's own independent inner)
                try {
                    Field innerField = inner.getClass().getField(name);
                    if (InnerBase.class.isAssignableFrom(innerField.getType())) {
                        wrapper.inner = (InnerBase) innerField.get(inner);
                        if (wrapper instanceof CmsChoice)
                            ((CmsChoice) wrapper).rebindChoices();
                        if (wrapper instanceof CmsSequence) {
                            CmsSequence seq = (CmsSequence) wrapper;
                            seq.rebindWrappers();
                            seq.ensureInnerCacheComplete();
                        }
                    }
                } catch (NoSuchFieldException e) {
                    // Inner* doesn't have this field — new wrapper keeps its own inner
                }
            } catch (Exception e) {
                // skip fields that fail reflection
            }
        }
    }

    /**
     * Scan @CmsField-annotated fields and ensure they are present in innerCache.
     * This handles:
     * <ul>
     *   <li>SEQUENCE OF fields (list) — not in innerCache by default</li>
     *   <li>@CmsField wrappers on InnerEmpty subclasses — injectFields skipped them</li>
     * </ul>
     */
    protected void ensureInnerCacheComplete() {
        detectWrapperReplacements();
        for (Field f : getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            CmsField ann = f.getAnnotation(CmsField.class);
            if (ann == null) continue;
            String name = f.getName();
            try {
                if (innerCache.containsKey(name)) continue; // already present
                if (ann.sequenceOf()) {
                    innerCache.put(name, f.get(this));
                }
            } catch (Exception e) {
                // skip fields that fail reflection
            }
        }
    }

    // ── decode override — rebind wrappers ──────────────────────────────

    @Override
    public void decode(byte[] data) {
        super.decode(data);
        rebindWrappers();
    }

    // ── equals / hashCode — delegates to CmsType's field-based reflection ─
    // @CmsField wrappers are public fields, so CmsType.equals() compares them
    // through CmsScalar's value-based equals.  No override needed.

    @Override
    public String toString() {
        return "(" + getClass().getSimpleName() + ")\n" + inner.toString();
    }
}
