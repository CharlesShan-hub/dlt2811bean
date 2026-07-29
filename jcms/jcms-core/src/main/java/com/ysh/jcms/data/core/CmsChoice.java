package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Base class for CHOICE types. Automates:
 * <ul>
 *   <li>variant index ↔ {@code _choice} string mapping via {@link CmsChoice @CmsChoice}</li>
 *   <li>wrapper creation and inner binding</li>
 *   <li>{@link #syncToInner()} / {@link #syncFromInner()} dispatch</li>
 * </ul>
 *
 * <p>The currently selected variant is stored in {@link #innerCache} under key {@code "choice"}.
 */
public abstract class CmsChoice extends CmsType {

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target(java.lang.annotation.ElementType.FIELD)
    public @interface Choice {
        /** Variant index (matches {@code CHOICE_XXX} constant). */
        int index();
        /** {@code _choice} string value in the Inner* class. */
        String name();
        /**
         * Inner field name — defaults to the field name with {@code alt_} prefix stripped.
         * Override when the Inner field name differs (e.g. Cms field {@code alt_boolean}
         * but Inner field is {@code Boolean}).
         */
        String innerField() default "";
        /** Sync mode. {@link Sync#AUTO} detects from field type. */
        Sync sync() default Sync.AUTO;
    }

    public enum Sync {
        /** Auto-detect: CmsScalar → SCALAR, List → LIST, InnerBase → INNER, else WRAPPER. */
        AUTO,
        /** Direct inner.value access (for CmsScalar subclasses). */
        SCALAR,
        /** Delegate syncToInner/syncFromInner to wrapper. */
        WRAPPER,
        /** Share inner reference directly (for DefaultInner* types). */
        INNER,
        /** SEQUENCE OF with inner wrapper type (e.g. InnerDataSequence.value list). */
        LIST,
        /** Direct List field: inner has a plain List<InnerType> field (no wrapper). */
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
        final Field field;         // alt_* field on this CmsChoice subclass
        Field innerF;              // corresponding field on Inner* class (nullable)
        final boolean isScalar;    // field type extends CmsScalar

        VariantInfo(int index, String name, String innerField, Sync sync,
                    Field field, Field innerF, boolean isScalar) {
            this.index = index;
            this.name = name;
            this.innerField = innerField;
            this.sync = sync;
            this.field = field;
            this.innerF = innerF;
            this.isScalar = isScalar;
        }
    }

    private final Map<Integer, VariantInfo> variantByIndex = new LinkedHashMap<>();
    private final Map<String, VariantInfo> variantByName = new LinkedHashMap<>();

    protected CmsChoice(InnerBase inner) {
        super(inner);
        injectChoices();
    }

    /**
     * Register a NULL variant (no payload field). Call from subclass constructor
     * after {@code super(inner)} for variants that only store a {@code _choice} string.
     */
    protected final void registerNullChoice(int index, String name) {
        VariantInfo vi = new VariantInfo(index, name, null, null, null, null, false);
        variantByIndex.put(index, vi);
        variantByName.put(name, vi);
    }

    /**
     * Re-resolve variant Inner* field references after {@code inner} is
     * replaced by a parent CmsSequence's {@code @CmsField} injection.
     */
    public void rebindChoices() {
        for (VariantInfo vi : variantByIndex.values()) {
            if (vi.field == null) continue;
            try {
                vi.innerF = inner.getClass().getField(vi.innerField);
            } catch (NoSuchFieldException e) {
                vi.innerF = null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void injectChoices() {
        for (Field f : getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            Choice ann = f.getAnnotation(Choice.class);
            if (ann == null) continue;

            String innerFn = ann.innerField().isEmpty() ? stripAlt(f.getName()) : ann.innerField();
            Field innerF = null;
            try {
                innerF = inner.getClass().getField(innerFn);
            } catch (NoSuchFieldException ignored) {}

            boolean isScalar = CmsScalar.class.isAssignableFrom(f.getType());
            boolean isList = List.class.isAssignableFrom(f.getType());
            boolean isInner = InnerBase.class.isAssignableFrom(f.getType());
            boolean isCmsType = CmsType.class.isAssignableFrom(f.getType());
            boolean isRaw = !isCmsType && !isList && !isInner;

            Sync sync = ann.sync();
            if (sync == Sync.AUTO) {
                if (isScalar) sync = Sync.SCALAR;
                else if (isList) {
                    // Check if inner field is a wrapper type (has .value) or plain List
                    sync = (innerF != null && hasListValueField(innerF))
                        ? Sync.LIST : Sync.ARRAY;
                }
                else if (isInner) sync = Sync.INNER;
                else if (isCmsType) sync = Sync.WRAPPER;
                else sync = Sync.RAW;
            }

            // Create wrapper instance if it's a CmsType, or keep raw value
            if (isCmsType) {
                createCmsTypeWrapper(f, innerF);
            } else if (isList) {
                try {
                    f.set(this, new ArrayList<>());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to init list field " + f.getName(), e);
                }
            } else if (isInner) {
                try {
                    if (innerF != null) {
                        Object innerVal = innerF.get(inner);
                        if (innerVal != null) {
                            f.set(this, innerVal);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to bind inner field " + f.getName(), e);
                }
            }
            // RAW fields (byte[], String) are left uninitialized

            VariantInfo vi = new VariantInfo(ann.index(), ann.name(), innerFn, sync, f, innerF, isScalar);
            variantByIndex.put(ann.index(), vi);
            variantByName.put(ann.name(), vi);
        }
    }

    private boolean hasListValueField(Field innerF) {
        try {
            Class<?> innerType = innerF.getType();
            // Check if the inner field's type has a "value" field (SEQUENCE OF wrapper)
            innerType.getField("value");
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private void createCmsTypeWrapper(Field f, Field innerF) {
        try {
            CmsType wrapper = (CmsType) f.getType().getDeclaredConstructor().newInstance();
            if (innerF != null && InnerBase.class.isAssignableFrom(innerF.getType())) {
                Object innerVal = innerF.get(inner);
                if (innerVal == null) {
                    innerVal = innerF.getType().getDeclaredConstructor().newInstance();
                    innerF.set(inner, innerVal);
                }
                wrapper.inner = (InnerBase) innerVal;
            }
            f.set(this, wrapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject CmsChoice field " + f.getName(), e);
        }
    }

    /** Get the current variant index. -1 means no variant selected. */
    public int choice() {
        Object v = innerCache.get("choice");
        return v instanceof Integer ? (Integer) v : -1;
    }

    /** Select a variant by index. */
    public CmsChoice choice(int v) {
        innerCache.put("choice", v);
        return this;
    }

    @Override
    public void syncToInner() {
        int ch = choice();
        if (ch < 0) return;
        VariantInfo vi = variantByIndex.get(ch);
        if (vi == null) return;

        // Clear previous variant data from innerCache (keep "choice")
        innerCache.keySet().removeIf(k -> !"choice".equals(k));

        try {
            innerSetChoice(vi.name);
            switch (vi.sync) {
                case SCALAR:
                    syncScalarToInner(vi);
                    break;
                case WRAPPER:
                    syncWrapperToInner(vi);
                    break;
                case INNER:
                    syncInnerToInner(vi);
                    break;
                case LIST:
                    syncListToInner(vi);
                    break;
                case ARRAY:
                    syncArrayToInner(vi);
                    break;
                case RAW:
                    syncRawToInner(vi);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("syncToInner failed for variant " + vi.name, e);
        }
        super.syncToInner();
        storeVariantInCache(vi);
    }

    @Override
    public void syncFromInner() {
        String ch = innerGetChoice();
        if (ch == null) { innerCache.put("choice", -1); return; }
        VariantInfo vi = variantByName.get(ch);
        if (vi == null) { innerCache.put("choice", -1); return; }

        // Clear previous variant data from innerCache (keep "choice")
        innerCache.keySet().removeIf(k -> !"choice".equals(k));

        innerCache.put("choice", vi.index);
        try {
            switch (vi.sync) {
                case SCALAR:
                    syncScalarFromInner(vi);
                    break;
                case WRAPPER:
                    syncWrapperFromInner(vi);
                    break;
                case INNER:
                    syncInnerFromInner(vi);
                    break;
                case LIST:
                    syncListFromInner(vi);
                    break;
                case ARRAY:
                    syncArrayFromInner(vi);
                    break;
                case RAW:
                    syncRawFromInner(vi);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("syncFromInner failed for variant " + vi.name, e);
        }
        storeVariantInCache(vi);
    }

    /**
     * Store the selected variant's field value in innerCache under the field name.
     *
     * <ul>
     *   <li>CmsType wrappers → store wrapper.innerCache (for recursive comparison)</li>
     *   <li>InnerBase types (DefaultInner*) → unwrap .value field</li>
     *   <li>Others → store as-is (byte[], List, etc.)</li>
     * </ul>
     */
    private void storeVariantInCache(VariantInfo vi) {
        if (vi == null || vi.field == null) return;
        try {
            Object val = vi.field.get(this);
            String key = vi.field.getName();
            if (val instanceof CmsType) {
                innerCache.put(key, ((CmsType) val).innerCache);
            } else if (val instanceof InnerBase) {
                // DefaultInner* types — store the inner .value directly
                try {
                    Field vf = val.getClass().getField("value");
                    innerCache.put(key, vf.get(val));
                } catch (NoSuchFieldException e) {
                    innerCache.put(key, val); // fallback: store as-is
                }
            } else {
                innerCache.put(key, val);
            }
        } catch (Exception e) {
            // skip if field cannot be read
        }
    }

    // ── sync helpers ─────────────────────────────────────────────────

    private void innerSetChoice(String name) {
        try {
            Field cf = inner.getClass().getField("_choice");
            cf.set(inner, name);
        } catch (Exception ignored) {}
    }

    private String innerGetChoice() {
        try {
            Field cf = inner.getClass().getField("_choice");
            return (String) cf.get(inner);
        } catch (Exception e) { return null; }
    }

    @SuppressWarnings("unchecked")
    private void syncScalarToInner(VariantInfo vi) throws Exception {
        CmsScalar wrapper = (CmsScalar) vi.field.get(this);
        if (wrapper == null) return;
        wrapper.syncToInner();
    }

    private void syncScalarFromInner(VariantInfo vi) throws Exception {
        CmsScalar wrapper = (CmsScalar) vi.field.get(this);
        if (wrapper == null || vi.innerF == null) return;
        Object innerVal = vi.innerF.get(inner);
        if (innerVal instanceof InnerBase) {
            wrapper.inner = (InnerBase) innerVal;
        }
        wrapper.syncFromInner();
    }

    private void syncWrapperToInner(VariantInfo vi) throws Exception {
        CmsType wrapper = (CmsType) vi.field.get(this);
        if (wrapper == null) return;
        wrapper.syncToInner();
        if (vi.innerF != null) {
            vi.innerF.set(inner, wrapper.inner);
        }
    }

    private void syncWrapperFromInner(VariantInfo vi) throws Exception {
        CmsType wrapper = (CmsType) vi.field.get(this);
        if (wrapper == null || vi.innerF == null) return;
        Object innerVal = vi.innerF.get(inner);
        if (innerVal instanceof InnerBase) {
            wrapper.inner = (InnerBase) innerVal;
        }
        wrapper.syncFromInner();
    }

    @SuppressWarnings("unchecked")
    private void syncInnerToInner(VariantInfo vi) throws Exception {
        Object val = vi.field.get(this);
        if (val instanceof InnerBase && vi.innerF != null) {
            vi.innerF.set(inner, val);
        }
    }

    @SuppressWarnings("unchecked")
    private void syncInnerFromInner(VariantInfo vi) throws Exception {
        if (vi.innerF == null) return;
        Object innerVal = vi.innerF.get(inner);
        Class<?> valType = vi.field.getType();
        if (innerVal != null && valType.isAssignableFrom(innerVal.getClass())) {
            vi.field.set(this, innerVal);
        }
    }

    @SuppressWarnings("unchecked")
    private void syncListToInner(VariantInfo vi) throws Exception {
        List<CmsType> list = (List<CmsType>) vi.field.get(this);
        if (list == null) return;
        if (vi.innerF == null) return;
        Object innerContainer = vi.innerF.get(inner);
        if (innerContainer == null) return;
        Field valueF = innerContainer.getClass().getField("value");
        List<Object> innerList = (List<Object>) valueF.get(innerContainer);
        if (innerList == null) {
            innerList = new ArrayList<>();
            valueF.set(innerContainer, innerList);
        }
        innerList.clear();
        for (CmsType elem : list) {
            elem.syncToInner();
            innerList.add(elem.inner);
        }
    }

    @SuppressWarnings("unchecked")
    private void syncListFromInner(VariantInfo vi) throws Exception {
        if (vi.innerF == null) return;
        Object innerContainer = vi.innerF.get(inner);
        if (innerContainer == null) return;
        Field valueF = innerContainer.getClass().getField("value");
        List<Object> innerList = (List<Object>) valueF.get(innerContainer);
        if (innerList == null) return;

        List<CmsType> list = (List<CmsType>) vi.field.get(this);
        if (list == null) {
            list = new ArrayList<>();
            vi.field.set(this, list);
        }
        Class<? extends CmsType> elemClass = inferListElemClass(vi);
        if (elemClass == null) return;

        list.clear();
        for (Object innerElem : innerList) {
            if (!(innerElem instanceof InnerBase)) continue;
            CmsType elem = elemClass.getDeclaredConstructor().newInstance();
            elem.inner = (InnerBase) innerElem;
            elem.syncFromInner();
            list.add(elem);
        }
    }

    @SuppressWarnings("unchecked")
    private void syncArrayToInner(VariantInfo vi) throws Exception {
        List<CmsType> list = (List<CmsType>) vi.field.get(this);
        if (list == null || vi.innerF == null) return;
        List<Object> innerList = new ArrayList<>();
        for (CmsType elem : list) {
            elem.syncToInner();
            innerList.add(elem.inner);
        }
        vi.innerF.set(inner, innerList);
    }

    @SuppressWarnings("unchecked")
    private void syncArrayFromInner(VariantInfo vi) throws Exception {
        if (vi.innerF == null) return;
        List<Object> innerList = (List<Object>) vi.innerF.get(inner);
        if (innerList == null) return;

        Class<? extends CmsType> elemClass = inferListElemClass(vi);
        if (elemClass == null) return;

        List<CmsType> list = (List<CmsType>) vi.field.get(this);
        if (list == null) {
            list = new ArrayList<>();
            vi.field.set(this, list);
        }
        list.clear();
        for (Object innerElem : innerList) {
            if (!(innerElem instanceof InnerBase)) continue;
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
            return (Class<? extends CmsType>) ((ParameterizedType) gt)
                    .getActualTypeArguments()[0];
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void syncRawToInner(VariantInfo vi) throws Exception {
        Object val = vi.field.get(this);
        if (vi.innerF != null) {
            vi.innerF.set(inner, val);
        }
    }

    @SuppressWarnings("unchecked")
    private void syncRawFromInner(VariantInfo vi) throws Exception {
        if (vi.innerF == null) return;
        Object val = vi.innerF.get(inner);
        if (val != null) {
            vi.field.set(this, val);
        }
    }

    private static String stripAlt(String fieldName) {
        return fieldName.startsWith("alt_") ? fieldName.substring(4) : fieldName;
    }
}
