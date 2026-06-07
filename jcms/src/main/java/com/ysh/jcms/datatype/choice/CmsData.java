package com.ysh.jcms.datatype.choice;

import com.sun.jna.Memory;
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
import java.util.Objects;
import static com.ysh.jcms.datatype.choice.CmsDataType.*;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsData extends CmsType {
    /** Choice discriminator as a CmsType. */
    public CmsDataType.ByValue choice = new CmsDataType.ByValue();
    public CmsDataUnion value = new CmsDataUnion();
    /** Backing native memory for array/structure to prevent GC. */
    private transient Memory _elementsMemory;

    // ==================== Factory: by type ====================

    /** Create a CmsData with a pre-built CmsType value.
     *  The choice discriminator is auto-detected from the value's class. */
    public static CmsData of(CmsServiceError val) { return of(ERROR, val); }
    public static CmsData of(CmsBoolean val)      { return of(BOOLEAN, val); }
    public static CmsData of(CmsInt8 val)          { return of(INT8, val); }
    public static CmsData of(CmsInt16 val)         { return of(INT16, val); }
    public static CmsData of(CmsInt32 val)         { return of(INT32, val); }
    public static CmsData of(CmsInt64 val)         { return of(INT64, val); }
    public static CmsData of(CmsInt8U val)         { return of(INT8U, val); }
    public static CmsData of(CmsInt16U val)        { return of(INT16U, val); }
    public static CmsData of(CmsInt32U val)        { return of(INT32U, val); }
    public static CmsData of(CmsFloat32 val)       { return of(FLOAT32, val); }
    public static CmsData of(CmsFloat64 val)       { return of(FLOAT64, val); }
    public static CmsData of(CmsDbpos val)         { return of(DBPOS, val); }
    public static CmsData of(CmsTcmd val)          { return of(TCMD, val); }
    public static CmsData of(CmsQuality val)       { return of(QUALITY, val); }
    public static CmsData of(CmsCheck val)         { return of(CHECK, val); }
    public static CmsData of(CmsUtcTime val)       { return of(UTC_TIME, val); }
    public static CmsData of(CmsBinaryTime val)    { return of(BINARY_TIME, val); }

    // ==================== Factory: simple values ====================

    /** Create a CmsData with a scalar value.
     *  scalars: (INT32, 42), (BOOLEAN, true), (DBPOS, 1) */
    public static CmsData of(int c, Object val) {
        CmsData d = new CmsData();
        d.choice().value(c);
        d.value.setType(unionClass(c));
        if (c == CmsDataType.ERROR) {
            if (val instanceof CmsServiceError) d.value.error = (CmsServiceError) val;
            else d.value.error.value((Integer) val);
        } else if (c == CmsDataType.BOOLEAN) {
            if (val instanceof CmsBoolean) d.value.boolean_value = (CmsBoolean) val;
            else d.value.boolean_value.value((Boolean) val);
        } else if (c == CmsDataType.INT8) {
            if (val instanceof CmsInt8) d.value.int8 = (CmsInt8) val;
            else d.value.int8.value((Byte) val);
        } else if (c == CmsDataType.INT16) {
            if (val instanceof CmsInt16) d.value.int16 = (CmsInt16) val;
            else d.value.int16.value((Short) val);
        } else if (c == CmsDataType.INT32) {
            if (val instanceof CmsInt32) d.value.int32 = (CmsInt32) val;
            else d.value.int32.value((Integer) val);
        } else if (c == CmsDataType.INT64) {
            if (val instanceof CmsInt64) d.value.int64 = (CmsInt64) val;
            else d.value.int64.value((Long) val);
        } else if (c == CmsDataType.INT8U) {
            if (val instanceof CmsInt8U) d.value.int8u = (CmsInt8U) val;
            else d.value.int8u.value((Byte) val);
        } else if (c == CmsDataType.INT16U) {
            if (val instanceof CmsInt16U) d.value.int16u = (CmsInt16U) val;
            else d.value.int16u.value((Short) val);
        } else if (c == CmsDataType.INT32U) {
            if (val instanceof CmsInt32U) d.value.int32u = (CmsInt32U) val;
            else d.value.int32u.value((Integer) val);
        } else if (c == CmsDataType.FLOAT32) {
            if (val instanceof CmsFloat32) d.value.float32 = (CmsFloat32) val;
            else d.value.float32.value((Float) val);
        } else if (c == CmsDataType.FLOAT64) {
            if (val instanceof CmsFloat64) d.value.float64 = (CmsFloat64) val;
            else d.value.float64.value((Double) val);
        } else if (c == CmsDataType.DBPOS) {
            if (val instanceof CmsDbpos) d.value.dbpos = (CmsDbpos) val;
            else d.value.dbpos.value((Integer) val);
        } else if (c == CmsDataType.TCMD) {
            if (val instanceof CmsTcmd) d.value.tcmd = (CmsTcmd) val;
            else d.value.tcmd.value((Integer) val);
        } else if (val instanceof CmsQuality) d.value.quality = (CmsQuality) val;
        else if (val instanceof CmsCheck) d.value.check = (CmsCheck) val;
        else if (val instanceof CmsUtcTime) d.value.utc_time = (CmsUtcTime) val;
        else if (val instanceof CmsBinaryTime) d.value.binary_time = (CmsBinaryTime) val;
        else throw new IllegalArgumentException("unsupported value type for choice " + c);
        return d;
    }

    // ==================== Factory: array / structure ====================

    /** Create a CmsData wrapping an ARRAY of elements. */
    public static CmsData array(CmsData... elems) {
        if (elems == null || elems.length == 0)
            throw new IllegalArgumentException("array must have at least 1 element");
        CmsData d = new CmsData();
        d.choice().value(CmsDataType.ARRAY);
        int elemSize = elems[0].size();
        d._elementsMemory = new Memory((long) elemSize * elems.length);
        for (int i = 0; i < elems.length; i++) {
            elems[i].write();
            byte[] raw = elems[i].getPointer().getByteArray(0, elemSize);
            d._elementsMemory.write((long) i * elemSize, raw, 0, elemSize);
        }
        d.value.array.elements = d._elementsMemory;
        d.value.array.count = elems.length;
        return d;
    }

    /** Create a CmsData wrapping a STRUCTURE of elements. */
    public static CmsData structure(CmsData... elems) {
        if (elems == null || elems.length == 0)
            throw new IllegalArgumentException("structure must have at least 1 element");
        CmsData d = new CmsData();
        d.choice().value(CmsDataType.STRUCTURE);
        int elemSize = elems[0].size();
        d._elementsMemory = new Memory((long) elemSize * elems.length);
        for (int i = 0; i < elems.length; i++) {
            elems[i].write();
            byte[] raw = elems[i].getPointer().getByteArray(0, elemSize);
            d._elementsMemory.write((long) i * elemSize, raw, 0, elemSize);
        }
        d.value.structure.elements = d._elementsMemory;
        d.value.structure.count = elems.length;
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
            case 0:  return CmsServiceError.class;
            case 1:  return CmsDataArray.ByValue.class;
            case 2:  return CmsDataStructure.ByValue.class;
            case 3:  return CmsBoolean.class;
            case 4:  return CmsInt8.class;
            case 5:  return CmsInt16.class;
            case 6:  return CmsInt32.class;
            case 7:  return CmsInt64.class;
            case 8:  return CmsInt8U.class;
            case 9:  return CmsInt16U.class;
            case 10: return CmsInt32U.class;
            case 11: return CmsInt64U.class;
            case 12: return CmsFloat32.class;
            case 13: return CmsFloat64.class;
            case 14: return CmsUint8Array.class;
            case 15: return CmsUint8Array.class;
            case 16: return CmsUint8Array.class;
            case 17: return CmsUint8Array.class;
            case 18: return CmsUtcTime.class;
            case 19: return CmsBinaryTime.class;
            case 20: return CmsQuality.class;
            case 21: return CmsDbpos.class;
            case 22: return CmsTcmd.class;
            case 23: return CmsCheck.class;
            default: return CmsInt32.class;
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

    public static class ByValue extends CmsData implements Structure.ByValue {}
}
