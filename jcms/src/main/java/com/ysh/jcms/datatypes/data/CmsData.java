package com.ysh.jcms.datatypes.data;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.compound.CmsBinaryTime;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CmsData — tagged union for Data CHOICE (24 alternatives).
 *
 * <p>Check {@link #choice} before accessing the corresponding value field.
 * Same convention as C: you read {@code data->choice} then {@code data->value.int32}.
 *
 * <p>Thread-safety: not guaranteed.
 */
@Getter
@Accessors(fluent = true)
public class CmsData extends AbstractCmsDataUnit<CmsData> {

    // ==================== Native struct/union mapping ====================

    /** Sub-struct for BIT_STRING. */
    public static class BitStringStruct extends Structure {
        public Pointer data;       // uint8_t*
        public int nbits;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("data", "nbits");
        }
    }

    /** Sub-struct for OCTET_STRING. */
    public static class OctetStringStruct extends Structure {
        public Pointer data;       // uint8_t*
        public int len;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("data", "len");
        }
    }

    /** The 24-alternative union. */
    public static class CmsDataUnion extends Union {
        public int error;                       // 0
        public CmsArray.ArrayStruct array;      // 1
        public CmsArray.ArrayStruct structure;  // 2
        public int boolean_value;               // 3
        public byte int8;                       // 4
        public short int16;                     // 5
        public int int32;                       // 6
        public long int64;                      // 7
        public byte int8u;                      // 8
        public short int16u;                    // 9
        public int int32u;                      // 10
        public long int64u;                     // 11
        public float float32;                   // 12
        public double float64;                  // 13
        public BitStringStruct bit_string;      // 14
        public OctetStringStruct octet_string;  // 15
        public Pointer visible_string;          // 16
        public Pointer utf8_string;             // 17
        public long utc_time_ms;                // 18
        public CmsBinaryTime.NativeStruct binary_time;  // 19
        public byte[] quality = new byte[2];    // 20
        public int dbpos;                       // 21
        public int tcmd;                        // 22
        public byte[] check = new byte[2];      // 23        
        public CmsDataUnion() {
            array = new CmsArray.ArrayStruct();
            structure = new CmsArray.ArrayStruct();
            bit_string = new BitStringStruct();
            octet_string = new OctetStringStruct();
            binary_time = new CmsBinaryTime.NativeStruct();
        }
    }

    /** Top-level native struct: cms_data_t. */
    public static class NativeStruct extends Structure {
        public int choice;
        public CmsDataUnion value;

        public NativeStruct() {
            value = new CmsDataUnion();
        }

        public NativeStruct(Pointer p) {
            super(p);
            value = new CmsDataUnion(); // JNA auto-calculates field offset during read()
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("choice", "value");
        }
    }

    // ===================== Java-level fields =====================

    /** The JNA native struct backing this Data value. */
    public NativeStruct nativeStruct;

    // array / structure
    public CmsArray arrayVal;

    // scalar types
    public boolean boolVal;
    public long intVal;          // int8~int64, int8u~int64u
    public double floatVal;      // float32, float64
    public String strVal;        // visible_string, utf8_string
    public byte[] bytesVal;      // bit_string data, octet_string data
    public int nbits;            // valid for BIT_STRING only
    public long utcTimeMs;       // UTC_TIME
    public CmsBinaryTime binaryTimeVal; // BINARY_TIME
    public byte[] quality;       // QUALITY (2 bytes)
    public int dbpos;            // DBPOS
    public int tcmd;             // TCMD
    public byte[] check;         // CHECK (2 bytes)

    // ===================== Construction =====================

    public CmsData() {
        super("Data");
        nativeStruct = new NativeStruct();
    }

    // ---- factory methods ----

    public static CmsData createError(int errorCode) {
        CmsData d = new CmsData();
        d.choice = ERROR;
        d.serviceError = errorCode;
        return d;
    }

    public static CmsData createArray(CmsArray array) {
        CmsData d = new CmsData();
        d.choice = ARRAY;
        d.arrayVal = array;
        return d;
    }

    public static CmsData createStructure(CmsArray members) {
        CmsData d = new CmsData();
        d.choice = STRUCTURE;
        d.arrayVal = members;
        return d;
    }

    public static CmsData createBoolean(boolean value) {
        CmsData d = new CmsData();
        d.choice = BOOLEAN;
        d.boolVal = value;
        return d;
    }

    public static CmsData createInt8(byte value) {
        CmsData d = new CmsData();
        d.choice = INT8;
        d.intVal = value;
        return d;
    }

    public static CmsData createInt16(short value) {
        CmsData d = new CmsData();
        d.choice = INT16;
        d.intVal = value;
        return d;
    }

    public static CmsData createInt32(int value) {
        CmsData d = new CmsData();
        d.choice = INT32;
        d.intVal = value;
        return d;
    }

    public static CmsData createInt64(long value) {
        CmsData d = new CmsData();
        d.choice = INT64;
        d.intVal = value;
        return d;
    }

    public static CmsData createInt8U(short value) {
        CmsData d = new CmsData();
        d.choice = INT8U;
        d.intVal = value & 0xFF;
        return d;
    }

    public static CmsData createInt16U(int value) {
        CmsData d = new CmsData();
        d.choice = INT16U;
        d.intVal = value & 0xFFFF;
        return d;
    }

    public static CmsData createInt32U(long value) {
        CmsData d = new CmsData();
        d.choice = INT32U;
        d.intVal = value & 0xFFFFFFFFL;
        return d;
    }

    public static CmsData createInt64U(long value) {
        CmsData d = new CmsData();
        d.choice = INT64U;
        d.intVal = value;
        return d;
    }

    public static CmsData createFloat32(float value) {
        CmsData d = new CmsData();
        d.choice = FLOAT32;
        d.floatVal = value;
        return d;
    }

    public static CmsData createFloat64(double value) {
        CmsData d = new CmsData();
        d.choice = FLOAT64;
        d.floatVal = value;
        return d;
    }

    public static CmsData createBitString(byte[] data, int nbits) {
        CmsData d = new CmsData();
        d.choice = BIT_STRING;
        d.bytesVal = data != null ? data.clone() : new byte[0];
        d.nbits = nbits;
        return d;
    }

    public static CmsData createOctetString(byte[] data) {
        CmsData d = new CmsData();
        d.choice = OCTET_STRING;
        d.bytesVal = data != null ? data.clone() : new byte[0];
        return d;
    }

    public static CmsData createVisibleString(String value) {
        CmsData d = new CmsData();
        d.choice = VISIBLE_STRING;
        d.strVal = value;
        return d;
    }

    public static CmsData createUtf8String(String value) {
        CmsData d = new CmsData();
        d.choice = UTF8_STRING;
        d.strVal = value;
        return d;
    }

    public static CmsData createUtcTime(long millis) {
        CmsData d = new CmsData();
        d.choice = UTC_TIME;
        d.utcTimeMs = millis;
        return d;
    }

    public static CmsData createBinaryTime(CmsBinaryTime value) {
        CmsData d = new CmsData();
        d.choice = BINARY_TIME;
        d.binaryTimeVal = value;
        return d;
    }

    public static CmsData createQuality(byte[] value) {
        CmsData d = new CmsData();
        d.choice = QUALITY;
        d.quality = value != null ? value.clone() : new byte[2];
        return d;
    }

    public static CmsData createDbpos(int value) {
        CmsData d = new CmsData();
        d.choice = DBPOS;
        d.dbpos = value;
        return d;
    }

    public static CmsData createTcmd(int value) {
        CmsData d = new CmsData();
        d.choice = TCMD;
        d.tcmd = value;
        return d;
    }

    public static CmsData createCheck(byte[] value) {
        CmsData d = new CmsData();
        d.choice = CHECK;
        d.check = value != null ? value.clone() : new byte[2];
        return d;
    }

    // ===================== Encode =====================

    @Override
    public byte[] encode() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        List<Pointer> allocs = new ArrayList<>(); // track Memory allocations for cleanup

        try {
            syncToNative(ns, allocs);
            ns.write();

            byte[] buf = new byte[65536];
            IntByReference outLen = new IntByReference(buf.length);
            CmsFFIDatatypes.INSTANCE.cms_data_encode(ns, buf, outLen);
            return Arrays.copyOf(buf, outLen.getValue());
        } finally {
            for (Pointer p : allocs) {
                // Memory will be GC'd anyway; just clear the reference
            }
        }
    }

    private void syncToNative(NativeStruct ns, List<Pointer> allocs) {
        ns.choice = choice;

        switch (choice) {
            case ERROR:
                ns.value.setType("error");
                ns.value.error = serviceError;
                break;

            case ARRAY:
                ns.value.setType("array");
                syncChildArray(ns.value.array, allocs);
                break;

            case STRUCTURE:
                ns.value.setType("structure");
                syncChildArray(ns.value.structure, allocs);
                break;

            case BOOLEAN:
                ns.value.setType("boolean_value");
                ns.value.boolean_value = boolVal ? 1 : 0;
                break;

            case INT8:
                ns.value.setType("int8");
                ns.value.int8 = (byte) intVal;
                break;

            case INT16:
                ns.value.setType("int16");
                ns.value.int16 = (short) intVal;
                break;

            case INT32:
                ns.value.setType("int32");
                ns.value.int32 = (int) intVal;
                break;

            case INT64:
                ns.value.setType("int64");
                ns.value.int64 = intVal;
                break;

            case INT8U:
                ns.value.setType("int8u");
                ns.value.int8u = (byte) intVal;
                break;

            case INT16U:
                ns.value.setType("int16u");
                ns.value.int16u = (short) intVal;
                break;

            case INT32U:
                ns.value.setType("int32u");
                ns.value.int32u = (int) intVal;
                break;

            case INT64U:
                ns.value.setType("int64u");
                ns.value.int64u = intVal;
                break;

            case FLOAT32:
                ns.value.setType("float32");
                ns.value.float32 = (float) floatVal;
                break;

            case FLOAT64:
                ns.value.setType("float64");
                ns.value.float64 = floatVal;
                break;

            case BIT_STRING: {
                ns.value.setType("bit_string");
                byte[] data = bytesVal != null ? bytesVal : new byte[0];
                Memory mem = new Memory(data.length);
                mem.write(0, data, 0, data.length);
                allocs.add(mem);
                ns.value.bit_string.data = mem;
                ns.value.bit_string.nbits = nbits;
                break;
            }

            case OCTET_STRING: {
                ns.value.setType("octet_string");
                byte[] data = bytesVal != null ? bytesVal : new byte[0];
                Memory mem = new Memory(data.length);
                mem.write(0, data, 0, data.length);
                allocs.add(mem);
                ns.value.octet_string.data = mem;
                ns.value.octet_string.len = data.length;
                break;
            }

            case VISIBLE_STRING:
                ns.value.setType("visible_string");
                ns.value.visible_string = stringToPointer(strVal, allocs);
                break;

            case UTF8_STRING:
                ns.value.setType("utf8_string");
                ns.value.utf8_string = stringToPointer(strVal, allocs);
                break;

            case UTC_TIME:
                ns.value.setType("utc_time_ms");
                ns.value.utc_time_ms = utcTimeMs;
                break;

            case BINARY_TIME:
                ns.value.setType("binary_time");
                if (binaryTimeVal != null) {
                    ns.value.binary_time.msOfDay = binaryTimeVal.msOfDay;
                    ns.value.binary_time.daysSince1984 = (short) binaryTimeVal.daysSince1984;
                }
                break;

            case QUALITY:
                ns.value.setType("quality");
                ns.value.quality = quality != null ? quality : new byte[2];
                break;

            case DBPOS:
                ns.value.setType("dbpos");
                ns.value.dbpos = dbpos;
                break;

            case TCMD:
                ns.value.setType("tcmd");
                ns.value.tcmd = tcmd;
                break;

            case CHECK:
                ns.value.setType("check");
                ns.value.check = check != null ? check : new byte[2];
                break;

            default:
                throw new IllegalArgumentException("Unknown choice: " + choice);
        }
    }
    
    private void syncChildArray(CmsArray.ArrayStruct arr, List<Pointer> allocs) {
        int count = arrayVal != null ? arrayVal.size() : 0;
        arr.count = count;
        if (count == 0) {
            arr.elements = null;
            return;
        }

        long elemSize = new NativeStruct().size();
        Memory block = new Memory(elemSize * count);
        allocs.add(block);

        for (int i = 0; i < count; i++) {
            Pointer elemPtr = block.share(i * elemSize);
            NativeStruct elemNs = new NativeStruct(elemPtr);
            CmsData elem = arrayVal.get(i);
            elem.syncToNative(elemNs, allocs);
            elemNs.write();
        }
        arr.elements = block;
    }

    // ===================== Decode =====================

    public static CmsData decode(byte[] data) {
        NativeStruct ns = new NativeStruct();
        CmsFFIDatatypes.INSTANCE.cms_data_decode(data, data.length, ns);
        ns.read();

        CmsData result;
        try {
            result = syncFromNative(ns);
        } finally {
            CmsFFIDatatypes.INSTANCE.cms_data_free(ns);
        }
        return result;
    }

    private static String unionFieldName(int choice) {
        switch (choice) {
            case ERROR:          return "error";
            case ARRAY:          return "array";
            case STRUCTURE:      return "structure";
            case BOOLEAN:        return "boolean_value";
            case INT8:           return "int8";
            case INT16:          return "int16";
            case INT32:          return "int32";
            case INT64:          return "int64";
            case INT8U:          return "int8u";
            case INT16U:         return "int16u";
            case INT32U:         return "int32u";
            case INT64U:         return "int64u";
            case FLOAT32:        return "float32";
            case FLOAT64:        return "float64";
            case BIT_STRING:     return "bit_string";
            case OCTET_STRING:   return "octet_string";
            case VISIBLE_STRING: return "visible_string";
            case UTF8_STRING:    return "utf8_string";
            case UTC_TIME:       return "utc_time_ms";
            case BINARY_TIME:    return "binary_time";
            case QUALITY:        return "quality";
            case DBPOS:          return "dbpos";
            case TCMD:           return "tcmd";
            case CHECK:          return "check";
            default:
                throw new IllegalArgumentException("Unknown choice: " + choice);
        }
    }

    private static CmsData syncFromNative(NativeStruct ns) {
        int ch = ns.choice;

        // Set union active type so JNA maps the correct field
        ns.value.setType(unionFieldName(ch));
        ns.value.read();

        // Read sub-structures when the union field is a struct
        switch (ch) {
            case ARRAY:
            case STRUCTURE:
                ns.value.array.read();
                break;
            case BIT_STRING:
                ns.value.bit_string.read();
                break;
            case OCTET_STRING:
                ns.value.octet_string.read();
                break;
            case BINARY_TIME:
                ns.value.binary_time.read();
                break;
        }

        switch (ch) {
            case ERROR:
                return createError(ns.value.error);

            case ARRAY:
            case STRUCTURE: {
                CmsArray.ArrayStruct arr = (ch == ARRAY) ? ns.value.array : ns.value.structure;
                int count = arr.count;
                List<CmsData> elems = new ArrayList<>(count);
                if (count > 0 && arr.elements != null) {
                    long elemSize = new NativeStruct().size();
                    for (int i = 0; i < count; i++) {
                        Pointer elemPtr = arr.elements.share(i * elemSize);
                        NativeStruct elemNs = new NativeStruct(elemPtr);
                        elemNs.read();
                        elems.add(syncFromNative(elemNs));
                    }
                }
                CmsArray ca = new CmsArray(elems);
                return ch == ARRAY ? createArray(ca) : createStructure(ca);
            }

            case BOOLEAN:
                return createBoolean(ns.value.boolean_value != 0);

            case INT8:
                return createInt8(ns.value.int8);

            case INT16:
                return createInt16(ns.value.int16);

            case INT32:
                return createInt32(ns.value.int32);

            case INT64:
                return createInt64(ns.value.int64);

            case INT8U:
                return createInt8U((short) (ns.value.int8u & 0xFF));

            case INT16U:
                return createInt16U(ns.value.int16u & 0xFFFF);

            case INT32U:
                return createInt32U(ns.value.int32u & 0xFFFFFFFFL);

            case INT64U:
                return createInt64U(ns.value.int64u);

            case FLOAT32:
                return createFloat32(ns.value.float32);

            case FLOAT64:
                return createFloat64(ns.value.float64);

            case BIT_STRING: {
                int nbits = ns.value.bit_string.nbits;
                int nbytes = (nbits + 7) / 8;
                byte[] data = new byte[nbytes];
                if (nbytes > 0 && ns.value.bit_string.data != null) {
                    ns.value.bit_string.data.read(0, data, 0, nbytes);
                }
                return createBitString(data, nbits);
            }

            case OCTET_STRING: {
                int len = ns.value.octet_string.len;
                byte[] data = new byte[len];
                if (len > 0 && ns.value.octet_string.data != null) {
                    ns.value.octet_string.data.read(0, data, 0, len);
                }
                return createOctetString(data);
            }

            case VISIBLE_STRING: {
                String s = ns.value.visible_string != null
                        ? ns.value.visible_string.getString(0) : "";
                return createVisibleString(s);
            }

            case UTF8_STRING: {
                String s = ns.value.utf8_string != null
                        ? ns.value.utf8_string.getString(0, "UTF-8") : "";
                return createUtf8String(s);
            }

            case UTC_TIME:
                return createUtcTime(ns.value.utc_time_ms);

            case BINARY_TIME:
                return createBinaryTime(new CmsBinaryTime(
                        ns.value.binary_time.msOfDay,
                        ns.value.binary_time.daysSince1984 & 0xFFFF));

            case QUALITY:
                return createQuality(ns.value.quality.clone());

            case DBPOS:
                return createDbpos(ns.value.dbpos);

            case TCMD:
                return createTcmd(ns.value.tcmd);

            case CHECK:
                return createCheck(ns.value.check.clone());

            default:
                throw new IllegalArgumentException("Unknown choice: " + ch);
        }
    }

    // ===================== isTagOnly =====================

    @Override
    public boolean isTagOnly() {
        return false; // Data has no tag-only choices; every choice carries a value
    }

    // ===================== Copy =====================

    @Override
    public CmsData copy() {
        CmsData c = new CmsData();
        c.choice = choice;
        c.serviceError = serviceError;

        c.arrayVal = arrayVal != null ? arrayVal.copy() : null;

        c.boolVal = boolVal;
        c.intVal = intVal;
        c.floatVal = floatVal;
        c.strVal = strVal;
        c.bytesVal = bytesVal != null ? bytesVal.clone() : null;
        c.nbits = nbits;
        c.utcTimeMs = utcTimeMs;
        c.binaryTimeVal = binaryTimeVal != null ? binaryTimeVal.copy() : null;
        c.quality = quality != null ? quality.clone() : null;
        c.dbpos = dbpos;
        c.tcmd = tcmd;
        c.check = check != null ? check.clone() : null;
        return c;
    }

    // ===================== Helpers =====================

    private static Pointer stringToPointer(String s, List<Pointer> allocs) {
        if (s == null) return null;
        byte[] bytes = s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        Memory mem = new Memory(bytes.length + 1);
        mem.write(0, bytes, 0, bytes.length);
        mem.setByte(bytes.length, (byte) 0); // null terminator
        allocs.add(mem);
        return mem;
    }

    @Override
    public String toString() {
        switch (choice) {
            case ERROR: return "Data(error=" + serviceError + ")";
            case ARRAY: return "Data(array=" + arrayVal + ")";
            case STRUCTURE: return "Data(structure=" + arrayVal + ")";
            case BOOLEAN: return "Data(bool=" + boolVal + ")";
            case INT8: case INT16: case INT32: case INT64:
            case INT8U: case INT16U: case INT32U: case INT64U:
                return "Data(int=" + intVal + ")";
            case FLOAT32: case FLOAT64:
                return "Data(float=" + floatVal + ")";
            case BIT_STRING: return "Data(bit_string[" + nbits + "])";
            case OCTET_STRING: return "Data(octet_string[" + (bytesVal != null ? bytesVal.length : 0) + "])";
            case VISIBLE_STRING: return "Data(visible=" + strVal + ")";
            case UTF8_STRING: return "Data(utf8=" + strVal + ")";
            case UTC_TIME: return "Data(utcTime=" + utcTimeMs + "ms)";
            case BINARY_TIME: return "Data(binaryTime=" + binaryTimeVal + ")";
            case QUALITY: return "Data(quality)";
            case DBPOS: return "Data(dbpos=" + dbpos + ")";
            case TCMD: return "Data(tcmd=" + tcmd + ")";
            case CHECK: return "Data(check)";
            default: return "Data(choice=" + choice + ")";
        }
    }
}
