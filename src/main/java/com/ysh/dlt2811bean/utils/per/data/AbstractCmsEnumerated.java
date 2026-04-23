package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerEnumerated;

public abstract class AbstractCmsEnumerated<T extends AbstractCmsEnumerated<T>> implements CmsEnumerated<T> {

    private static final int initSize = -1;
    protected final String typeName;
    protected int value;
    protected int size = initSize;

    protected AbstractCmsEnumerated(String typeName, int value, int size) {
        this.typeName = typeName;
        this.value = value;
        this.size = size;
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerEnumerated.encode(pos, value, size - 1);
    }

    @Override
    public T decode(PerInputStream pis) throws Exception {
        set(PerEnumerated.decode(pis, size - 1));
        return self();
    }

    public T set(Integer value) {
        validate();
        this.value = value;
        return self();
    }

    public Integer get() {
        return value;
    }

    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

    @Override
    public boolean is(int value) {
        return this.value == value;
    }

    @Override
    public String toString() {
        return typeName + "[" + value + "]";
    }

    private void validate() {
        if (size == initSize) {
            throw new IllegalStateException("size is not set");
        }
        if (value < 0 || value >= size) { // size = max index + 1
            throw new IllegalArgumentException(
                    String.format("Enumerated value %d out of range [0, %d]", value, size - 1));
        }
    }
}
