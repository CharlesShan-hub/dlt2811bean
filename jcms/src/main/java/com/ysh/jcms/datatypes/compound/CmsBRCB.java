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
public class CmsBRCB extends AbstractCmsCompound<CmsBRCB> {

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
        public int purgeBuf;
        public byte[] entryID = new byte[8];
        public CmsBinaryTime.NativeStruct timeOfEntry = new CmsBinaryTime.NativeStruct();
        public short resvTms;
        public int resvTms_present;
        public byte[] owner = new byte[64];
        public int owner_len;
        public int owner_present;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("rptID", "rptEna", "datSet", "confRev", "optFlds", "bufTm",
                    "sqNum", "trgOps", "intgPd", "gi", "purgeBuf", "entryID", "timeOfEntry",
                    "resvTms", "resvTms_present", "owner", "owner_len", "owner_present");
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
    public boolean purgeBuf;
    public byte[] entryID;
    public CmsBinaryTime timeOfEntry;
    public Short resvTms;       // null if not present
    public byte[] owner;         // null if not present

    public CmsBRCB() {
        super("BRCB");
        nativeStruct = new NativeStruct();
    }

    private void syncToNative() {
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
        ns.purgeBuf = purgeBuf ? 1 : 0;
        ns.entryID = entryID != null ? entryID : new byte[8];
        if (timeOfEntry != null) {
            ns.timeOfEntry.msOfDay = timeOfEntry.msOfDay;
            ns.timeOfEntry.daysSince1984 = (short) timeOfEntry.daysSince1984;
        }
        ns.resvTms_present = resvTms != null ? 1 : 0;
        if (resvTms != null) {
            ns.resvTms = resvTms;
        }
        ns.owner_present = owner != null ? 1 : 0;
        if (owner != null) {
            ns.owner = owner.length == 64 ? owner : Arrays.copyOf(owner, 64);
            ns.owner_len = owner.length;
        } else {
            ns.owner = new byte[64];
            ns.owner_len = 0;
        }
    }

    private void syncFromNative() {
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
        purgeBuf = ns.purgeBuf != 0;
        entryID = ns.entryID.clone();
        timeOfEntry = new CmsBinaryTime(
                ns.timeOfEntry.msOfDay,
                ns.timeOfEntry.daysSince1984 & 0xFFFF);
        resvTms = ns.resvTms_present != 0 ? ns.resvTms : null;
        owner = ns.owner_present != 0 ? Arrays.copyOf(ns.owner, ns.owner_len) : null;
    }

    public byte[] encode() {
        syncToNative();
        write();
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_brcb_encode((NativeStruct) nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBRCB decode(byte[] data) {
        CmsBRCB obj = new CmsBRCB();
        CmsFFIDatatypes.INSTANCE.cms_brcb_decode(data, data.length, obj.nativeStruct);
        ((NativeStruct) obj.nativeStruct).read();
        obj.syncFromNative();
        return obj;
    }

    public CmsBRCB copy() {
        CmsBRCB clone = new CmsBRCB();
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
        clone.purgeBuf = this.purgeBuf;
        clone.entryID = this.entryID != null ? this.entryID.clone() : null;
        clone.timeOfEntry = this.timeOfEntry != null ? this.timeOfEntry.copy() : null;
        clone.resvTms = this.resvTms;
        clone.owner = this.owner != null ? this.owner.clone() : null;
        return clone;
    }
}
