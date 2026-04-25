package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCmsCompound<T extends AbstractCmsCompound<T>>
        extends AbstractCmsType<T> implements CmsCompound<T> {

    private final List<String> fieldNames = new ArrayList<>();

    protected AbstractCmsCompound(String typeName) {
        super(typeName);
    }

    protected final void registerField(String name) {
        fieldNames.add(name);
    }

    @SuppressWarnings("unchecked")
    private AbstractCmsType<?> getField(String name) {
        try {
            Field f = getClass().getField(name);
            return (AbstractCmsType<?>) f.get(this);
        } catch (Exception e) {
            throw new RuntimeException("Field not accessible: " + name, e);
        }
    }

    protected void validate() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void encode(PerOutputStream pos) {
        validate();
        for (String name : fieldNames) {
            getField(name).encode(pos);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T decode(PerInputStream pis) throws Exception {
        for (String name : fieldNames) {
            getField(name).decode(pis);
        }
        validate();
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T copy() {
        try {
            T clone = (T) getClass().getDeclaredConstructor().newInstance();
            for (String name : fieldNames) {
                AbstractCmsType<?> field = getField(name);
                AbstractCmsType<?> clonedField = (AbstractCmsType<?>) field.copy();
                Field f = getClass().getField(name);
                f.set(clone, clonedField);
            }
            return clone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy " + typeName, e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(typeName).append("[");
        boolean first = true;
        for (String name : fieldNames) {
            if (!first) sb.append(", ");
            sb.append(name).append("=").append(getField(name));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }
}