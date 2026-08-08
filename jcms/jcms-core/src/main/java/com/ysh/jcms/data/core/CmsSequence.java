package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.V;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for SEQUENCE types backed directly by an Inner* PDU.
 *
 * <p>
 * Subclasses declare public CmsType fields annotated with {@link CmsField}. The
 * base class constructor automatically creates wrapper instances whose
 * {@code inner._v} is shared with the parent's corresponding {@code _v} entry,
 * eliminating the need for explicit data sync.
 *
 * <p>
 * {@link #syncToInner()} still exists for wrappers that have Java fields
 * needing packing (e.g. {@link CmsBits}).
 *
 * <pre>
 * {
 *     &#64;code
 *     public class CmsBrcb extends CmsSequence {
 *         &#64;CmsField
 *         public CmsBoolean rptEna;
 *         &#64;CmsField(optional = true)
 *         public CmsInt16 resvTms;
 *         // ...
 *         public CmsBrcb() {
 *             super(new InnerBRCB());
 *         }
 *     }
 * }
 * </pre>
 */
public abstract class CmsSequence extends CmsType {

    private final Map<String, CmsType> injectedWrappers = new LinkedHashMap<>();

    /**
     * One annotated {@code @CmsField}. Field refs are class-level and shareable.
     */
    private static final class CmsFieldInfo {
        final Field field;
        final String innerName; // _v key; defaults to the Java field name
        final boolean optional;
        final boolean sequenceOf;
        final Class<? extends CmsType> elementType;

        CmsFieldInfo(Field field, String innerName, boolean optional, boolean sequenceOf, Class<? extends CmsType> elementType) {
            this.field = field;
            this.innerName = innerName;
            this.optional = optional;
            this.sequenceOf = sequenceOf;
            this.elementType = elementType;
        }
    }

    /**
     * Per-class metadata built from {@code @CmsField} annotations — field
     * descriptions only; wrapper instances stay per-object in
     * {@link #injectedWrappers}.
     */
    private static final ClassValue<SequenceMeta> SEQ_META = new ClassValue<SequenceMeta>() {
        @Override
        protected SequenceMeta computeValue(Class<?> type) {
            List<CmsFieldInfo> fields = new ArrayList<>();
            Map<String, CmsFieldInfo> byName = new LinkedHashMap<>();
            Map<String, CmsFieldInfo> sequenceOf = new LinkedHashMap<>();
            Set<String> optional = new HashSet<>();
            for (Field f : type.getFields()) {
                if (Modifier.isStatic(f.getModifiers()))
                    continue;
                CmsField ann = f.getAnnotation(CmsField.class);
                if (ann == null)
                    continue;
                CmsFieldInfo info = new CmsFieldInfo(f, ann.inner().isEmpty() ? f.getName() : ann.inner(), ann.optional(), ann.sequenceOf(),
                        ann.elementType());
                fields.add(info);
                byName.put(f.getName(), info);
                if (ann.sequenceOf())
                    sequenceOf.put(f.getName(), info);
                if (ann.optional())
                    optional.add(f.getName());
            }
            return new SequenceMeta(fields, byName, sequenceOf, optional);
        }
    };

    private static final class SequenceMeta {
        final List<CmsFieldInfo> fields; // declaration order
        final Map<String, CmsFieldInfo> byName;
        final Map<String, CmsFieldInfo> sequenceOf; // fieldName → info
        final Set<String> optional; // OPTIONAL field names

        SequenceMeta(List<CmsFieldInfo> fields, Map<String, CmsFieldInfo> byName, Map<String, CmsFieldInfo> sequenceOf,
                Set<String> optional) {
            this.fields = fields;
            this.byName = byName;
            this.sequenceOf = sequenceOf;
            this.optional = optional;
        }
    }

    protected CmsSequence() {
    }

    @SuppressWarnings("unchecked")
    protected CmsSequence(InnerBase inner) {
        super(inner);
        injectFields();
    }

    // ── @CmsField injection ──────────────────────────────────────────

    private void injectFields() {
        for (CmsFieldInfo info : SEQ_META.get(getClass()).fields) {
            Field f = info.field;
            String fieldName = f.getName();

            // SEQUENCE OF field
            if (info.sequenceOf) {
                try {
                    f.set(this, new ArrayList<>());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to init sequence field " + fieldName, e);
                }
                continue;
            }

            if (!CmsType.class.isAssignableFrom(f.getType()))
                continue;

            try {
                CmsType wrapper = ((Class<? extends CmsType>) f.getType()).getDeclaredConstructor().newInstance();
                // Share _v sub-map: wrapper._v points to parent's field entry
                Object sub = inner._v.get(info.innerName);
                if (sub instanceof LinkedHashMap) {
                    wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
                }
                // If wrapper is a CmsChoice, rebind its variant choices to the same _v
                if (wrapper instanceof CmsChoice) {
                    ((CmsChoice) wrapper).rebindChoices();
                }
                f.set(this, wrapper);
                injectedWrappers.put(fieldName, wrapper);
            } catch (Exception e) {
                throw new RuntimeException("Failed to inject @CmsField " + fieldName, e);
            }
        }
    }

    /** Rebind wrapper _v after decode creates a new Inner*. */
    public void rebindWrappers() {
        SequenceMeta meta = SEQ_META.get(getClass());
        // Rebuild OPTIONAL presence markers: a decoded frame only contains the
        // fields that were actually present, so presence == field in inner._v.
        for (String name : meta.optional) {
            CmsFieldInfo info = meta.byName.get(name);
            V.setPresent(inner._v, name, inner._v.containsKey(info != null ? info.innerName : name));
        }
        for (Map.Entry<String, CmsType> entry : injectedWrappers.entrySet()) {
            String name = entry.getKey();
            CmsType wrapper = entry.getValue();
            CmsFieldInfo info = meta.byName.get(name);
            String innerKey = info != null ? info.innerName : name;
            Object sub = inner._v.get(innerKey);
            if (sub instanceof LinkedHashMap) {
                wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
            } else if (sub != null && !(wrapper instanceof CmsSequence) && !(wrapper instanceof CmsChoice)) {
                // Leaf wrapper (CmsScalar, CmsBits, CmsUtcTime, ...): Jackson stores
                // the decoded JER value directly (e.g. hex string for UtcTime); wrap
                // into _v so innerGet()/syncFromInner() can read it.
                V.setVal(wrapper.inner._v, sub);
            } else if (sub == null && meta.optional.contains(name)) {
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
        for (Map.Entry<String, CmsFieldInfo> e : meta.sequenceOf.entrySet()) {
            try {
                Field f = e.getValue().field;
                @SuppressWarnings("unchecked")
                List<Object> innerList = (List<Object>) inner._v.get(e.getValue().innerName);
                List<CmsType> list = new ArrayList<>();
                if (innerList != null) {
                    for (Object innerElem : innerList) {
                        CmsType wrapper = e.getValue().elementType.getDeclaredConstructor().newInstance();
                        if (innerElem instanceof InnerBase) {
                            wrapper.inner._v = ((InnerBase) innerElem)._v;
                        } else if (innerElem instanceof LinkedHashMap) {
                            // Jackson-deserialized raw map — share it as wrapper's _v
                            wrapper.inner._v = (LinkedHashMap<String, Object>) innerElem;
                        } else if (innerElem != null && wrapper instanceof CmsScalar) {
                            // Raw scalar element (SEQUENCE OF Int16U/ObjectReference:
                            // JER [1,2,3] / ["LD1"]) — wrap into the scalar _v
                            V.setVal(wrapper.inner._v, innerElem);
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

    /**
     * Mark an optional field as present. Stores in {@code _v} under
     * "_present_<name>".
     */
    public void setPresent(String fieldName, boolean v) {
        V.setPresent(inner._v, fieldName, v);
    }

    /** Check if an optional field is present. */
    public boolean isPresent(String fieldName) {
        return V.isPresent(inner._v, fieldName);
    }

    /**
     * True if any OPTIONAL field is currently marked present.
     *
     * <p>
     * Set*Result PDUs (e.g. {@code SetBRCBResult}) encode "this field failed" as
     * "the optional field is present", so a single call replaces the old
     * per-field {@code hasEntryError()} enumeration.
     */
    public boolean hasAnyPresent() {
        for (String name : SEQ_META.get(getClass()).optional) {
            if (isPresent(name))
                return true;
        }
        return false;
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
        if (v instanceof byte[])
            return (byte[]) v;
        if (v instanceof List) {
            // JER hex format: decode hex string list to bytes
            List<String> hexParts = (List<String>) v;
            StringBuilder sb = new StringBuilder();
            for (String h : hexParts)
                sb.append(h.replace("0x", ""));
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
        if (cached != null)
            return (T) cached;
        // Create on-demand and share _v
        try {
            T wrapper = wrapperType.getDeclaredConstructor().newInstance();
            CmsFieldInfo info = SEQ_META.get(getClass()).byName.get(fieldName);
            Object sub = inner._v.get(info != null ? info.innerName : fieldName);
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
     * auto-injected wrapper. Shares the {@code _v} sub-map so data is in-sync.
     */
    protected void bindWrapper(String fieldName, CmsType wrapper) {
        injectedWrappers.put(fieldName, wrapper);
        CmsFieldInfo info = SEQ_META.get(getClass()).byName.get(fieldName);
        Object sub = inner._v.get(info != null ? info.innerName : fieldName);
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
        SequenceMeta meta = SEQ_META.get(getClass());
        detectWrapperReplacements();
        for (Map.Entry<String, CmsType> e : injectedWrappers.entrySet()) {
            CmsFieldInfo info = meta.byName.get(e.getKey());
            String innerKey = info != null ? info.innerName : e.getKey();
            if (meta.optional.contains(e.getKey()) && !isPresent(e.getKey())) {
                // Optional field not marked present — remove stale default from _v
                inner._v.remove(innerKey);
                continue;
            }
            CmsType w = e.getValue();
            w.syncToInner();
            if (w instanceof CmsScalar) {
                // Use toJsonValue() so special scalars (e.g. unsigned Int32U) serialize
                // correctly
                inner._v.put(innerKey, ((CmsScalar) w).inner.toJsonValue());
            } else if (w instanceof CmsBits) {
                // BIT STRING: JER form is a bare hex string. Unlike CmsScalar, the
                // wrapper _v is NOT shared with parent after decode (rebindWrappers
                // stores the decoded hex string directly), so write it back explicitly
                // — otherwise a decode→modify→encode cycle silently loses updates.
                inner._v.put(innerKey, V.getVal(w.inner._v));
            } else {
                // Non-scalar wrapper (CmsSequence/CmsChoice/CmsUtcTime/...): JER form
                // is the _v map itself. Re-assert the alias so OPTIONAL fields that
                // were not pre-seeded in the Inner constructor still reach parent _v
                // on encode (no-op when already shared).
                inner._v.put(innerKey, w.inner._v);
            }
        }
        // Sync SEQUENCE OF fields → inner._v
        for (Map.Entry<String, CmsFieldInfo> e : meta.sequenceOf.entrySet()) {
            try {
                Field f = e.getValue().field;
                @SuppressWarnings("unchecked")
                List<CmsType> list = (List<CmsType>) f.get(this);
                if (list == null)
                    continue;
                List<Object> innerList = new ArrayList<>();
                for (CmsType elem : list) {
                    elem.syncToInner();
                    innerList.add(elem.inner);
                }
                inner._v.put(e.getValue().innerName, innerList);
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

    /**
     * Empty hook kept for subclass compatibility (data flows through shared _v).
     */
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
        for (CmsFieldInfo info : SEQ_META.get(getClass()).fields) {
            if (info.sequenceOf)
                continue; // List field, not a CmsType wrapper
            Field f = info.field;
            if (!CmsType.class.isAssignableFrom(f.getType()))
                continue;
            String name = f.getName();
            try {
                Object val = f.get(this);
                if (!(val instanceof CmsType))
                    continue;
                CmsType existing = injectedWrappers.get(name);
                if (existing == val)
                    continue;
                // Register new wrapper and share _v
                CmsType wrapper = (CmsType) val;
                injectedWrappers.put(name, wrapper);
                Object sub = inner._v.get(info.innerName);
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
