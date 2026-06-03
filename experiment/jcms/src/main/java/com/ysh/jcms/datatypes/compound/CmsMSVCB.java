package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

import java.util.Arrays;
import java.util.List;

public class CmsMSVCB extends AbstractCmsCompound<CmsMSVCB> {

    public static class NativeStruct extends Structure {
        public int svEna;
        public byte[] msvID = new byte[130];
        public byte[] datSet = new byte[256];
        public int confRev;
        public int smpMod;
        public int smpMod_present;
        public short smpRate;
        public byte[] optFlds = new byte[1];
        public byte[] dstAddr = new byte[6];
        public byte dstPriority;
        public short dstVid;
        public short dstAppId;
        public int dstAddress_present;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("svEna", "msvID", "datSet", "confRev",
                    "smpMod", "smpMod_present", "smpRate", "optFlds",
                    "dstAddr", "dstPriority", "dstVid", "dstAppId", "dstAddress_present");
        }
    }

    public boolean svEna;
    public String msvID;
    public String datSet;
    public long confRev;
    public Integer smpMod;       // null if not present, cms_smp_mod_t enum value
    public int smpRate;
    public byte[] optFlds;
    public byte[] dstAddr;       // null if not present
    public int dstPriority;
    public int dstVid;
    public int dstAppId;

    private final NativeStruct nativeStruct;

    public CmsMSVCB() {
        super("MSVCB");
        this.nativeStruct = new NativeStruct();
        setNativeStruct(nativeStruct);
    }

    private void syncToNative() {
        nativeStruct.svEna = svEna ? 1 : 0;
        byte[] idBuf = new byte[130];
        if (msvID != null) {
            byte[] src = msvID.getBytes();
            System.arraycopy(src, 0, idBuf, 0, Math.min(src.length, 129));
        }
        nativeStruct.msvID = idBuf;
        byte[] dsBuf = new byte[256];
        if (datSet != null) {
            byte[] src = datSet.getBytes();
            System.arraycopy(src, 0, dsBuf, 0, Math.min(src.length, 255));
        }
        nativeStruct.datSet = dsBuf;
        nativeStruct.confRev = (int) confRev;
        nativeStruct.smpMod_present = smpMod != null ? 1 : 0;
        nativeStruct.smpMod = smpMod != null ? smpMod : 0;
        nativeStruct.smpRate = (short) smpRate;
        nativeStruct.optFlds = optFlds != null ? optFlds : new byte[1];
        nativeStruct.dstAddress_present = dstAddr != null ? 1 : 0;
        if (dstAddr != null) {
            nativeStruct.dstAddr = dstAddr.length == 6 ? dstAddr : Arrays.copyOf(dstAddr, 6);
            nativeStruct.dstPriority = (byte) dstPriority;
            nativeStruct.dstVid = (short) dstVid;
            nativeStruct.dstAppId = (short) dstAppId;
        } else {
            nativeStruct.dstAddr = new byte[6];
            nativeStruct.dstPriority = 0;
            nativeStruct.dstVid = 0;
            nativeStruct.dstAppId = 0;
        }
    }

    private void syncFromNative() {
        svEna = nativeStruct.svEna != 0;
        msvID = new String(nativeStruct.msvID).trim();
        datSet = new String(nativeStruct.datSet).trim();
        confRev = nativeStruct.confRev & 0xFFFFFFFFL;
        smpMod = nativeStruct.smpMod_present != 0 ? nativeStruct.smpMod : null;
        smpRate = nativeStruct.smpRate & 0xFFFF;
        optFlds = nativeStruct.optFlds.clone();
        if (nativeStruct.dstAddress_present != 0) {
            dstAddr = nativeStruct.dstAddr.clone();
            dstPriority = nativeStruct.dstPriority & 0xFF;
            dstVid = nativeStruct.dstVid & 0xFFFF;
            dstAppId = nativeStruct.dstAppId & 0xFFFF;
        } else {
            dstAddr = null;
            dstPriority = 0;
            dstVid = 0;
            dstAppId = 0;
        }
    }

    public byte[] encode() {
        syncToNative();
        write();
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_msvcb_encode(nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsMSVCB decode(byte[] data) {
        CmsMSVCB obj = new CmsMSVCB();
        CmsFFIDatatypes.INSTANCE.cms_msvcb_decode(data, data.length, obj.nativeStruct);
        obj.nativeStruct.read();
        obj.syncFromNative();
        return obj;
    }

    public CmsMSVCB copy() {
        CmsMSVCB clone = new CmsMSVCB();
        clone.svEna = this.svEna;
        clone.msvID = this.msvID;
        clone.datSet = this.datSet;
        clone.confRev = this.confRev;
        clone.smpMod = this.smpMod;
        clone.smpRate = this.smpRate;
        clone.optFlds = this.optFlds != null ? this.optFlds.clone() : null;
        clone.dstAddr = this.dstAddr != null ? this.dstAddr.clone() : null;
        clone.dstPriority = this.dstPriority;
        clone.dstVid = this.dstVid;
        clone.dstAppId = this.dstAppId;
        return clone;
    }
}
