package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
@EqualsAndHashCode
public final class CmsData {

    public static final int CHOICE_SERVICE_ERROR = 0;
    public static final int CHOICE_ARRAY = 1;
    public static final int CHOICE_STRUCTURE = 2;
    public static final int CHOICE_BOOLEAN = 3;
    public static final int CHOICE_INT8 = 4;
    public static final int CHOICE_INT16 = 5;
    public static final int CHOICE_INT32 = 6;
    public static final int CHOICE_INT64 = 7;
    public static final int CHOICE_INT8U = 8;
    public static final int CHOICE_INT16U = 9;
    public static final int CHOICE_INT32U = 10;
    public static final int CHOICE_INT64U = 11;
    public static final int CHOICE_FLOAT32 = 12;
    public static final int CHOICE_FLOAT64 = 13;
    public static final int CHOICE_BIT_STRING = 14;
    public static final int CHOICE_OCTET_STRING = 15;
    public static final int CHOICE_VISIBLE_STRING = 16;
    public static final int CHOICE_UTF8_STRING = 17;
    public static final int CHOICE_UTC_TIME = 18;
    public static final int CHOICE_BINARY_TIME = 19;
    public static final int CHOICE_QUALITY = 20;
    public static final int CHOICE_DBPOS = 21;
    public static final int CHOICE_TCMD = 22;
    public static final int CHOICE_CHECK = 23;

    private final int choice;
    private final long intValue;
    private final double floatValue;
    private final String stringValue;
    private final byte[] bytesValue;

    public CmsData(int choice, long intValue, double floatValue, String stringValue, byte[] bytesValue) {
        this.choice = choice;
        this.intValue = intValue;
        this.floatValue = floatValue;
        this.stringValue = stringValue;
        this.bytesValue = bytesValue;
    }

    public static CmsData fromBoolean(boolean v) {
        return new CmsData(CHOICE_BOOLEAN, v ? 1 : 0, 0, null, null);
    }

    public static CmsData fromInt8(long v) {
        return new CmsData(CHOICE_INT8, v, 0, null, null);
    }

    public static CmsData fromInt16(long v) {
        return new CmsData(CHOICE_INT16, v, 0, null, null);
    }

    public static CmsData fromInt32(long v) {
        return new CmsData(CHOICE_INT32, v, 0, null, null);
    }

    public static CmsData fromInt64(long v) {
        return new CmsData(CHOICE_INT64, v, 0, null, null);
    }

    public static CmsData fromInt8U(long v) {
        return new CmsData(CHOICE_INT8U, v, 0, null, null);
    }

    public static CmsData fromInt16U(long v) {
        return new CmsData(CHOICE_INT16U, v, 0, null, null);
    }

    public static CmsData fromInt32U(long v) {
        return new CmsData(CHOICE_INT32U, v, 0, null, null);
    }

    public static CmsData fromInt64U(long v) {
        return new CmsData(CHOICE_INT64U, v, 0, null, null);
    }

    public static CmsData fromFloat32(float v) {
        return new CmsData(CHOICE_FLOAT32, 0, v, null, null);
    }

    public static CmsData fromFloat64(double v) {
        return new CmsData(CHOICE_FLOAT64, 0, v, null, null);
    }

    public static CmsData fromVisibleString(String v) {
        return new CmsData(CHOICE_VISIBLE_STRING, 0, 0, v, null);
    }

    public static CmsData fromUTF8String(String v) {
        return new CmsData(CHOICE_UTF8_STRING, 0, 0, v, null);
    }

    public static CmsData fromOctetString(byte[] v) {
        return new CmsData(CHOICE_OCTET_STRING, 0, 0, null, v);
    }

    public static CmsData fromBitString(byte[] v) {
        return new CmsData(CHOICE_BIT_STRING, 0, 0, null, v);
    }

    public static CmsData fromUtcTime(long timestampMs) {
        return new CmsData(CHOICE_UTC_TIME, timestampMs, 0, null, null);
    }

    public static CmsData fromBinaryTime(long msOfDay) {
        return new CmsData(CHOICE_BINARY_TIME, msOfDay, 0, null, null);
    }

    public static CmsData fromQuality(byte[] v) {
        return new CmsData(CHOICE_QUALITY, 0, 0, null, v);
    }

    public static CmsData fromDbpos(int v) {
        return new CmsData(CHOICE_DBPOS, v, 0, null, null);
    }

    public static CmsData fromTcmd(int v) {
        return new CmsData(CHOICE_TCMD, v, 0, null, null);
    }

    public static CmsData fromCheck(byte[] v) {
        return new CmsData(CHOICE_CHECK, 0, 0, null, v);
    }

    public static CmsData fromServiceError(int v) {
        return new CmsData(CHOICE_SERVICE_ERROR, v, 0, null, null);
    }

    public byte[] encode() {
        byte[] buf = new byte[4096];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Data(
            choice, intValue, floatValue,
            stringValue, bytesValue, bytesValue != null ? bytesValue.length : 0,
            buf, outLen
        );
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsData decode(byte[] data) {
        IntByReference choice = new IntByReference();
        LongByReference intVal = new LongByReference();
        double[] floatVal = new double[1];
        byte[] strBuf = new byte[256];
        IntByReference strCap = new IntByReference(strBuf.length);
        byte[] bytesBuf = new byte[4096];
        IntByReference bytesCap = new IntByReference(bytesBuf.length);

        CmsFFI.INSTANCE.cms_ffi_decode_Data(data, data.length,
            choice, intVal, floatVal, strBuf, strCap, bytesBuf, bytesCap);

        int c = choice.getValue();
        long iv = intVal.getValue();
        double fv = floatVal[0];
        String sv = strCap.getValue() > 0
            ? new String(strBuf, 0, strCap.getValue(), StandardCharsets.US_ASCII)
            : null;
        byte[] bv = bytesCap.getValue() > 0
            ? java.util.Arrays.copyOf(bytesBuf, bytesCap.getValue())
            : null;

        return new CmsData(c, iv, fv, sv, bv);
    }

    @Override
    public String toString() {
        return "CmsData{choice=" + choice + "}";
    }
}
