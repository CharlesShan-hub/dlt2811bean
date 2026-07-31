package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.V;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Base class for SEQUENCE types backed directly by an Inner* PDU.
 *
 * <p>Subclasses declare public CmsType fields annotated with {@link CmsField}.
 * The base class constructor automatically creates wrapper instances whose
 * {@code inner._v} is shared with the parent's corresponding {@code _v} entry,
 * eliminating the need for explicit data sync.
 *
 * <p>{@link #syncToInner()} still exists for wrappers that have Java fields
 * needing packing (e.g. {@link CmsBits}).
 *
 * <pre>{@code
 * public class CmsBrcb extends CmsSequence {
 *     &#64;CmsField public CmsBoolean rptEna;
 *     &#64;CmsField(optional = true) public CmsInt16 resvTms;
 *     // ...
 *     public CmsBrcb() { super(new InnerBRCB()); }
 * }
 * }</pre>
 */
public abstract class CmsSequence extends CmsType {

    private final Map<String, CmsType> injectedWrappers = new LinkedHashMap<>();
    /** Field names annotated with {@code @CmsField(optional = true)}. */
    private final Set<String> optionalFields = new HashSet<>();
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

            try {
                CmsType wrapper = ((Class<? extends CmsType>) f.getType()).getDeclaredConstructor().newInstance();
                // Share _v sub-map: wrapper._v points to parent's field entry
                Object sub = inner._v.get(fieldName);
                if (sub instanceof LinkedHashMap) {
                    wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
                }
                // If wrapper is a CmsChoice, rebind its variant choices to the same _v
                if (wrapper instanceof CmsChoice) {
                    ((CmsChoice) wrapper).rebindChoices();
                }
                f.set(this, wrapper);
                injectedWrappers.put(fieldName, wrapper);

                if (ann.optional()) {
                    optionalFields.add(fieldName);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to inject @CmsField " + fieldName, e);
            }
        }
    }

    /** Rebind wrapper _v after decode creates a new Inner*. */
    public void rebindWrappers() {
        for (Map.Entry<String, CmsType> entry : injectedWrappers.entrySet()) {
            String name = entry.getKey();
            CmsType wrapper = entry.getValue();
            Object sub = inner._v.get(name);
            if (sub instanceof LinkedHashMap) {
                wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
            } else if (sub != null && (wrapper instanceof CmsScalar || wrapper instanceof CmsBits)) {
                // Jackson stores scalar / BIT STRING values directly (e.g. "cbRef1", "0000");
                // wrap into _v so innerGet()/readPacked() can see them.
                wrapper.inner._v.put("_", sub);
            } else if (sub == null && optionalFields.contains(name)) {
                // Optional field absent after decode — drop stale constructor defaults
                // so a later re-encode doesn't serialize them.
                wrapper.inner._v.clear();
            }
            if (wrapper instanceof CmsChoice) {
                ((CmsChoice) wrapper).rebindChoices();
            }
            if (wrapper instanceof CmsSequence) {
                ((CmsSequence) wrapper).rebindWrappers();
            }
        }
        // Rebuild SEQUENCE OF wrappers from inner._v
        for (Map.Entry<String, Class<? extends CmsType>> e : sequenceFields.entrySet()) {
            try {
                Field f = getClass().getField(e.getKey());
                @SuppressWarnings("unchecked")
                List<Object> innerList = (List<Object>) inner._v.get(e.getKey());
                List<CmsType> list = new ArrayList<>();
                if (innerList != null) {
                    for (Object innerElem : innerList) {
                        CmsType wrapper = e.getValue().getDeclaredConstructor().newInstance();
                        if (innerElem instanceof InnerBase) {
                            wrapper.inner._v = ((InnerBase) innerElem)._v;
                        } else if (innerElem instanceof LinkedHashMap) {
                            // Jackson-deserialized raw map — share it as wrapper's _v
                            wrapper.inner._v = (LinkedHashMap<String, Object>) innerElem;
                        } else {
                            continue;
                        }
                        if (wrapper instanceof CmsChoice)
                            ((CmsChoice) wrapper).rebindChoices();
                        if (wrapper instanceof CmsSequence)
                            ((CmsSequence) wrapper).rebindWrappers();
                        wrapper.syncFromInner();
                        list.add(wrapper);
                    }
                }
                f.set(this, list);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to rebind sequence field " + e.getKey(), ex);
            }
        }
    }

    // ── presence API ─────────────────────────────────────────────────

    /** Mark an optional field as present. Stores in {@code _v} under "_present_<name>". */
    public void setPresent(String fieldName, boolean v) {
        V.setPresent(inner._v, fieldName, v);
    }

    /** Check if an optional field is present. */
    public boolean isPresent(String fieldName) {
        return V.isPresent(inner._v, fieldName);
    }

    // ── field access — direct to _v ──────────────────────────────────

    protected int getInt(String fieldName) {
        Object v = V.field(inner._v, fieldName);
        return v instanceof Number ? ((Number) v).intValue() : 0;
    }

    protected void setInt(String fieldName, int v) {
        V.setField(inner._v, fieldName, v);
    }

    protected String getString(String fieldName) {
        Object v = V.field(inner._v, fieldName);
        return v instanceof String ? (String) v : null;
    }

    protected void setString(String fieldName, String v) {
        V.setField(inner._v, fieldName, v);
    }

    @SuppressWarnings("unchecked")
    protected byte[] getBytes(String fieldName) {
        Object v = V.field(inner._v, fieldName);
        if (v instanceof byte[]) return (byte[]) v;
        if (v instanceof List) {
            // JER hex format: decode hex string list to bytes
            List<String> hexParts = (List<String>) v;
            StringBuilder sb = new StringBuilder();
            for (String h : hexParts) sb.append(h.replace("0x", ""));
            return InnerBase.unhex(sb.toString());
        }
        return null;
    }

    protected void setBytes(String fieldName, byte[] v) {
        V.setField(inner._v, fieldName, v);
    }

    // ── CmsType wrapper access ──────────────────────────────────────

    /** Get a cached CmsType wrapper (created by injectFields). */
    @SuppressWarnings("unchecked")
    protected <T extends CmsType> T getWrapper(String fieldName, Class<T> wrapperType) {
        CmsType cached = injectedWrappers.get(fieldName);
        if (cached != null) return (T) cached;
        // Create on-demand and share _v
        try {
            T wrapper = wrapperType.getDeclaredConstructor().newInstance();
            Object sub = inner._v.get(fieldName);
            if (sub instanceof LinkedHashMap) {
                wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
            }
            injectedWrappers.put(fieldName, wrapper);
            return wrapper;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create wrapper for " + fieldName, e);
        }
    }

    /**
     * Bind an externally-created CmsType wrapper to a field, replacing the
     * auto-injected wrapper.  Shares the {@code _v} sub-map so data is in-sync.
     */
    protected void bindWrapper(String fieldName, CmsType wrapper) {
        injectedWrappers.put(fieldName, wrapper);
        Object sub = inner._v.get(fieldName);
        if (sub instanceof LinkedHashMap) {
            wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
        }
        if (wrapper instanceof CmsChoice) {
            ((CmsChoice) wrapper).rebindChoices();
        }
        if (wrapper instanceof CmsSequence) {
            ((CmsSequence) wrapper).rebindWrappers();
        }
    }

    // ── automatic sync ───────────────────────────────────────────

    @Override
    public void syncToInner() {
        detectWrapperReplacements();
        for (Map.Entry<String, CmsType> e : injectedWrappers.entrySet()) {
            if (optionalFields.contains(e.getKey()) && !isPresent(e.getKey())) {
                // Optional field not marked present — remove stale default from _v
                inner._v.remove(e.getKey());
                continue;
            }
            CmsType w = e.getValue();
            w.syncToInner();
            if (w instanceof CmsScalar) {
                // Use toJsonValue() so special scalars (e.g. unsigned Int32U) serialize correctly
                inner._v.put(e.getKey(), ((CmsScalar) w).inner.toJsonValue());
            }
        }
        // Sync SEQUENCE OF fields → inner._v
        for (Map.Entry<String, Class<? extends CmsType>> e : sequenceFields.entrySet()) {
            try {
                Field f = getClass().getField(e.getKey());
                @SuppressWarnings("unchecked")
                List<CmsType> list = (List<CmsType>) f.get(this);
                if (list == null) continue;
                List<Object> innerList = new ArrayList<>();
                for (CmsType elem : list) {
                    elem.syncToInner();
                    innerList.add(elem.inner);
                }
                inner._v.put(e.getKey(), innerList);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to sync sequence field " + e.getKey(), ex);
            }
        }
    }

    @Override
    public byte[] encode() {
        syncToInner();
        return inner.encode();
    }

    /** Empty hook kept for subclass compatibility (data flows through shared _v). */
    @Override
    public void syncFromInner() {
        detectWrapperReplacements();
        // Populate Java fields that don't share _v (e.g. CmsBits bit fields)
        for (CmsType wrapper : injectedWrappers.values()) {
            wrapper.syncFromInner();
        }
    }

    /**
     * Detect wrapper replacements in @CmsField fields (e.g.
     * {@code p.signedTime = new CmsUtcTime()}) and rebind.
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
                if (existing == val) continue;
                // Register new wrapper and share _v
                CmsType wrapper = (CmsType) val;
                injectedWrappers.put(name, wrapper);
                Object sub = inner._v.get(name);
                if (sub instanceof LinkedHashMap) {
                    wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
                }
                if (wrapper instanceof CmsChoice)
                    ((CmsChoice) wrapper).rebindChoices();
                if (wrapper instanceof CmsSequence) {
                    ((CmsSequence) wrapper).rebindWrappers();
                }
            } catch (Exception e) {
                // skip fields that fail reflection
            }
        }
    }

    protected void ensureInnerCacheComplete() {
        detectWrapperReplacements();
    }

    // ── decode override — rebind wrappers ──────────────────────────────

    @Override
    public void decode(byte[] data) {
        super.decode(data);
        rebindWrappers();
        syncFromInner();
    }

    @Override
    public String toString() {
        return "(" + getClass().getSimpleName() + ")\n" + inner.toString();
    }
}
