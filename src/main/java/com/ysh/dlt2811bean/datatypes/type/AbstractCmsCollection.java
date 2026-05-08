package com.ysh.dlt2811bean.datatypes.type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerInteger;


public abstract class AbstractCmsCollection<T extends AbstractCmsCollection<T, E>, E>
        extends AbstractCmsScalar<T, List<E>>
        implements CmsCollection<T, E> {

    protected int capacity;

    protected AbstractCmsCollection(String typeName) {
        super(typeName, new ArrayList<>());
    }

    @Override
    public T capacity(int capacity) {
        this.capacity = capacity;
        return self();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return value.iterator();
    }

    protected void encodeLengthPrefix(PerOutputStream pos) {
        if (capacity <= 0) {
            throw new IllegalStateException("capacity must be set before encode");
        }
        PerInteger.encodeLength(pos, value.size());
    }

    protected int decodeLengthPrefix(PerInputStream pis) throws Exception {
        if (capacity <= 0) {
            throw new IllegalStateException("capacity must be set before decode");
        }
        return PerInteger.decodeLength(pis);
    }
}