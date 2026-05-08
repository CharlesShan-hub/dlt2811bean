package com.ysh.dlt2811bean.datatypes.type;

public abstract class AbstractCmsType<T extends AbstractCmsType<T>> implements CmsType<T> {

    protected final String typeName;
    protected boolean optional = false;
    protected boolean present = true;

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

    public boolean isOptional() {
        return optional;
    }

    public T setOptional(boolean optional) {
        this.optional = optional;
        return self();
    }

    public boolean isPresent() {
        return present;
    }

    public T setPresent(boolean present) {
        this.present = present;
        return self();
    }

    protected static <T> T requireNotNull(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        return obj;
    }
}