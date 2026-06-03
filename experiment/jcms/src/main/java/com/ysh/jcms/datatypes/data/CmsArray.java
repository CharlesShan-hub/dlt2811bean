package com.ysh.jcms.datatypes.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.ysh.jcms.datatypes.type.CmsCollection;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CmsArray — container for Data CHOICE array/structure elements.
 *
 * <p>Wraps the C {@code { struct cms_data *elements; int count; }} struct.
 * Always embedded inside {@link CmsData} for choice=ARRAY or choice=STRUCTURE.
 *
 * <p>Thread-safety: not guaranteed.
 */
@Getter
@Accessors(fluent = true)
public class CmsArray implements CmsCollection<CmsArray, CmsData> {

    /** Native struct matching {@code { struct cms_data *elements; int count; }}. */
    public static class ArrayStruct extends Structure {
        public Pointer elements;   // struct cms_data*
        public int count;

        public ArrayStruct() {}

        public ArrayStruct(Pointer p) {
            super(p);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("elements", "count");
        }
    }

    /** Java-level list of decoded/encoded elements. */
    public List<CmsData> elements;

    public CmsArray() {
        this.elements = new ArrayList<>();
    }

    public CmsArray(List<CmsData> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public CmsData get(int index) {
        return elements.get(index);
    }

    public CmsArray add(CmsData element) {
        elements.add(element);
        return this;
    }

    public CmsArray addAll(List<? extends CmsData> items) {
        elements.addAll(items);
        return this;
    }

    @Override
    public List<CmsData> elements() {
        return elements;
    }

    public CmsArray copy() {
        CmsArray clone = new CmsArray();
        for (CmsData e : elements) {
            clone.elements.add(e.copy());
        }
        return clone;
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
