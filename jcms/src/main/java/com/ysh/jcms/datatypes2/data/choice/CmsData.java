package com.ysh.jcms.datatypes2.data.choice;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_data_t — tagged union for Data CHOICE (24 alternatives).
 */
public class CmsData extends CmsStructure {

    public static class BitStringStruct extends Structure {
        public Pointer data;
        public int nbits;
        @Override protected List<String> getFieldOrder() { return Arrays.asList("data", "nbits"); }
    }

    public static class OctetStringStruct extends Structure {
        public Pointer data;
        public int len;
        @Override protected List<String> getFieldOrder() { return Arrays.asList("data", "len"); }
    }

    public static class ArrayStruct extends Structure {
        public Pointer elements;
        public int count;
        @Override protected List<String> getFieldOrder() { return Arrays.asList("elements", "count"); }
    }

    public static class BinaryTimeStruct extends Structure {
        public int msOfDay;
        public short daysSince1984;
        @Override protected List<String> getFieldOrder() { return Arrays.asList("msOfDay", "daysSince1984"); }
    }

    public static class CmsDataUnion extends Union {
        public int error;
        public ArrayStruct array;
        public ArrayStruct structure;
        public int booleanValue;
        public byte int8;
        public short int16;
        public int int32;
        public long int64;
        public byte int8u;
        public short int16u;
        public int int32u;
        public long int64u;
        public float float32;
        public double float64;
        public BitStringStruct bitString;
        public OctetStringStruct octetString;
        public Pointer visibleString;
        public Pointer utf8String;
        public long utcTimeMs;
        public BinaryTimeStruct binaryTime;
        public byte[] quality = new byte[2];
        public int dbpos;
        public int tcmd;
        public byte[] check = new byte[2];
    }

    public int choice;
    public CmsDataUnion value = new CmsDataUnion();
    public int serviceError;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("choice", "value", "serviceError");
    }

    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_data_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_data_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsData from(byte[] data) { return new CmsData().decode(data); }
}
