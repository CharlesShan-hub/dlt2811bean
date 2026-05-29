package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsDataDefinition {

    private final String dataName;
    private final String dataType;
    private final byte[] fc;
    private final int dataChoice;
    private final long dataInt;
    private final double dataFloat;
    private final String dataStr;
    private final byte[] dataBytes;

    public CmsDataDefinition(String dataName, String dataType, byte[] fc,
                             int dataChoice, long dataInt, double dataFloat,
                             String dataStr, byte[] dataBytes) {
        this.dataName = dataName;
        this.dataType = dataType;
        this.fc = fc;
        this.dataChoice = dataChoice;
        this.dataInt = dataInt;
        this.dataFloat = dataFloat;
        this.dataStr = dataStr;
        this.dataBytes = dataBytes;
    }

    public byte[] encode() {
        byte[] buf = new byte[65536];
        IntByReference outLen = new IntByReference(buf.length);
        byte[] bytes = dataBytes != null ? dataBytes : new byte[0];
        CmsFFI.INSTANCE.cms_ffi_encode_DataDefinition(
            dataName, dataType, fc,
            dataChoice, dataInt, dataFloat,
            dataStr, bytes, bytes.length,
            buf, outLen
        );
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsDataDefinition decode(byte[] data) {
        byte[] nameBuf = new byte[65536];
        IntByReference nameCap = new IntByReference(nameBuf.length);
        byte[] typeBuf = new byte[65536];
        IntByReference typeCap = new IntByReference(typeBuf.length);
        byte[] fc = new byte[2];
        IntByReference dataChoice = new IntByReference();
        LongByReference dataInt = new LongByReference();
        double[] dataFloat = new double[1];
        byte[] strBuf = new byte[65536];
        IntByReference strCap = new IntByReference(strBuf.length);
        byte[] bytesBuf = new byte[65536];
        IntByReference bytesCap = new IntByReference(bytesBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_DataDefinition(
            data, data.length,
            nameBuf, nameCap,
            typeBuf, typeCap,
            fc,
            dataChoice, dataInt, dataFloat,
            strBuf, strCap,
            bytesBuf, bytesCap
        );
        String name = new String(nameBuf, 0, nameCap.getValue());
        String type = new String(typeBuf, 0, typeCap.getValue());
        String str = strCap.getValue() > 0 ? new String(strBuf, 0, strCap.getValue()) : null;
        byte[] bytes = new byte[bytesCap.getValue()];
        System.arraycopy(bytesBuf, 0, bytes, 0, bytes.length);
        return new CmsDataDefinition(name, type, fc, dataChoice.getValue(), dataInt.getValue(), dataFloat[0], str, bytes);
    }
}
