package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CmsArray<T extends CmsType<?>> extends AbstractCmsScalar<CmsArray<T>, List<T>> implements Iterable<T> {

    private final Class<T> elementType;
    private int max;

    public CmsArray(Class<T> elementType) {
        super("SEQUENCE OF", new ArrayList<>());
        this.elementType = Objects.requireNonNull(elementType, "elementType cannot be null");
    }

    public CmsArray<T> max(int max) {
        this.max = max;
        return this;
    }

    public int getMax() {
        return max;
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

    public int size() {
        return value.size();
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public List<T> toList() {
        return new ArrayList<>(value);
    }

    public Stream<T> stream() {
        return value.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return value.iterator();
    }

    @Override
    public void encode(PerOutputStream pos) {
        if (max <= 0) {
            throw new IllegalStateException("max must be set before encode");
        }
        PerInteger.encodeLength(pos, value.size());
        for (T item : value) {
            item.encode(pos);
        }
    }

    @Override
    public CmsArray<T> decode(PerInputStream pis) throws Exception {
        value.clear();
        int count = PerInteger.decodeLength(pis);
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