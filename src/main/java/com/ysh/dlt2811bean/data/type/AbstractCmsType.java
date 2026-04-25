package com.ysh.dlt2811bean.data.type;

public abstract class AbstractCmsType<T extends AbstractCmsType<T>> implements CmsType<T> {

    protected final String typeName;

    protected AbstractCmsType(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    protected static <T> T requireNotNull(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        return obj;
    }
}