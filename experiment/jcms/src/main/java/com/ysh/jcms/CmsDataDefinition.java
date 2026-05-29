package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import java.nio.charset.StandardCharsets;

public final class CmsDataDefinition {

    private final int dataChoice;
    private final long dataInt;
    private final String dataStr;
    private final byte[] dataBytes;

    public CmsDataDefinition(int dataChoice, long dataInt, String dataStr, byte[] dataBytes) {
        this.dataChoice = dataChoice;
        this.dataInt = dataInt;
        this.dataStr = dataStr;
        this.dataBytes = dataBytes;
    }

    public int getDataChoice() { return dataChoice; }
    public long getDataInt() { return dataInt; }
    public String getDataStr() { return dataStr; }
    public byte[] getDataBytes() { return dataBytes; }

    public byte[] encode() {
        byte[] buf = new byte[4096];
        IntByReference outLen = new IntByReference(buf.length);
        byte[] bytes = dataBytes != null ? dataBytes : new byte[0];
        CmsFFI.INSTANCE.cms_encode_DataDefinition(dataChoice, dataInt, dataStr, bytes, bytes.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsDataDefinition decode(byte[] data) {
        IntByReference dataChoice = new IntByReference();
        LongByReference dataInt = new LongByReference();
        byte[] strBuf = new byte[256];
        IntByReference strCap = new IntByReference(strBuf.length);
        byte[] bytesBuf = new byte[4096];
        IntByReference bytesCap = new IntByReference(bytesBuf.length);
        CmsFFI.INSTANCE.cms_decode_DataDefinition(data, data.length, dataChoice, dataInt, strBuf, strCap, bytesBuf, bytesCap);
        String str = strCap.getValue() > 0 ? new String(strBuf, 0, strCap.getValue(), StandardCharsets.US_ASCII) : null;
        byte[] bytes = bytesCap.getValue() > 0 ? java.util.Arrays.copyOf(bytesBuf, bytesCap.getValue()) : null;
        return new CmsDataDefinition(dataChoice.getValue(), dataInt.getValue(), str, bytes);
    }
}
