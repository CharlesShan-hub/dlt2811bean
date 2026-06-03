package com.ysh.dlt2811bean.datatypes.type;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractCmsCompound<T extends AbstractCmsCompound<T>>
        extends AbstractCmsType<T> implements CmsCompound<T> {

    private boolean fieldsRegistered = false;
    private final List<String> fieldNames = new ArrayList<>();
    private final Set<String> removedFields = new HashSet<>();

    protected AbstractCmsCompound(String typeName) {
        super(typeName);
    }

    private void ensureFieldsRegistered() {
        if (fieldsRegistered) return;
        fieldsRegistered = true;
        
        List<Class<?>> hierarchy = new ArrayList<>();
        Class<?> curr = getClass();
        while (curr != AbstractCmsCompound.class && curr != null) {
            hierarchy.add(0, curr);
            curr = curr.getSuperclass();
        }

        for (Class<?> clazz : hierarchy) {
            for (java.lang.reflect.Field f : clazz.getDeclaredFields()) {
                CmsField ann = f.getAnnotation(CmsField.class);
                if (ann != null && !fieldNames.contains(f.getName())
                        && !removedFields.contains(f.getName())
                        && acceptField(ann)) {
                    registerField(f.getName());
                    getField(f.getName()).setOptional(ann.optional());
                }
            }
        }
    }

    protected boolean acceptField(CmsField ann) {
        return ann.only().length == 0;
    }

    protected final void registerField(String name) {
        fieldNames.add(name);
    }

    protected final void registerOptionalField(String name) {
        fieldNames.add(name);
        getField(name).setOptional(true);
    }

    protected final void removeField(String name) {
        removedFields.add(name);
        int idx = fieldNames.indexOf(name);
        if (idx >= 0) {
            fieldNames.remove(idx);
        }
    }

    public boolean isFieldPresent(String name) {
        return getField(name).isPresent();
    }

    public T setFieldPresent(String name, boolean present) {
        getField(name).setPresent(present);
        return self();
    }

    private AbstractCmsType<?> getField(String name) {
        try {
            java.lang.reflect.Field f = getClass().getField(name);
            return (AbstractCmsType<?>) f.get(this);
        } catch (Exception e) {
            throw new RuntimeException("Field not accessible: " + name, e);
        }
    }

    protected void validate() {
    }

    @Override
    public void encode(PerOutputStream pos) {
        ensureFieldsRegistered();
        validate();
        for (int i = 0; i < fieldNames.size(); i++) {
            String name = fieldNames.get(i);
            AbstractCmsType<?> field = getField(name);
            if (field.isOptional()) {
                pos.writeBit(field.isPresent());
            }
        }
        for (int i = 0; i < fieldNames.size(); i++) {
            String name = fieldNames.get(i);
            AbstractCmsType<?> field = getField(name);
            if (field.isOptional() && !field.isPresent()) continue;
            field.encode(pos);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T decode(PerInputStream pis) throws Exception {
        ensureFieldsRegistered();
        for (int i = 0; i < fieldNames.size(); i++) {
            String name = fieldNames.get(i);
            AbstractCmsType<?> field = getField(name);
            if (field.isOptional()) {
                field.setPresent(pis.readBit());
            }
        }
        for (int i = 0; i < fieldNames.size(); i++) {
            String name = fieldNames.get(i);
            AbstractCmsType<?> field = getField(name);
            if (field.isOptional() && !field.isPresent()) continue;
            field.decode(pis);
        }
        validate();
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T copy() {
        ensureFieldsRegistered();
        try {
            T clone = (T) getClass().getDeclaredConstructor().newInstance();
            for (String name : fieldNames) {
                AbstractCmsType<?> field = getField(name);
                AbstractCmsType<?> clonedField = (AbstractCmsType<?>) field.copy();
                java.lang.reflect.Field f = getClass().getField(name);
                f.set(clone, clonedField);
        }
        return clone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy " + typeName, e);
        }
    }

    @Override
    public String toString() {
        ensureFieldsRegistered();
        return toString(0);
    }

    public String toString(int depth) {
        String indent = "    ".repeat(depth + 1);
        String bracketIndent = "    ".repeat(depth);
        StringBuilder sb = new StringBuilder("(").append(getClass().getSimpleName()).append(") {\n");
        for (String name : fieldNames) {
            AbstractCmsType<?> field = getField(name);
            String fieldStr = field instanceof AbstractCmsCompound
                ? ((AbstractCmsCompound<?>) field).toString(depth + 1)
                : field.toString();
            sb.append(indent).append(name).append(": ").append(fieldStr).append(",\n");
        }
        if (!fieldNames.isEmpty()) {
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }
        sb.append(bracketIndent).append("}");
        return sb.toString();
    }
}