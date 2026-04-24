package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * SEQUENCE OF Data type representing a collection of heterogeneous elements (structure).
 * <p>
 * Unlike {@link CmsArray}, CmsStructure elements can be different Data value types.
 * Values added via {@link #add(CmsType)} are automatically wrapped in {@link CmsData}.
 * <p>
 * Encode example:
 * <pre>{@code
 * CmsData.write(pos, new CmsStructure().max(10)
 *     .add(new CmsInt32(42))
 *     .add(new CmsVisibleString("hello").max(10)));
 * }</pre>
 * <p>
 * Decode example:
 * <pre>{@code
 * CmsStructure template = new CmsStructure().max(10)
 *     .add(new CmsInt32())
 *     .add(new CmsVisibleString("").max(10));
 * CmsData.read(pis, template);
 * int val = ((CmsInt32) ((CmsData<?>) template.get(0)).get()).get();
 * }</pre>
 */
public class CmsStructure extends AbstractCmsScalar<CmsStructure, List<CmsData<?>>> implements Iterable<CmsData<?>> {

    private int max;

    public CmsStructure() {
        super("SEQUENCE OF", new ArrayList<>());
    }

    public CmsStructure max(int max) {
        this.max = max;
        return this;
    }

    public int getMax() {
        return max;
    }

    // public CmsStructure add(CmsData<?> item) {
    //     Objects.requireNonNull(item, "item cannot be null");
    //     value.add(item);
    //     return this;
    // }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsStructure add(CmsType<?> item) {
        Objects.requireNonNull(item, "item cannot be null");
        CmsData data = new CmsData();
        data.value = (CmsType) item;
        value.add(data);
        return this;
    }

    public CmsData<?> get(int index) {
        return value.get(index);
    }

    public int size() {
        return value.size();
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public Iterator<CmsData<?>> iterator() {
        return value.iterator();
    }

    @Override
    public void encode(PerOutputStream pos) {
        if (max <= 0) {
            throw new IllegalStateException("max must be set before encode");
        }
        PerInteger.encodeLength(pos, value.size());
        for (CmsData<?> item : value) {
            item.encode(pos);
        }
    }

    @Override
    public CmsStructure decode(PerInputStream pis) throws Exception {
        int count = PerInteger.decodeLength(pis);
        for (int i = 0; i < count; i++) {
            CmsData<?> item;
            if (i < value.size()) {
                item = value.get(i);
            } else {
                item = new CmsData<>();
                value.add(item);
            }
            item.decode(pis);
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Structure[");
        for (int i = 0; i < value.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(value.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}