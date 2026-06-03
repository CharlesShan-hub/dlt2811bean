package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

import java.util.Arrays;
import java.util.List;

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

    private final NativeStruct nativeStruct;

    public CmsURCB() {
        super("URCB");
        this.nativeStruct = new NativeStruct();
        setNativeStruct(nativeStruct);
    }

    private void syncToNative() {
        byte[] rptBuf = new byte[130];
        if (rptID != null) {
            byte[] src = rptID.getBytes();
            System.arraycopy(src, 0, rptBuf, 0, Math.min(src.length, 129));
        }
        nativeStruct.rptID = rptBuf;
        nativeStruct.rptEna = rptEna ? 1 : 0;
        byte[] dsBuf = new byte[256];
        if (datSet != null) {
            byte[] src = datSet.getBytes();
            System.arraycopy(src, 0, dsBuf, 0, Math.min(src.length, 255));
        }
        nativeStruct.datSet = dsBuf;
        nativeStruct.confRev = (int) confRev;
        nativeStruct.optFlds = optFlds != null ? optFlds : new byte[2];
        nativeStruct.bufTm = (int) bufTm;
        nativeStruct.sqNum = (short) sqNum;
        nativeStruct.trgOps = trgOps != null ? trgOps : new byte[1];
        nativeStruct.intgPd = (int) intgPd;
        nativeStruct.gi = gi ? 1 : 0;
        nativeStruct.resv = resv ? 1 : 0;
        nativeStruct.owner_present = owner != null ? 1 : 0;
        if (owner != null) {
            nativeStruct.owner = owner.length == 64 ? owner : Arrays.copyOf(owner, 64);
            nativeStruct.owner_len = owner.length;
        } else {
            nativeStruct.owner = new byte[64];
            nativeStruct.owner_len = 0;
        }
    }

    private void syncFromNative() {
        rptID = new String(nativeStruct.rptID).trim();
        rptEna = nativeStruct.rptEna != 0;
        datSet = new String(nativeStruct.datSet).trim();
        confRev = nativeStruct.confRev & 0xFFFFFFFFL;
        optFlds = nativeStruct.optFlds.clone();
        bufTm = nativeStruct.bufTm & 0xFFFFFFFFL;
        sqNum = nativeStruct.sqNum & 0xFFFF;
        trgOps = nativeStruct.trgOps.clone();
        intgPd = nativeStruct.intgPd & 0xFFFFFFFFL;
        gi = nativeStruct.gi != 0;
        resv = nativeStruct.resv != 0;
        owner = nativeStruct.owner_present != 0 ? Arrays.copyOf(nativeStruct.owner, nativeStruct.owner_len) : null;
    }

    public byte[] encode() {
        syncToNative();
        write();
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_urcb_encode(nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsURCB decode(byte[] data) {
        CmsURCB obj = new CmsURCB();
        CmsFFIDatatypes.INSTANCE.cms_urcb_decode(data, data.length, obj.nativeStruct);
        obj.nativeStruct.read();
        obj.syncFromNative();
        return obj;
    }

    public CmsURCB copy() {
        CmsURCB clone = new CmsURCB();
        clone.rptID = this.rptID;
        clone.rptEna = this.rptEna;
        clone.datSet = this.datSet;
        clone.confRev = this.confRev;
        clone.optFlds = this.optFlds != null ? this.optFlds.clone() : null;
        clone.bufTm = this.bufTm;
        clone.sqNum = this.sqNum;
        clone.trgOps = this.trgOps != null ? this.trgOps.clone() : null;
        clone.intgPd = this.intgPd;
        clone.gi = this.gi;
        clone.resv = this.resv;
        clone.owner = this.owner != null ? this.owner.clone() : null;
        return clone;
    }
}
