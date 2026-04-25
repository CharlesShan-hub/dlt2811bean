package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * SEQUENCE OF type representing a collection of homogeneous elements.
 * <p>
 * Encode example:
 * <pre>{@code
 * CmsArray<CmsInt32> arr = new CmsArray<>(CmsInt32.class).max(10);
 * arr.add(new CmsInt32(100)).add(new CmsInt32(200));
 * CmsData.write(pos, arr);
 * }</pre>
 * <p>
 * Decode example:
 * <pre>{@code
 * CmsArray<CmsInt32> template = new CmsArray<>(CmsInt32.class).max(10);
 * CmsData.read(pis, template);
 * int val = template.get(0).get();
 * }</pre>
 */
public class CmsArray<T extends CmsType<?>> extends AbstractCmsCollection<CmsArray<T>, T> {

    private final Class<T> elementType;

    public CmsArray(Class<T> elementType) {
        super("SEQUENCE OF");
        this.elementType = Objects.requireNonNull(elementType, "elementType cannot be null");
    }

    public CmsArray<T> add(T item) {
        Objects.requireNonNull(item, "item cannot be null");
        value.add(item);
        return this;
    }

    public CmsArray<T> addAll(List<? extends T> items) {
        for (T item : items) {
            add(item);
        }
        return this;
    }

    public T get(int index) {
        return value.get(index);
    }

    public List<T> toList() {
        return new ArrayList<>(value);
    }

    public Stream<T> stream() {
        return value.stream();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsArray<T> copy() {
        CmsArray<T> clone = new CmsArray<>(elementType).capacity(capacity);
        for (T item : value) {
            clone.add((T) item.copy());
        }
        return clone;
    }

    @Override
    public void encode(PerOutputStream pos) {
        encodeLengthPrefix(pos);
        for (T item : value) {
            item.encode(pos);
        }
    }

    @Override
    public CmsArray<T> decode(PerInputStream pis) throws Exception {
        value.clear();
        int count = decodeLengthPrefix(pis);
        for (int i = 0; i < count; i++) {
            T item = elementType.getDeclaredConstructor().newInstance();
            item.decode(pis);
            value.add(item);
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(typeName).append("[");
        for (int i = 0; i < value.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(value.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}