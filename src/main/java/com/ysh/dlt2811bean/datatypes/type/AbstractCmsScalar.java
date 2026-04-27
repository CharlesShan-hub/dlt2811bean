package com.ysh.dlt2811bean.datatypes.type;

public abstract class AbstractCmsScalar<T extends AbstractCmsScalar<T, V>, V>
        extends AbstractCmsType<T> implements CmsScalar<T, V> {

    protected V value;

    protected AbstractCmsScalar(String typeName, V defaultValue) {
        super(typeName);
        this.value = defaultValue;
    }

    @Override
    public T set(V value) {
        if (value == null) {
            throw new IllegalArgumentException(typeName + " value cannot be null");
        }
        this.value = value;
        return self();
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T copy() {
        try {
            T clone = (T) getClass().getDeclaredConstructor().newInstance();
            clone.set(this.value);
            return clone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy " + typeName, e);
        }
    }

    @Override
    public String toString() {
        return typeName + ": " + value;
    }
}