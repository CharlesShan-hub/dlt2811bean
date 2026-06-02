package com.ysh.jcms.datatypes.enumerated;

import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public abstract class AbstractCmsEnumerated<T extends AbstractCmsEnumerated<T>>
        extends AbstractCmsScalar<T, Integer> implements CmsEnumerated<T> {

    protected final int size;

    protected AbstractCmsEnumerated(String typeName, int value, int size) {
        super(typeName, 0);
        this.size = size;
        set(value);
    }

    @Override
    public void set(Integer value) {
        if (value < 0 || value >= size) {
            throw new IllegalArgumentException("value " + value + " out of range [0, " + (size - 1) + "]");
        }
        super.set(value);
    }

    @Override
    public boolean is(int value) {
        return this.value == value;
    }
}
