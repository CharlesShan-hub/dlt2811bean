package com.ysh.jcms.datatype.choice;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.*;
import com.ysh.jcms.datatype.common.*;
import com.ysh.jcms.datatype.control.CmsCheck;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import com.ysh.jcms.datatype.extended.CmsUtcTime;
import com.ysh.jcms.ffi.CmsField;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.jcms.util.CmsDataFormatter;
import java.util.Objects;
import static com.ysh.jcms.datatype.choice.CmsDataType.*;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsData extends CmsField {
    /** Choice discriminator as a CmsType. */
    public CmsDataType.ByValue choice = new CmsDataType.ByValue();
    public CmsDataUnion value = new CmsDataUnion();

    public CmsData() {
        formatter = CmsDataFormatter.INSTANCE;
    }

    @Override
    public CmsData test() {
        super.test();
        Object active = value.get(choice.value());
        if (active instanceof CmsField && !(active instanceof CmsUint8Array)) {
            ((CmsField) active).test();
        }
        return this;
    }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CmsData)) return false;
        CmsData other = (CmsData) o;
        int c = choice.value();
        if (c != other.choice.value()) return false;
        switch (c) {
            case ARRAY:
                if (value.array.count != other.value.array.count) return false;
                return Objects.equals(value.array.elements, other.value.array.elements);
            case STRUCTURE:
                if (value.structure.count != other.value.structure.count) return false;
                return Objects.equals(value.structure.elements, other.value.structure.elements);
            default:
                return Objects.equals(value.get(c), other.value.get(c));
        }
    }

    @Override
    public int hashCode() {
        int c = choice.value();
        switch (c) {
            case ARRAY:     return Objects.hash(c, value.array.elements);
            case STRUCTURE: return Objects.hash(c, value.structure.elements);
            default:        return Objects.hash(c, value.get(c));
        }
    }

    // ==================== Factory: by CmsType ====================

    /** 通用工厂：自动推断 choice，通过字段反射拷贝值到 ByValue。 */
    public static CmsData of(CmsType val) {
        int c = choiceFor(val.getClass());
        if (c < 0) throw new IllegalArgumentException("unrecognized type: " + val.getClass());
        // CmsUint8Array 有 4 个 choice，默认 VISIBLE_STRING（16）
        if (c == 14 || c == 15 || c == 17) c = 16;

        Class<?> bvClass = unionClass(c);
        CmsType bv = newBv(bvClass);
        copyFields(val, bv);

        CmsData d = new CmsData();
        d.choice().value(c);
        d.value.setType(bvClass);
        d.value.set(c, bv);
        return d;
    }

    /** Create a CmsData with a scalar value: (INT32, 42), (BOOLEAN, true), (DBPOS, 1) */
    public static CmsData of(int c, Object val) {
        CmsData d = new CmsData();
        d.choice().value(c);
        d.value.setType(unionClass(c));
        d.value.set(c, val);
        return d;
    }

    @SuppressWarnings("unchecked")
    private static <T extends com.sun.jna.Structure> T newBv(Class<?> cls) {
        try {
            return (T) cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate " + cls, e);
        }
    }

    /** 反射拷贝：将 src 的 public 字段值赋给 dst（相同字段名）。 */
    private static void copyFields(CmsType src, CmsType dst) {
        Class<?> clazz = src.getClass();
        while (clazz != null && clazz != com.sun.jna.Structure.class) {
            for (java.lang.reflect.Field f : clazz.getDeclaredFields()) {
                if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
                String fn = f.getName();
                if ("name".equals(fn) || "encodeFn".equals(fn) || "decodeFn".equals(fn)
                    || "codecEnabled".equals(fn) || "formatter".equals(fn)) continue;
                try {
                    java.lang.reflect.Field df;
                    try { df = dst.getClass().getField(fn); } catch (NoSuchFieldException e) { continue; }
                    df.set(dst, f.get(src));
                } catch (Exception ignored) {}
            }
            clazz = clazz.getSuperclass();
        }
    }

    /** Create a CmsData wrapping an ARRAY of elements. */
    public static CmsData array(CmsData... elems) {
        CmsData d = new CmsData();
        d.choice().value(CmsDataType.ARRAY);
        d.value.array.append(elems);
        return d;
    }

    /** Create a CmsData wrapping a STRUCTURE of elements. */
    public static CmsData structure(CmsData... elems) {
        CmsData d = new CmsData();
        d.choice().value(CmsDataType.STRUCTURE);
        d.value.structure.append(elems);
        return d;
    }

    /** Read back elements from an ARRAY or STRUCTURE after decode.
     *  Works reliably for simple-type elements; nested ARRAY/STRUCTURE
     *  elements may have empty union fields due to JNA Union limitations. */
    public CmsData[] elements() {
        int c = choice().value();
        if (c != CmsDataType.ARRAY && c != CmsDataType.STRUCTURE)
            throw new IllegalStateException("not an array/structure (choice=" + c + ")");
        int count = (c == CmsDataType.ARRAY) ? value.array.count : value.structure.count;
        Pointer ptr = (c == CmsDataType.ARRAY) ? value.array.elements : value.structure.elements;
        if (ptr == null || count == 0) return new CmsData[0];
        int elemSize = size();
        CmsData[] result = new CmsData[count];
        for (int i = 0; i < count; i++) {
            result[i] = decodeElementAt(ptr, i, elemSize);
        }
        return result;
    }

    /** Decode one sub-element from a raw struct buffer. */
    private static CmsData decodeElementAt(Pointer base, int index, int elemSize) {
        // Copy raw struct bytes to a fresh allocation
        byte[] raw = base.getByteArray((long) index * elemSize, elemSize);
        int ec = (raw[0] & 0xFF) | ((raw[1] & 0xFF) << 8)
               | ((raw[2] & 0xFF) << 16) | ((raw[3] & 0xFF) << 24);
        CmsData e = new CmsData();
        e.getPointer().write(0, raw, 0, raw.length);
        e.read();
        e.value.setType(unionClass(ec));
        e.getPointer().write(0, raw, 0, raw.length);
        e.read();
        return e;
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("choice", "value");
    }

    @Override
    protected int encodeBufSize() { return 4096; }

    @Override
    public byte[] encode() {
        value.setType(unionClass(choice().value()));
        return super.encode();
    }

    @Override
    public CmsData decode(byte[] data) {
        super.decode(data);
        value.setType(unionClass(choice().value()));
        value.read();
        return this;
    }
}
