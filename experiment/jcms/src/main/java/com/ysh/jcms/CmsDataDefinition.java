package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
@EqualsAndHashCode
public final class CmsDataDefinition {

    private final String dataName;
    private final String dataType;
    private final byte[] fc;
    private final CmsData data;

    public CmsDataDefinition(String dataName, String dataType, byte[] fc, CmsData data) {
        this.dataName = dataName;
        this.dataType = dataType;
        this.fc = fc;
        this.data = data;
    }

    public byte[] encode() {
        byte[] buf = new byte[4096];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_DataDefinition(
            dataName, dataType, fc,
            data.getChoice(), data.getIntValue(), data.getFloatValue(),
            data.getStringValue(), data.getBytesValue(),
            data.getBytesValue() != null ? data.getBytesValue().length : 0,
            buf, outLen
        );
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsDataDefinition decode(byte[] data) {
        byte[] nameBuf = new byte[128];
        IntByReference nameCap = new IntByReference(nameBuf.length);
        byte[] typeBuf = new byte[128];
        IntByReference typeCap = new IntByReference(typeBuf.length);
        byte[] fcBuf = new byte[2];
        IntByReference dataChoice = new IntByReference();
        LongByReference dataInt = new LongByReference();
        double[] dataFloat = new double[1];
        byte[] dataStrBuf = new byte[256];
        IntByReference dataStrCap = new IntByReference(dataStrBuf.length);
        byte[] dataBytesBuf = new byte[4096];
        IntByReference dataBytesCap = new IntByReference(dataBytesBuf.length);

        CmsFFI.INSTANCE.cms_ffi_decode_DataDefinition(
            data, data.length,
            nameBuf, nameCap, typeBuf, typeCap, fcBuf,
            dataChoice, dataInt, dataFloat,
            dataStrBuf, dataStrCap, dataBytesBuf, dataBytesCap
        );

        String name = new String(nameBuf, 0, nameCap.getValue(), StandardCharsets.US_ASCII);
        String type = new String(typeBuf, 0, typeCap.getValue(), StandardCharsets.US_ASCII);
        byte[] fc = java.util.Arrays.copyOf(fcBuf, 2);

        String strVal = dataStrCap.getValue() > 0
            ? new String(dataStrBuf, 0, dataStrCap.getValue(), StandardCharsets.US_ASCII)
            : null;
        byte[] bytesVal = dataBytesCap.getValue() > 0
            ? java.util.Arrays.copyOf(dataBytesBuf, dataBytesCap.getValue())
            : null;

        CmsData cmsData = new CmsData(
            dataChoice.getValue(), dataInt.getValue(),
            dataFloat[0], strVal, bytesVal
        );

        return new CmsDataDefinition(name, type, fc, cmsData);
    }

    @Override
    public String toString() {
        return "DataDefinition{name=" + dataName + ", type=" + dataType + "}";
    }
}
