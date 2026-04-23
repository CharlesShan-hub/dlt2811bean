package com.ysh.dlt2811bean.utils.per.data;

public abstract class AbstractCmsScalar<T extends AbstractCmsScalar<T, V>, V> implements CmsScalar<T, V> {

    protected final String typeName;
    protected V value;

    protected AbstractCmsScalar(String typeName, V defaultValue) {
        this.typeName = typeName;
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
    public String toString() {
        return typeName + ": " + value;
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
}