package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerEnumerated;

public abstract class AbstractCmsEnumerated<T extends AbstractCmsEnumerated<T>> extends AbstractCmsScalar<T, Integer> implements CmsEnumerated<T> {

    private static final int initSize = -1;
    protected int size = initSize;

    protected AbstractCmsEnumerated(String typeName, int value, int size) {
        super(typeName, value);
        this.size = size;
    }

    // ==================== Public API ====================

    @Override
    public T set(Integer value) {
        super.set(value);
        validate();
        return self();
    }

    @Override
    public boolean is(int value) {
        return this.value == value;
    }

    // ==================== Encode / Decode ====================

    @Override
    public void encode(PerOutputStream pos) {
        PerEnumerated.encode(pos, value, size - 1);
    }

    @Override
    public T decode(PerInputStream pis) throws Exception {
        set(PerEnumerated.decode(pis, size - 1));
        return self();
    }

    // ==================== Private Helpers ====================

    private void validate() {
        if (size == initSize) {
            throw new IllegalArgumentException("size is not set");
        }
        if (value < 0 || value >= size) { // size = max index + 1
            throw new IllegalArgumentException(
                    String.format("Enumerated value %d out of range [0, %d]", value, size - 1));
        }
    }
}
