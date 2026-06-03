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

    public CmsLCB() {
        super("LCB");
        nativeStruct = new NativeStruct();
    }

    @Override
    protected void syncToNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        ns.logEna = logEna ? 1 : 0;
        byte[] dsBuf = new byte[256];
        if (datSet != null) {
            byte[] src = datSet.getBytes();
            System.arraycopy(src, 0, dsBuf, 0, Math.min(src.length, 255));
        }
        ns.datSet = dsBuf;
        ns.trgOps = trgOps != null ? trgOps : new byte[1];
        ns.intgPd = (int) intgPd;
        byte[] lrBuf = new byte[256];
        if (logRef != null) {
            byte[] src = logRef.getBytes();
            System.arraycopy(src, 0, lrBuf, 0, Math.min(src.length, 255));
        }
        ns.logRef = lrBuf;
        ns.optFlds_present = optFlds != null ? 1 : 0;
        ns.optFlds = optFlds != null ? optFlds : new byte[1];
        ns.bufTm_present = bufTm != null ? 1 : 0;
        ns.bufTm = bufTm != null ? (int) (long) bufTm : 0;
    }

    @Override
    protected void syncFromNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        logEna = ns.logEna != 0;
        datSet = new String(ns.datSet).trim();
        trgOps = ns.trgOps.clone();
        intgPd = ns.intgPd & 0xFFFFFFFFL;
        logRef = new String(ns.logRef).trim();
        optFlds = ns.optFlds_present != 0 ? ns.optFlds.clone() : null;
        bufTm = ns.bufTm_present != 0 ? (ns.bufTm & 0xFFFFFFFFL) : null;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_lcb_encode((NativeStruct) nativeStruct, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_lcb_decode(data, data.length, nativeStruct);
        ((NativeStruct) nativeStruct).read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    public static CmsLCB from(byte[] data) {
        return new CmsLCB().decode(data);
    }
}
