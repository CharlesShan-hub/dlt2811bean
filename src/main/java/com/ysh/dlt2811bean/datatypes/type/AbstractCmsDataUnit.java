package com.ysh.dlt2811bean.datatypes.type;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerChoice;

/**
 * Abstract base class for CMS data unit types.
 * Provides common encode/decode/copy logic based on CHOICE encoding.
 *
 * @param <T> the concrete type extending this class
 * @param <V> the value type held by this data unit
 */
public abstract class AbstractCmsDataUnit<T extends AbstractCmsDataUnit<T, V>, V extends CmsType<V>>
        extends AbstractCmsScalar<T, V>
        implements CmsDataUnit<T, V> {

    protected AbstractCmsDataUnit(String typeName, V defaultValue) {
        super(typeName, defaultValue);
    }

    /**
     * Returns the CHOICE index for the given value type.
     */
    protected abstract int choiceIndex(Class<?> type);

    /**
     * Creates a new value instance for the given CHOICE index.
     */
    protected abstract V createValue(int index);

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public void encode(PerOutputStream pos) {
        if (value == null) {
            throw new IllegalStateException("value must be set before encode");
        }
        PerChoice.encode(pos, choiceIndex(value.getClass()));
        value.encode(pos);
    }

    @Override
    public T decode(PerInputStream pis) throws Exception {
        int idx = PerChoice.decode(pis);
        if (value == null) {
            value = createValue(idx);
        }
        value.decode(pis);
        return self();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T copy() {
        T clone;
        try {
            clone = (T) getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy " + typeName, e);
        }
        if (value != null) {
            ((AbstractCmsDataUnit<T, V>) clone).setValue((V) ((CmsType<?>) value).copy());
        }
        return clone;
    }

    @Override
    public String toString() {
        if (value == null) {
            return typeName + "[unset]";
        }
        return typeName + "[" + choiceIndex(value.getClass()) + "]=" + value;
    }
}
