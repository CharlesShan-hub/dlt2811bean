package com.ysh.jcms.data.core;

import com.ysh.jcms.data.DefaultInnerOctetString;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.V;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for CHOICE types.
 *
 * <p>
 * The variant selection is stored in {@code inner._v} as {@code {"_choice":
 * "name", "name": value}}. Wrappers share the {@code _v} sub-map with the
 * selected variant, eliminating explicit data sync.
 */
public abstract class CmsChoice extends CmsType {

    private static final Logger LOG = Logger.getLogger(CmsChoice.class.getName());

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target(java.lang.annotation.ElementType.FIELD)
    public @interface Choice {
        /** Variant index (matches {@code CHOICE_XXX} constant). */
        int index();
        /** {@code _choice} string value. */
        String name();
        /**
         * Inner field name — defaults to the field name with {@code alt_} prefix
         * stripped. Override when the Inner field name differs (e.g. Cms field
         * {@code alt_boolean} but Inner key is {@code Boolean}).
         */
        String innerField() default "";
        /** Sync mode. {@link Sync#AUTO} detects from field type. */
        Sync sync() default Sync.AUTO;
    }

    public enum Sync {
        /**
         * Auto-detect: CmsScalar → SCALAR, List → LIST, InnerBase → INNER, else
         * WRAPPER.
         */
        AUTO,
        /** Direct inner.value access (for CmsScalar subclasses). */
        SCALAR,
        /** Delegate syncToInner/syncFromInner to wrapper. */
        WRAPPER,
        /** InnerBase field (e.g. DefaultInner* types) — share _v directly. */
        INNER,
        /** SEQUENCE OF with inner wrapper type. */
        LIST,
        /** Direct List field: inner has a plain list. */
        ARRAY,
        /** Direct value assignment for byte[], String, etc. */
        RAW,
    }

    // ── variant metadata built from @Choice annotations ──────────────

    private static class VariantInfo {
        final int index;
        final String name;
        final String innerField;
        final Sync sync;
        final Field field; // alt_* field on this CmsChoice subclass
        final boolean isScalar; // field type extends CmsScalar

        VariantInfo(int index, String name, String innerField, Sync sync, Field field, boolean isScalar) {
            this.index = index;
            this.name = name;
            this.innerField = innerField;
            this.sync = sync;
            this.field = field;
            this.isScalar = isScalar;
        }
    }

    /**
     * Per-class metadata built from {@code @Choice} annotations — variant
     * descriptions only (Field refs are class-level). Wrapper instances stay
     * per-object. NULL variants registered via {@link #registerNullChoice} at
     * construction time are still instance-level.
     */
    private static final ClassValue<ChoiceMeta> CHOICE_META = new ClassValue<ChoiceMeta>() {
        @Override
        protected ChoiceMeta computeValue(Class<?> type) {
            Map<Integer, VariantInfo> byIndex = new LinkedHashMap<>();
            Map<String, VariantInfo> byName = new LinkedHashMap<>();
            for (Field f : type.getFields()) {
                if (Modifier.isStatic(f.getModifiers()))
                    continue;
                Choice ann = f.getAnnotation(Choice.class);
                if (ann == null)
                    continue;

                String innerFn = ann.innerField().isEmpty() ? stripAlt(f.getName()) : ann.innerField();
                boolean isScalar = CmsScalar.class.isAssignableFrom(f.getType());

                Sync sync = ann.sync();
                if (sync == Sync.AUTO) {
                    if (isScalar)
                        sync = Sync.SCALAR;
                    else if (List.class.isAssignableFrom(f.getType()))
                        sync = Sync.ARRAY;
                    else if (InnerBase.class.isAssignableFrom(f.getType()))
                        sync = Sync.INNER;
                    else if (CmsType.class.isAssignableFrom(f.getType()))
                        sync = Sync.WRAPPER;
                    else
                        sync = Sync.RAW;
                }

                VariantInfo vi = new VariantInfo(ann.index(), ann.name(), innerFn, sync, f, isScalar);
                byIndex.put(ann.index(), vi);
                byName.put(ann.name(), vi);
            }
            return new ChoiceMeta(byIndex, byName);
        }
    };

    private static final class ChoiceMeta {
        final Map<Integer, VariantInfo> byIndex;
        final Map<String, VariantInfo> byName;

        ChoiceMeta(Map<Integer, VariantInfo> byIndex, Map<String, VariantInfo> byName) {
            this.byIndex = byIndex;
            this.byName = byName;
        }
    }

    private final Map<Integer, VariantInfo> variantByIndex = new LinkedHashMap<>();
    private final Map<String, VariantInfo> variantByName = new LinkedHashMap<>();
    /** Locally tracked variant index. */
    protected int selectedChoiceIndex = -1;

