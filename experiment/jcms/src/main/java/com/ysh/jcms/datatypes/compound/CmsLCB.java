package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

import java.util.Arrays;
import java.util.List;

public class CmsLCB extends AbstractCmsCompound<CmsLCB> {

    public static class NativeStruct extends Structure {
        public int logEna;
        public byte[] datSet = new byte[256];
        public byte[] trgOps = new byte[1];
        public int intgPd;
        public byte[] logRef = new byte[256];
        public byte[] optFlds = new byte[1];
        public int optFlds_present;
        public int bufTm;
        public int bufTm_present;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("logEna", "datSet", "trgOps", "intgPd", "logRef",
                    "optFlds", "optFlds_present", "bufTm", "bufTm_present");
        }
    }

    public boolean logEna;
    public String datSet;
    public byte[] trgOps;
    public long intgPd;
    public String logRef;
    public byte[] optFlds;       // null if not present
    public Long bufTm;           // null if not present

    private final NativeStruct nativeStruct;

    public CmsLCB() {
        super("LCB");
        this.nativeStruct = new NativeStruct();
        setNativeStruct(nativeStruct);
    }

    private void syncToNative() {
        nativeStruct.logEna = logEna ? 1 : 0;
        byte[] dsBuf = new byte[256];
        if (datSet != null) {
            byte[] src = datSet.getBytes();
            System.arraycopy(src, 0, dsBuf, 0, Math.min(src.length, 255));
        }
        nativeStruct.datSet = dsBuf;
        nativeStruct.trgOps = trgOps != null ? trgOps : new byte[1];
        nativeStruct.intgPd = (int) intgPd;
        byte[] lrBuf = new byte[256];
        if (logRef != null) {
            byte[] src = logRef.getBytes();
            System.arraycopy(src, 0, lrBuf, 0, Math.min(src.length, 255));
        }
        nativeStruct.logRef = lrBuf;
        nativeStruct.optFlds_present = optFlds != null ? 1 : 0;
        nativeStruct.optFlds = optFlds != null ? optFlds : new byte[1];
        nativeStruct.bufTm_present = bufTm != null ? 1 : 0;
        nativeStruct.bufTm = bufTm != null ? (int) (long) bufTm : 0;
    }

    private void syncFromNative() {
        logEna = nativeStruct.logEna != 0;
        datSet = new String(nativeStruct.datSet).trim();
        trgOps = nativeStruct.trgOps.clone();
        intgPd = nativeStruct.intgPd & 0xFFFFFFFFL;
        logRef = new String(nativeStruct.logRef).trim();
        optFlds = nativeStruct.optFlds_present != 0 ? nativeStruct.optFlds.clone() : null;
        bufTm = nativeStruct.bufTm_present != 0 ? (nativeStruct.bufTm & 0xFFFFFFFFL) : null;
    }

    public byte[] encode() {
        syncToNative();
        write();
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_lcb_encode(nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsLCB decode(byte[] data) {
        CmsLCB obj = new CmsLCB();
        CmsFFIDatatypes.INSTANCE.cms_lcb_decode(data, data.length, obj.nativeStruct);
        obj.nativeStruct.read();
        obj.syncFromNative();
        return obj;
    }

    public CmsLCB copy() {
        CmsLCB clone = new CmsLCB();
        clone.logEna = this.logEna;
        clone.datSet = this.datSet;
        clone.trgOps = this.trgOps != null ? this.trgOps.clone() : null;
        clone.intgPd = this.intgPd;
        clone.logRef = this.logRef;
        clone.optFlds = this.optFlds != null ? this.optFlds.clone() : null;
        clone.bufTm = this.bufTm;
        return clone;
    }
}
