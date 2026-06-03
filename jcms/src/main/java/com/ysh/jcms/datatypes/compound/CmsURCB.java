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
public class CmsURCB extends AbstractCmsCompound<CmsURCB> {

    public static class NativeStruct extends Structure {
        public byte[] rptID = new byte[130];
        public int rptEna;
        public byte[] datSet = new byte[256];
        public int confRev;
        public byte[] optFlds = new byte[2];
        public int bufTm;
        public short sqNum;
        public byte[] trgOps = new byte[1];
        public int intgPd;
        public int gi;
        public int resv;
        public byte[] owner = new byte[64];
        public int owner_len;
        public int owner_present;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("rptID", "rptEna", "datSet", "confRev", "optFlds", "bufTm",
                    "sqNum", "trgOps", "intgPd", "gi", "resv", "owner", "owner_len", "owner_present");
        }
    }

    public String rptID;
    public boolean rptEna;
    public String datSet;
    public long confRev;
    public byte[] optFlds;
    public long bufTm;
    public int sqNum;
    public byte[] trgOps;
    public long intgPd;
    public boolean gi;
    public boolean resv;
    public byte[] owner;         // null if not present

    public CmsURCB() {
        super("URCB");
        nativeStruct = new NativeStruct();
    }

    @Override
    protected void syncToNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        byte[] rptBuf = new byte[130];
        if (rptID != null) {
            byte[] src = rptID.getBytes();
            System.arraycopy(src, 0, rptBuf, 0, Math.min(src.length, 129));
        }
        ns.rptID = rptBuf;
        ns.rptEna = rptEna ? 1 : 0;
        byte[] dsBuf = new byte[256];
        if (datSet != null) {
            byte[] src = datSet.getBytes();
            System.arraycopy(src, 0, dsBuf, 0, Math.min(src.length, 255));
        }
        ns.datSet = dsBuf;
        ns.confRev = (int) confRev;
        ns.optFlds = optFlds != null ? optFlds : new byte[2];
        ns.bufTm = (int) bufTm;
        ns.sqNum = (short) sqNum;
        ns.trgOps = trgOps != null ? trgOps : new byte[1];
        ns.intgPd = (int) intgPd;
        ns.gi = gi ? 1 : 0;
        ns.resv = resv ? 1 : 0;
        ns.owner_present = owner != null ? 1 : 0;
        if (owner != null) {
            ns.owner = owner.length == 64 ? owner : Arrays.copyOf(owner, 64);
            ns.owner_len = owner.length;
        } else {
            ns.owner = new byte[64];
            ns.owner_len = 0;
        }
    }

    @Override
    protected void syncFromNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        rptID = new String(ns.rptID).trim();
        rptEna = ns.rptEna != 0;
        datSet = new String(ns.datSet).trim();
        confRev = ns.confRev & 0xFFFFFFFFL;
        optFlds = ns.optFlds.clone();
        bufTm = ns.bufTm & 0xFFFFFFFFL;
        sqNum = ns.sqNum & 0xFFFF;
        trgOps = ns.trgOps.clone();
        intgPd = ns.intgPd & 0xFFFFFFFFL;
        gi = ns.gi != 0;
        resv = ns.resv != 0;
        owner = ns.owner_present != 0 ? Arrays.copyOf(ns.owner, ns.owner_len) : null;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_urcb_encode((NativeStruct) nativeStruct, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_urcb_decode(data, data.length, nativeStruct);
        ((NativeStruct) nativeStruct).read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    public static CmsURCB from(byte[] data) {
        return new CmsURCB().decode(data);
    }
}
