package com.ysh.jcms.datatype.choice;

import com.sun.jna.Pointer;
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
    // ==================== Factory: by type ====================

    /** Create a CmsData with a pre-built value.
     *  The choice discriminator is auto-detected from the value's class. */
    public static CmsData of(CmsServiceError val) { CmsServiceError.ByValue bv = new CmsServiceError.ByValue(); bv.value = val.value; return of(ERROR, bv); }
    public static CmsData of(CmsBoolean val)      { CmsBoolean.ByValue bv = new CmsBoolean.ByValue(); bv.value = val.value; return of(BOOLEAN, bv); }
    public static CmsData of(CmsInt8 val)         { CmsInt8.ByValue bv = new CmsInt8.ByValue(); bv.value = val.value; return of(INT8, bv); }
    public static CmsData of(CmsInt16 val)        { CmsInt16.ByValue bv = new CmsInt16.ByValue(); bv.value = val.value; return of(INT16, bv); }
    public static CmsData of(CmsInt32 val)        { CmsInt32.ByValue bv = new CmsInt32.ByValue(); bv.value = val.value; return of(INT32, bv); }
    public static CmsData of(CmsInt64 val)        { CmsInt64.ByValue bv = new CmsInt64.ByValue(); bv.value = val.value; return of(INT64, bv); }
    public static CmsData of(CmsInt8U val)        { CmsInt8U.ByValue bv = new CmsInt8U.ByValue(); bv.value = val.value; return of(INT8U, bv); }
    public static CmsData of(CmsInt16U val)       { CmsInt16U.ByValue bv = new CmsInt16U.ByValue(); bv.value = val.value; return of(INT16U, bv); }
    public static CmsData of(CmsInt32U val)       { CmsInt32U.ByValue bv = new CmsInt32U.ByValue(); bv.value = val.value; return of(INT32U, bv); }
    public static CmsData of(CmsInt64U val)       { CmsInt64U.ByValue bv = new CmsInt64U.ByValue(); bv.value = val.value; return of(INT64U, bv); }
    public static CmsData of(CmsFloat32 val)      { CmsFloat32.ByValue bv = new CmsFloat32.ByValue(); bv.value = val.value; return of(FLOAT32, bv); }
    public static CmsData of(CmsFloat64 val)      { CmsFloat64.ByValue bv = new CmsFloat64.ByValue(); bv.value = val.value; return of(FLOAT64, bv); }
    public static CmsData of(CmsDbpos val)        { CmsDbpos.ByValue bv = new CmsDbpos.ByValue(); bv.value = val.value; return of(DBPOS, bv); }
    public static CmsData of(CmsTcmd val)         { CmsTcmd.ByValue bv = new CmsTcmd.ByValue(); bv.value = val.value; return of(TCMD, bv); }
    public static CmsData of(CmsQuality val)      { CmsQuality.ByValue bv = new CmsQuality.ByValue(); bv.validity = val.validity; bv.overflow = val.overflow; bv.outOfRange = val.outOfRange; bv.badReference = val.badReference; bv.oscillatory = val.oscillatory; bv.failure = val.failure; bv.oldData = val.oldData; bv.inconsistent = val.inconsistent; bv.inaccurate = val.inaccurate; bv.substituted = val.substituted; bv.test = val.test; bv.operatorBlocked = val.operatorBlocked; return of(QUALITY, bv); }
    public static CmsData of(CmsCheck val)        { CmsCheck.ByValue bv = new CmsCheck.ByValue(); bv.syncheck.value = val.syncheck.value; bv.interlock_check.value = val.interlock_check.value; return of(CHECK, bv); }
    public static CmsData of(CmsUtcTime val)      { CmsUtcTime.ByValue bv = new CmsUtcTime.ByValue(); bv.seconds_since_epoch.value = val.seconds_since_epoch.value; bv.fraction_of_second.value = val.fraction_of_second.value; bv.time_quality.leap_seconds_known.value = val.time_quality.leap_seconds_known.value; bv.time_quality.clock_failure.value = val.time_quality.clock_failure.value; bv.time_quality.clock_not_synchronized.value = val.time_quality.clock_not_synchronized.value; bv.time_quality.precision.value = val.time_quality.precision.value; return of(UTC_TIME, bv); }
    public static CmsData of(CmsBinaryTime val)   { CmsBinaryTime.ByValue bv = new CmsBinaryTime.ByValue(); bv.msOfDay.value = val.msOfDay.value; bv.daysSince1984.value = val.daysSince1984.value; return of(BINARY_TIME, bv); }
    public static CmsData of(CmsUint8Array val)   { return of(VISIBLE_STRING, val); }
    public static CmsData of(CmsDataArray.ByValue val)     { return of(ARRAY, val); }
    public static CmsData of(CmsDataStructure.ByValue val) { return of(STRUCTURE, val); }

    // ==================== Factory: simple values ====================

    /** Create a CmsData with a scalar value.
     *  scalars: (INT32, 42), (BOOLEAN, true), (DBPOS, 1) */
    public static CmsData of(int c, Object val) {
        CmsData d = new CmsData();
        d.choice().value(c);
        d.value.setType(unionClass(c));
        assign(d.value, c, val);
        return d;
    }

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
    private static <T> T cvt(Object v) {
        return (T) v;
    }

    // ==================== Factory: array / structure ====================

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

    private static Class<?> unionClass(int c) {
        switch (c) {
            case 0:  return CmsServiceError.ByValue.class;
            case 1:  return CmsDataArray.ByValue.class;
            case 2:  return CmsDataStructure.ByValue.class;
            case 3:  return CmsBoolean.ByValue.class;
            case 4:  return CmsInt8.ByValue.class;
            case 5:  return CmsInt16.ByValue.class;
            case 6:  return CmsInt32.ByValue.class;
            case 7:  return CmsInt64.ByValue.class;
            case 8:  return CmsInt8U.ByValue.class;
            case 9:  return CmsInt16U.ByValue.class;
            case 10: return CmsInt32U.ByValue.class;
            case 11: return CmsInt64U.ByValue.class;
            case 12: return CmsFloat32.ByValue.class;
            case 13: return CmsFloat64.ByValue.class;
            case 14: return CmsUint8Array.ByValue.class;
            case 15: return CmsUint8Array.ByValue.class;
            case 16: return CmsUint8Array.ByValue.class;
            case 17: return CmsUint8Array.ByValue.class;
            case 18: return CmsUtcTime.ByValue.class;
            case 19: return CmsBinaryTime.ByValue.class;
            case 20: return CmsQuality.ByValue.class;
            case 21: return CmsDbpos.ByValue.class;
            case 22: return CmsTcmd.ByValue.class;
            case 23: return CmsCheck.ByValue.class;
            default: return CmsInt32.ByValue.class;
        }
    }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CmsData)) return false;
        CmsData other = (CmsData) o;
        int c = choice().value();
        if (c != other.choice().value()) return false;
        return compareByChoice(c, other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(choice().value());
    }

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
                // Compare array elements by address (shallow)
                return Objects.equals(value.array.elements, other.value.array.elements);
            case STRUCTURE:
                if (value.structure.count != other.value.structure.count) return false;
                return Objects.equals(value.structure.elements, other.value.structure.elements);
            default: return false;
        }
    }

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
