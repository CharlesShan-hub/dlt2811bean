package com.ysh.jcms.datatype.choice;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsDataArray extends Structure {
    private static final Map<CmsDataArray, Memory> _backingMemory = new WeakHashMap<>();

    public Pointer elements;
    public int count;

    /** Append elements into native memory, managing caching internally. */
    public CmsDataArray append(CmsData... elems) {
        if (elems == null || elems.length == 0)
            throw new IllegalArgumentException("array must have at least 1 element");
        int elemSize = elems[0].size();
        Memory mem = new Memory((long) elemSize * elems.length);
        for (int i = 0; i < elems.length; i++) {
            elems[i].write();
            byte[] raw = elems[i].getPointer().getByteArray(0, elemSize);
            mem.write((long) i * elemSize, raw, 0, elemSize);
        }
        this.elements = mem;
        this.count = elems.length;
        _backingMemory.put(this, mem);
        return this;
    }

    /** Build a ByValue shallow copy from this instance. */
    public CmsDataArray.ByValue toByValue() {
        CmsDataArray.ByValue bv = new CmsDataArray.ByValue();
        bv.elements = this.elements;
        bv.count = this.count;
        Memory m = _backingMemory.get(this);
        if (m != null) _backingMemory.put(bv, m);
        return bv;
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("elements", "count");
    }

    public static class ByValue extends CmsDataArray implements Structure.ByValue {
        public ByValue() {}
    }
}
