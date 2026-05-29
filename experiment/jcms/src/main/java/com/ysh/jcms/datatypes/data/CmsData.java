package com.ysh.jcms.datatypes.data;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsData extends AbstractCmsDataUnit<Object> {

    private final long intVal;
    private final double floatVal;
    private final String strVal;
    private final byte[] bytesVal;

    public CmsData(int choice, long intVal, double floatVal, String strVal, byte[] bytesVal) {
        super("Data", null);
        set(choice);
        this.intVal = intVal;
        this.floatVal = floatVal;
        this.strVal = strVal;
        this.bytesVal = bytesVal;
    }

    @Override
    public void set(Object value) {
        if (!(value instanceof Integer)) {
            throw new IllegalArgumentException("value must be Integer (choice index)");
        }
        super.set(value);
    }

    public int getChoice() { return (int) value; }
    public long getIntVal() { return intVal; }
    public double getFloatVal() { return floatVal; }
    public String getStrVal() { return strVal; }
    public byte[] getBytesVal() { return bytesVal; }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[65536];
        IntByReference outLen = new IntByReference(buf.length);
        byte[] bytes = bytesVal != null ? bytesVal : new byte[0];
        CmsFFIDatatypes.INSTANCE.cms_encode_Data((int) value, intVal, floatVal, strVal, bytes, bytes.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsData decode(byte[] data) {
        IntByReference choice = new IntByReference();
        LongByReference intVal = new LongByReference();
        double[] floatVal = new double[1];
        byte[] strBuf = new byte[65536];
        IntByReference strCap = new IntByReference(strBuf.length);
        byte[] bytesBuf = new byte[65536];
        IntByReference bytesCap = new IntByReference(bytesBuf.length);
        CmsFFIDatatypes.INSTANCE.cms_decode_Data(data, data.length, choice, intVal, floatVal, strBuf, strCap, bytesBuf, bytesCap);
        String str = strCap.getValue() > 0 ? new String(strBuf, 0, strCap.getValue()) : null;
        byte[] bytes = new byte[bytesCap.getValue()];
        System.arraycopy(bytesBuf, 0, bytes, 0, bytes.length);
        return new CmsData(choice.getValue(), intVal.getValue(), floatVal[0], str, bytes);
    }

    @Override
    public CmsData copy() {
        return new CmsData((int) value, intVal, floatVal, strVal, bytesVal != null ? bytesVal.clone() : null);
    }
}
