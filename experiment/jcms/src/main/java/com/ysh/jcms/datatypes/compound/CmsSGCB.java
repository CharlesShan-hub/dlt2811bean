package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

import java.util.Arrays;
import java.util.List;

public class CmsSGCB extends AbstractCmsCompound<CmsSGCB> {

    public static class NativeStruct extends Structure {
        public byte numOfSG;
        public byte actSG;
        public byte editSG;
        /* padding byte inserted by compiler to align uint32_t */
        public CmsUtcTime.NativeStruct tActEdt;
        public short resvTms;
        public int resvTms_present;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("numOfSG", "actSG", "editSG", "tActEdt",
                    "resvTms", "resvTms_present");
        }
    }

    public int numOfSG;
    public int actSG;
    public int editSG;
    public CmsUtcTime tActEdt;
    public Integer resvTms;      // null if not present (INT16U)

    private final NativeStruct nativeStruct;

    public CmsSGCB() {
        super("SGCB");
        this.nativeStruct = new NativeStruct();
        this.nativeStruct.tActEdt = new CmsUtcTime.NativeStruct();
        setNativeStruct(nativeStruct);
    }

    private void syncToNative() {
        nativeStruct.numOfSG = (byte) numOfSG;
        nativeStruct.actSG = (byte) actSG;
        nativeStruct.editSG = (byte) editSG;
        if (tActEdt != null) {
            nativeStruct.tActEdt.seconds_since_epoch = tActEdt.seconds_since_epoch;
            nativeStruct.tActEdt.fraction_of_second = tActEdt.fraction_of_second;
            nativeStruct.tActEdt.time_quality = tActEdt.time_quality;
        }
        nativeStruct.resvTms_present = resvTms != null ? 1 : 0;
        nativeStruct.resvTms = resvTms != null ? (short) (int) resvTms : 0;
    }

    private void syncFromNative() {
        numOfSG = nativeStruct.numOfSG & 0xFF;
        actSG = nativeStruct.actSG & 0xFF;
        editSG = nativeStruct.editSG & 0xFF;
        tActEdt = new CmsUtcTime();
        tActEdt.seconds_since_epoch = nativeStruct.tActEdt.seconds_since_epoch;
        tActEdt.fraction_of_second = nativeStruct.tActEdt.fraction_of_second;
        tActEdt.time_quality = nativeStruct.tActEdt.time_quality;
        resvTms = nativeStruct.resvTms_present != 0 ? (nativeStruct.resvTms & 0xFFFF) : null;
    }

    public byte[] encode() {
        syncToNative();
        write();
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_sgcb_encode(nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsSGCB decode(byte[] data) {
        CmsSGCB obj = new CmsSGCB();
        CmsFFIDatatypes.INSTANCE.cms_sgcb_decode(data, data.length, obj.nativeStruct);
        obj.nativeStruct.read();
        obj.syncFromNative();
        return obj;
    }

    public CmsSGCB copy() {
        CmsSGCB clone = new CmsSGCB();
        clone.numOfSG = this.numOfSG;
        clone.actSG = this.actSG;
        clone.editSG = this.editSG;
        clone.tActEdt = this.tActEdt != null ? this.tActEdt.copy() : null;
        clone.resvTms = this.resvTms;
        return clone;
    }
}
