package com.ysh.dlt2811bean.utils.per.data;

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
}