    protected CmsChoice(InnerBase inner) {
        super(inner);
        injectChoices();
    }

    /**
     * Register a NULL variant (no payload field). Call from subclass constructor
     * after {@code super(inner)} for variants that only store a {@code _choice}
     * string.
     */
    protected final void registerNullChoice(int index, String name) {
        VariantInfo vi = new VariantInfo(index, name, null, null, null, false);
        variantByIndex.put(index, vi);
        variantByName.put(name, vi);
    }

    /**
     * Rebind wrapper _v references after {@code inner} is replaced. Shares the
     * variant's {@code _v} sub-map with each wrapper.
     */
    public void rebindChoices() {
        for (VariantInfo vi : variantByIndex.values()) {
            if (vi.field == null || !CmsType.class.isAssignableFrom(vi.field.getType()))
                continue;
            try {
                CmsType w = (CmsType) vi.field.get(this);
                if (w == null)
                    continue;
                Object sub = inner._v.get(vi.name);
                if (sub instanceof LinkedHashMap) {
                    w.inner._v = (LinkedHashMap<String, Object>) sub;
                }
                if (w instanceof CmsChoice)
                    ((CmsChoice) w).rebindChoices();
                if (w instanceof CmsSequence)
                    ((CmsSequence) w).rebindWrappers();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "rebindChoices failed for variant " + vi.name + " in " + getClass().getSimpleName(), e);
            }
        }
    }

    private void injectChoices() {
        // Variant descriptions come from the per-class cache (no reflection scan);
        // only per-object work remains: creating wrapper instances + setting fields.
        for (VariantInfo vi : CHOICE_META.get(getClass()).byIndex.values()) {
            Field f = vi.field;
            if (f == null)
                continue;
            Class<?> ft = f.getType();

            if (CmsType.class.isAssignableFrom(ft)) {
                createCmsTypeWrapper(f, vi.name);
            } else if (InnerBase.class.isAssignableFrom(ft)) {
                try {
                    InnerBase val = (InnerBase) f.getType().getDeclaredConstructor().newInstance();
                    Object sub = inner._v.get(vi.name);
                    if (sub instanceof LinkedHashMap) {
                        val._v = (LinkedHashMap<String, Object>) sub;
                    }
                    f.set(this, val);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to init inner field " + f.getName(), e);
                }
            } else if (List.class.isAssignableFrom(ft)) {
                try {
                    f.set(this, new ArrayList<>());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to init list field " + f.getName(), e);
                }
            }
            // RAW fields (byte[], String) are left uninitialized

            variantByIndex.put(vi.index, vi);
            variantByName.put(vi.name, vi);
        }
    }

    private void createCmsTypeWrapper(Field f, String innerField) {
        try {
            CmsType wrapper = (CmsType) f.getType().getDeclaredConstructor().newInstance();
            // Share _v sub-map with the variant's entry
            Object sub = inner._v.get(innerField);
            if (sub instanceof LinkedHashMap) {
                wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
            }
            f.set(this, wrapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject CmsChoice field " + f.getName(), e);
        }
    }

    /** Get the current variant index. -1 means no variant selected. */
    public int choice() {
        return selectedChoiceIndex;
    }

    /** Select a variant by index. */
    public CmsChoice choice(int v) {
        selectedChoiceIndex = v;
        VariantInfo vi = variantByIndex.get(v);
        if (vi != null) {
            // Remove all old variant entries from _v
            for (VariantInfo old : variantByIndex.values()) {
                if (old != vi)
                    inner._v.remove(old.name);
            }
            V.setChoice(inner._v, vi.name);
            // Share the wrapper's _v with parent so writes go to the right place.
            // Both CmsType and InnerBase (DefaultInner*) variants keep the shared-_v
            // invariant: the entry in parent _v aliases the variant's own _v map.
            if (vi.field != null) {
                try {
                    if (CmsType.class.isAssignableFrom(vi.field.getType())) {
                        CmsType w = (CmsType) vi.field.get(this);
                        if (w != null) {
                            inner._v.put(vi.name, w.inner._v);
                        }
                    } else if (InnerBase.class.isAssignableFrom(vi.field.getType())) {
                        InnerBase val = (InnerBase) vi.field.get(this);
                        if (val != null) {
                            inner._v.put(vi.name, val._v);
                        }
                    }
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "choice(" + v + ") failed to seed variant " + vi.name + " in " + getClass().getSimpleName(), e);
                }
            }
        }
        return this;
    }

    @Override
    public byte[] encode() {
        syncToInner();
        return inner.encode();
    }

    @Override
    public void decode(byte[] data) {
        super.decode(data);
        rebindChoices();
        syncFromInner();
    }

    @Override
    public void syncToInner() {
        int ch = choice();
        if (ch < 0)
            throw new IllegalStateException("CHOICE variant not selected — call one of the alt*() setters before encode " + "(class="
                    + getClass().getSimpleName() + ")");
        VariantInfo vi = variantByIndex.get(ch);
        if (vi == null)
            return;

        // Clear all non-selected variant values from _v
        for (VariantInfo v : variantByIndex.values()) {
            if (v == vi)
                continue;
            inner._v.remove(v.name);
        }

        inner._v.put("_choice", vi.name);
        // NULL variants (registerNullChoice) carry no payload field — but JER
        // still needs the variant key present ({"Boolean": {"_": null}}), otherwise
        // toJson emits {} and the native encoder has no variant to encode.
        if (vi.sync == null) {
            inner._v.putIfAbsent(vi.name, V.wrapScalar(null));
            return;
        }
        try {
            switch (vi.sync) {
                case SCALAR :
                    syncScalarToInner(vi);
                    break;
                case WRAPPER :
                    syncWrapperToInner(vi);
                    break;
                case INNER :
                    syncInnerToInner(vi);
                    break;
                case LIST :
                    syncListToInner(vi);
                    break;
                case ARRAY :
                    syncListToInner(vi);
                    break;
                case RAW :
                    syncRawToInner(vi);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("syncToInner failed for variant " + vi.name, e);
        }
    }

    @Override
    public void syncFromInner() {
        String ch = V.choice(inner._v);
        if (ch == null) {
            normalizeVariant();
            ch = V.choice(inner._v);
            if (ch == null)
                return;
        }
        VariantInfo vi = variantByName.get(ch);
        if (vi == null)
            return;
        selectedChoiceIndex = vi.index;

        // NULL variants carry no payload field — nothing to sync
        if (vi.sync == null)
            return;
        try {
            switch (vi.sync) {
                case SCALAR :
                    syncScalarFromInner(vi);
                    break;
                case WRAPPER :
                    syncWrapperFromInner(vi);
                    break;
                case INNER :
                    syncInnerFromInner(vi);
                    break;
                case LIST :
                    syncListFromInner(vi);
                    break;
                case ARRAY :
                    syncListFromInner(vi);
                    break;
                case RAW :
                    syncRawFromInner(vi);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("syncFromInner failed for variant " + vi.name, e);
        }
    }

    /**
     * If {@code _v} holds JER form {@code {"variant": value}} (no {@code _choice}),
     * normalize it to the internal form {@code {"_choice": "variant", "variant":
     * {"_": value}}}.
     */
    protected final void normalizeVariant() {
        for (java.util.Map.Entry<String, Object> e : new ArrayList<>(inner._v.entrySet())) {
            if (e.getKey().startsWith("_"))
                continue;
            V.setChoice(inner._v, e.getKey());
            if (!(e.getValue() instanceof LinkedHashMap)) {
                inner._v.put(e.getKey(), V.wrapScalar(e.getValue()));
            }
            break;
        }
    }

    // ── sync helpers (all via _v, no reflection on Inner* fields) ────

    @SuppressWarnings("unchecked")
    private void syncScalarToInner(VariantInfo vi) throws Exception {
        CmsScalar wrapper = (CmsScalar) vi.field.get(this);
        if (wrapper == null)
            return;
        wrapper.syncToInner();
        // Use toJsonValue() so special scalars (e.g. unsigned Int32U) serialize
        // correctly
        inner._v.put(vi.name, wrapper.inner.toJsonValue());
    }

    @SuppressWarnings("unchecked")
    private void syncScalarFromInner(VariantInfo vi) throws Exception {
        CmsScalar wrapper = (CmsScalar) vi.field.get(this);
        if (wrapper == null)
            return;
        Object sub = inner._v.get(vi.name);
        if (sub instanceof LinkedHashMap) {
            wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
        } else if (sub != null) {
            V.setVal(wrapper.inner._v, sub);
        }
        wrapper.syncFromInner();
    }

    @SuppressWarnings("unchecked")
    private void syncWrapperToInner(VariantInfo vi) throws Exception {
        CmsType wrapper = (CmsType) vi.field.get(this);
        if (wrapper == null)
            return;
        wrapper.syncToInner();
    }

    @SuppressWarnings("unchecked")
    private void syncWrapperFromInner(VariantInfo vi) throws Exception {
        CmsType wrapper = (CmsType) vi.field.get(this);
        if (wrapper == null)
            return;
        Object sub = inner._v.get(vi.name);
        if (sub instanceof LinkedHashMap) {
            wrapper.inner._v = (LinkedHashMap<String, Object>) sub;
        } else if (sub != null && (wrapper instanceof CmsScalar || wrapper instanceof CmsBits)) {
            // Scalar / BIT STRING values stored directly (e.g. "0000"); wrap into _v
            V.setVal(wrapper.inner._v, sub);
        }
        if (wrapper instanceof CmsChoice)
            ((CmsChoice) wrapper).rebindChoices();
        if (wrapper instanceof CmsSequence)
            ((CmsSequence) wrapper).rebindWrappers();
        wrapper.syncFromInner();
    }

    @SuppressWarnings("unchecked")
    private void syncInnerToInner(VariantInfo vi) throws Exception {
        InnerBase val = (InnerBase) vi.field.get(this);
        if (val == null)
            return;
        // DefaultInner* types store their payload in val._v, which choice(int)
        // already aliased into parent _v. Just (re-)assert the alias so the
        // shared-_v invariant holds; byte[] stays in the map and JER hex is
        // produced by InnerBase's byte[] serializer at the JSON boundary.
        inner._v.put(vi.name, val._v);
    }

    @SuppressWarnings("unchecked")
    private void syncInnerFromInner(VariantInfo vi) throws Exception {
        Object sub = inner._v.get(vi.name);
        if (!(sub instanceof LinkedHashMap))
            return;
        InnerBase val = (InnerBase) vi.field.get(this);
        if (val != null) {
            LinkedHashMap<String, Object> m = (LinkedHashMap<String, Object>) sub;
            // OCTET STRING: JER hex string → byte[]
            if (val instanceof DefaultInnerOctetString) {
                Object raw = V.getVal(m);
                if (raw instanceof String)
                    V.setVal(m, InnerBase.unhex((String) raw));
            }
            // DefaultInner* stores everything in _v, so sharing the map is enough
            val._v = m;
        }
    }

    @SuppressWarnings("unchecked")
    private void syncListToInner(VariantInfo vi) throws Exception {
        List<CmsType> list = (List<CmsType>) vi.field.get(this);
        if (list == null)
            return;
        List<InnerBase> innerList = new ArrayList<>();
        for (CmsType elem : list) {
            elem.syncToInner();
            innerList.add(elem.inner);
        }
        inner._v.put(vi.name, innerList);
    }

    @SuppressWarnings("unchecked")
    private void syncListFromInner(VariantInfo vi) throws Exception {
        List<Object> innerList = (List<Object>) inner._v.get(vi.name);
        if (innerList == null)
            return;

        Class<? extends CmsType> elemClass = inferListElemClass(vi);
        if (elemClass == null)
            return;

        List<CmsType> list = (List<CmsType>) vi.field.get(this);
        if (list == null) {
            list = new ArrayList<>();
            vi.field.set(this, list);
        }
        list.clear();
        for (Object innerElem : innerList) {
            if (!(innerElem instanceof InnerBase))
                continue;
            CmsType elem = elemClass.getDeclaredConstructor().newInstance();
            elem.inner = (InnerBase) innerElem;
            elem.syncFromInner();
            list.add(elem);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends CmsType> inferListElemClass(VariantInfo vi) {
        java.lang.reflect.Type gt = vi.field.getGenericType();
        if (gt instanceof ParameterizedType) {
            return (Class<? extends CmsType>) ((ParameterizedType) gt).getActualTypeArguments()[0];
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void syncRawToInner(VariantInfo vi) throws Exception {
        Object val = vi.field.get(this);
        if (val instanceof byte[]) {
            // BIT STRING in JER: {"value": "UPPER-HEX", "length": BIT-COUNT} (rasn format)
            byte[] bytes = (byte[]) val;
            java.util.LinkedHashMap<String, Object> sub = new java.util.LinkedHashMap<>();
            sub.put("value", InnerBase.hex(bytes));
            sub.put("length", bytes.length * 8);
            inner._v.put(vi.name, sub);
        } else {
            inner._v.put(vi.name, val);
        }
    }

    @SuppressWarnings("unchecked")
    private void syncRawFromInner(VariantInfo vi) throws Exception {
        Object val = inner._v.get(vi.name);
        if (val == null)
            return;
        if (val instanceof java.util.Map && vi.field.getType() == byte[].class) {
            // BIT STRING in JER: {"length": N, "value": "HEX"}
            Object hex = ((java.util.Map<String, Object>) val).get("value");
            if (hex instanceof String) {
                vi.field.set(this, InnerBase.unhex((String) hex));
            }
        } else {
            vi.field.set(this, val);
        }
    }

    private static String stripAlt(String fieldName) {
        return fieldName.startsWith("alt_") ? fieldName.substring(4) : fieldName;
    }
}
