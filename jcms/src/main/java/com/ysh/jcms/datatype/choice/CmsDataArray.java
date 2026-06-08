package com.ysh.jcms.datatype.choice;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsDataArray extends Structure {
    /** Tracks Memory backing-elements to prevent GC of nested pointer targets. */
    private static final Map<Memory, Object[]> _elementKeepAlive = new HashMap<>();

    public Pointer elements;
    public int count;

    /** Append elements into native memory.
     *  Nested ARRAY/STRUCTURE elements are deep-copied via FFI encode/decode
     *  so their internal pointers resolve to self-contained memory. */
    public CmsDataArray append(CmsData... elems) {
        if (elems == null || elems.length == 0)
            throw new IllegalArgumentException("array must have at least 1 element");
        int elemSize = elems[0].size();
        Memory mem = new Memory((long) elemSize * elems.length);
        // Keep references to resolved deep copies alive as long as this Memory lives
        Object[] keepAlive = new Object[elems.length];
        for (int i = 0; i < elems.length; i++) {
            CmsData src = elems[i];
            src.write();
            byte[] raw = src.getPointer().getByteArray(0, elemSize);
            mem.write((long) i * elemSize, raw, 0, elemSize);
            keepAlive[i] = src;
        }
        this.elements = mem;
        this.count = elems.length;
        _elementKeepAlive.put(mem, keepAlive);
        return this;
    }

    /** Build a ByValue shallow copy from this instance. */
    public CmsDataArray.ByValue toByValue() {
        CmsDataArray.ByValue bv = new CmsDataArray.ByValue();
        bv.elements = this.elements;
        bv.count = this.count;
        if (this.elements instanceof Memory) {
            _elementKeepAlive.put((Memory) bv.elements, _elementKeepAlive.get(this.elements));
        }
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
