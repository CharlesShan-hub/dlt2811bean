package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
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

    public CmsSGCB() {
        super("SGCB");
        nativeStruct = new NativeStruct();
        ((NativeStruct) nativeStruct).tActEdt = new CmsUtcTime.NativeStruct();
    }

    private void syncToNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        ns.numOfSG = (byte) numOfSG;
        ns.actSG = (byte) actSG;
        ns.editSG = (byte) editSG;
        if (tActEdt != null) {
            ns.tActEdt.seconds_since_epoch = tActEdt.seconds_since_epoch;
            ns.tActEdt.fraction_of_second = tActEdt.fraction_of_second;
            ns.tActEdt.time_quality = tActEdt.time_quality;
        }
        ns.resvTms_present = resvTms != null ? 1 : 0;
        ns.resvTms = resvTms != null ? (short) (int) resvTms : 0;
    }

    private void syncFromNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        numOfSG = ns.numOfSG & 0xFF;
        actSG = ns.actSG & 0xFF;
        editSG = ns.editSG & 0xFF;
        tActEdt = new CmsUtcTime();
        tActEdt.seconds_since_epoch = ns.tActEdt.seconds_since_epoch;
        tActEdt.fraction_of_second = ns.tActEdt.fraction_of_second;
        tActEdt.time_quality = ns.tActEdt.time_quality;
        resvTms = ns.resvTms_present != 0 ? (ns.resvTms & 0xFFFF) : null;
    }

    public byte[] encode() {
        syncToNative();
        write();
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_sgcb_encode((NativeStruct) nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsSGCB decode(byte[] data) {
        CmsSGCB obj = new CmsSGCB();
        CmsFFIDatatypes.INSTANCE.cms_sgcb_decode(data, data.length, obj.nativeStruct);
        ((NativeStruct) obj.nativeStruct).read();
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
