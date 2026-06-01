package com.ysh.jcms.datatypes.data;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsDataDefinition extends AbstractCmsDataUnit<Object> {

    private final long dataInt;
    private final String dataStr;
    private final byte[] dataBytes;

    public CmsDataDefinition(int dataChoice, long dataInt, String dataStr, byte[] dataBytes) {
        super("DataDefinition", null);
        set(dataChoice);
        this.dataInt = dataInt;
        this.dataStr = dataStr;
        this.dataBytes = dataBytes;
    }

    @Override
    public void set(Object value) {
        if (!(value instanceof Integer)) {
            throw new IllegalArgumentException("value must be Integer (choice index)");
        }
        super.set(value);
    }

    public int getDataChoice() { return (int) value; }
    public long getDataInt() { return dataInt; }
    public String getDataStr() { return dataStr; }
    public byte[] getDataBytes() { return dataBytes; }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[65536];
        IntByReference outLen = new IntByReference(buf.length);
        byte[] bytes = dataBytes != null ? dataBytes : new byte[0];
        CmsFFIDatatypes.INSTANCE.cms_data_definition_encode((int) value, dataInt, dataStr, bytes, bytes.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsDataDefinition decode(byte[] data) {
        IntByReference dataChoice = new IntByReference();
        LongByReference dataInt = new LongByReference();
        byte[] strBuf = new byte[65536];
        IntByReference strCap = new IntByReference(strBuf.length);
        byte[] bytesBuf = new byte[65536];
        IntByReference bytesCap = new IntByReference(bytesBuf.length);
        CmsFFIDatatypes.INSTANCE.cms_data_definition_decode(data, data.length, dataChoice, dataInt, strBuf, strCap, bytesBuf, bytesCap);
        String str = strCap.getValue() > 0 ? new String(strBuf, 0, strCap.getValue()) : null;
        byte[] bytes = new byte[bytesCap.getValue()];
        System.arraycopy(bytesBuf, 0, bytes, 0, bytes.length);
        return new CmsDataDefinition(dataChoice.getValue(), dataInt.getValue(), str, bytes);
    }

    @Override
    public CmsDataDefinition copy() {
        return new CmsDataDefinition((int) value, dataInt, dataStr, dataBytes != null ? dataBytes.clone() : null);
    }
}
