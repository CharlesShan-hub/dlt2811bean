package com.ysh.jcms.datatype.choice;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.*;
import com.ysh.jcms.datatype.common.*;
import com.ysh.jcms.datatype.control.CmsCheck;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import com.ysh.jcms.datatype.extended.CmsUtcTime;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.jcms.util.CmsDefaultFormatter;
import java.util.Objects;
import static com.ysh.jcms.datatype.choice.CmsDataType.*;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsData extends CmsType {
    /** Choice discriminator as a CmsType. */
    public CmsDataType.ByValue choice = new CmsDataType.ByValue();
    public CmsDataUnion value = new CmsDataUnion();

    public CmsData() {
        formatter = CmsDataFormatter.INSTANCE;
    }

    // ==================== Choice 映射表 ====================
    // 以下为 CmsDataUnion 字段与 CmsDataType choice 常量间的映射。
    // choiceFor() / unionClass() 定义在 CmsDataType 中（通过 static import 访问）。
    // =========================================================

    /** choice → 当前活跃的 Union 值。 */
    private Object activeValue(int c) {
        switch (c) {
            case ERROR:        return value.error;
            case BOOLEAN:      return value.boolean_value;
            case INT8:         return value.int8;
            case INT16:        return value.int16;
            case INT32:        return value.int32;
            case INT64:        return value.int64;
            case INT8U:        return value.int8u;
            case INT16U:       return value.int16u;
            case INT32U:       return value.int32u;
            case INT64U:       return value.int64u;
            case FLOAT32:      return value.float32;
            case FLOAT64:      return value.float64;
            case UTC_TIME:     return value.utc_time;
            case BINARY_TIME:  return value.binary_time;
            case QUALITY:      return value.quality;
            case DBPOS:        return value.dbpos;
            case TCMD:         return value.tcmd;
            case CHECK:        return value.check;
            case BIT_STRING:
            case OCTET_STRING:
            case VISIBLE_STRING:
            case UTF8_STRING:
                try { return value.visible_string.value(); } catch (Exception e) { return value.visible_string; }
            case ARRAY:        return value.array;
            case STRUCTURE:    return value.structure;
            default:           return value.int32;
        }
    }

    /** choice → 标量值赋予 Union 字段。 */
    @SuppressWarnings("unchecked")
    private static void assign(CmsDataUnion u, int c, Object val) {
        switch (c) {
            case ERROR:        u.error = new CmsServiceError.ByValue();   u.error.value = cvt(val); break;
            case BOOLEAN:      u.boolean_value = new CmsBoolean.ByValue(); u.boolean_value.value = cvt(val); break;
            case INT8:         u.int8 = new CmsInt8.ByValue();            u.int8.value = cvt(val); break;
            case INT16:        u.int16 = new CmsInt16.ByValue();          u.int16.value = cvt(val); break;
            case INT32:        u.int32 = new CmsInt32.ByValue();          u.int32.value = cvt(val); break;
            case INT64:        u.int64 = new CmsInt64.ByValue();          u.int64.value = cvt(val); break;
            case INT8U:        u.int8u = new CmsInt8U.ByValue();          u.int8u.value = cvt(val); break;
            case INT16U:       u.int16u = new CmsInt16U.ByValue();        u.int16u.value = cvt(val); break;
            case INT32U:       u.int32u = new CmsInt32U.ByValue();        u.int32u.value = cvt(val); break;
            case INT64U:       u.int64u = new CmsInt64U.ByValue();        u.int64u.value = cvt(val); break;
            case FLOAT32:      u.float32 = new CmsFloat32.ByValue();      u.float32.value = cvt(val); break;
            case FLOAT64:      u.float64 = new CmsFloat64.ByValue();      u.float64.value = cvt(val); break;
            case DBPOS:        u.dbpos = new CmsDbpos.ByValue();          u.dbpos.value = cvt(val); break;
            case TCMD:         u.tcmd = new CmsTcmd.ByValue();            u.tcmd.value = cvt(val); break;
            case UTC_TIME:     u.utc_time = (CmsUtcTime.ByValue) val; break;
            case BINARY_TIME:  u.binary_time = (CmsBinaryTime.ByValue) val; break;
            case QUALITY:      u.quality = (CmsQuality.ByValue) val; break;
            case CHECK:        u.check = (CmsCheck.ByValue) val; break;
            case BIT_STRING:
            case OCTET_STRING:
            case VISIBLE_STRING:
            case UTF8_STRING:
                u.visible_string = val instanceof byte[]
                    ? new CmsUint8Array.ByValue().value((byte[]) val)
                    : (CmsUint8Array.ByValue) val;
                break;
            case ARRAY:        u.array = (CmsDataArray.ByValue) val; break;
            case STRUCTURE:    u.structure = (CmsDataStructure.ByValue) val; break;
            default: throw new IllegalArgumentException("unsupported choice " + c);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T cvt(Object v) { return (T) v; }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CmsData)) return false;
        CmsData other = (CmsData) o;
        int c = choice.value();
        if (c != other.choice.value()) return false;
        return compareByChoice(c, other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(choice.value());
    }

    /** choice → 相等比较。 */
    private boolean compareByChoice(int c, CmsData other) {
        switch (c) {
            case ERROR:  return Objects.equals(value.error, other.value.error);
            case BOOLEAN: return Objects.equals(value.boolean_value, other.value.boolean_value);
            case INT8:   return Objects.equals(value.int8, other.value.int8);
            case INT16:  return Objects.equals(value.int16, other.value.int16);
            case INT32:  return Objects.equals(value.int32, other.value.int32);
            case INT64:  return Objects.equals(value.int64, other.value.int64);
            case INT8U:  return Objects.equals(value.int8u, other.value.int8u);
            case INT16U: return Objects.equals(value.int16u, other.value.int16u);
            case INT32U: return Objects.equals(value.int32u, other.value.int32u);
            case INT64U: return Objects.equals(value.int64u, other.value.int64u);
            case FLOAT32: return Objects.equals(value.float32, other.value.float32);
            case FLOAT64: return Objects.equals(value.float64, other.value.float64);
            case BIT_STRING:
            case OCTET_STRING:
            case VISIBLE_STRING:
            case UTF8_STRING:
                return Arrays.equals(value.visible_string.value(), other.value.visible_string.value());
            case UTC_TIME:    return Objects.equals(value.utc_time, other.value.utc_time);
            case BINARY_TIME: return Objects.equals(value.binary_time, other.value.binary_time);
            case QUALITY:     return Objects.equals(value.quality, other.value.quality);
            case DBPOS:       return Objects.equals(value.dbpos, other.value.dbpos);
            case TCMD:        return Objects.equals(value.tcmd, other.value.tcmd);
            case CHECK:       return Objects.equals(value.check, other.value.check);
            case ARRAY:
                if (value.array.count != other.value.array.count) return false;
                return Objects.equals(value.array.elements, other.value.array.elements);
            case STRUCTURE:
                if (value.structure.count != other.value.structure.count) return false;
                return Objects.equals(value.structure.elements, other.value.structure.elements);
            default: return false;
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
        assign(d.value, c, bv);
        return d;
    }

    /** Create a CmsData with a scalar value: (INT32, 42), (BOOLEAN, true), (DBPOS, 1) */
    public static CmsData of(int c, Object val) {
        CmsData d = new CmsData();
        d.choice().value(c);
        d.value.setType(unionClass(c));
        assign(d.value, c, val);
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

    /**
     * CmsData 专用格式化器 — 根据 choice 只展开活跃值，不显示所有 union 字段。
     */
    private static class CmsDataFormatter implements com.ysh.jcms.util.CmsToString {
        static final CmsDataFormatter INSTANCE = new CmsDataFormatter();

        @Override
        public String toString(com.ysh.jcms.ffi.CmsType obj, int indent) {
            CmsData d = (CmsData) obj;
            String pad = indent > 0 ? CmsDefaultFormatter.repeat("    ", indent) : "";
            int c = d.choice.value();
            Object active = d.activeValue(c);
            String valStr = (active instanceof com.ysh.jcms.ffi.CmsType)
                ? CmsDefaultFormatter.formatValue(active, indent + 1)
                : String.valueOf(active);
            return "(" + d.getClass().getSimpleName() + ") {\n"
                + pad + "    choice: (" + com.ysh.jcms.datatype.choice.CmsDataType.class.getSimpleName() + ") " + c + "\n"
                + pad + "    value: (CmsDataUnion) -> " + valStr + "\n"
                + pad + "}";
        }
    }
}
