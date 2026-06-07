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

@Getter
@Setter
@Accessors(fluent = true)
public class CmsData extends CmsType {
    /** Choice discriminator as a CmsType. */
    public CmsDataType.ByValue choice = new CmsDataType.ByValue();
    public CmsDataUnion value = new CmsDataUnion();
    /** Backing native memory for array/structure to prevent GC. */
    private transient Memory _elementsMemory;

    public CmsData() {
        super();
    }

    // ==================== Factory: simple values ====================

    /** Create a CmsData with a scalar value. */
    public static CmsData of(int c, Object val) {
        CmsData d = new CmsData();
        d.choice().value(c);
        d.value.setType(unionClass(c));
        if (c == CmsDataType.ERROR)  d.value.error.value((Integer) val);
        else if (c == CmsDataType.BOOLEAN) d.value.boolean_value.value((Boolean) val);
        else if (c == CmsDataType.INT8)   d.value.int8.value((Byte) val);
        else if (c == CmsDataType.INT16)  d.value.int16.value((Short) val);
        else if (c == CmsDataType.INT32)  d.value.int32.value((Integer) val);
        else if (c == CmsDataType.INT64)  d.value.int64.value((Long) val);
        else if (c == CmsDataType.INT8U)  d.value.int8u.value((Byte) val);
        else if (c == CmsDataType.INT16U) d.value.int16u.value((Short) val);
        else if (c == CmsDataType.INT32U) d.value.int32u.value((Integer) val);
        else if (c == CmsDataType.FLOAT32) d.value.float32.value((Float) val);
        else if (c == CmsDataType.FLOAT64) d.value.float64.value((Double) val);
        else if (c == CmsDataType.DBPOS)  d.value.dbpos.value((Integer) val);
        else if (c == CmsDataType.TCMD)   d.value.tcmd.value((Integer) val);
        else throw new IllegalArgumentException("unsupported scalar choice: " + c);
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

    /** Read back elements from an ARRAY or STRUCTURE after decode. */
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
            CmsData e = new CmsData();
            e.useMemory(ptr.share((long) i * elemSize));
            e.read();
            e.value.setType(unionClass(e.choice().value()));
            e.value.read();
            result[i] = e;
        }
        return result;
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

    public static class ByValue extends CmsData implements Structure.ByValue {}
}
