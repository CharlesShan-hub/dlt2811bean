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

    private NativeStruct ns = new NativeStruct();

    public boolean svEna;
    public String msvID;
    public String datSet;
    public long confRev;
    public Integer smpMod;       // null if not present
    public int smpRate;
    public byte[] optFlds;
    public byte[] dstAddr;       // null if not present
    public int dstPriority;
    public int dstVid;
    public int dstAppId;

    public CmsMSVCB() {
        super("MSVCB");
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList();
    }

    @Override
    public byte[] encode() {
        syncToNative();
        ns.write();
        byte[] buf = new byte[encodeBufSize()];
        IntByReference outLen = new IntByReference(buf.length);
        ffiEncode(buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsMSVCB decode(byte[] data) {
        ffiDecode(data);
        ns.read();
        syncFromNative();
        return this;
    }

    @Override
    protected void syncToNative() {
        ns.svEna = svEna ? 1 : 0;
        byte[] idBuf = new byte[130];
        if (msvID != null) {
            byte[] src = msvID.getBytes();
            System.arraycopy(src, 0, idBuf, 0, Math.min(src.length, 129));
        }
        ns.msvID = idBuf;
        byte[] dsBuf = new byte[256];
        if (datSet != null) {
            byte[] src = datSet.getBytes();
            System.arraycopy(src, 0, dsBuf, 0, Math.min(src.length, 255));
        }
        ns.datSet = dsBuf;
        ns.confRev = (int) confRev;
        ns.smpMod_present = smpMod != null ? 1 : 0;
        ns.smpMod = smpMod != null ? smpMod : 0;
        ns.smpRate = (short) smpRate;
        ns.optFlds = optFlds != null ? optFlds : new byte[1];
        ns.dstAddress_present = dstAddr != null ? 1 : 0;
        if (dstAddr != null) {
            ns.dstAddr = dstAddr.length == 6 ? dstAddr : Arrays.copyOf(dstAddr, 6);
            ns.dstPriority = (byte) dstPriority;
            ns.dstVid = (short) dstVid;
            ns.dstAppId = (short) dstAppId;
        } else {
            ns.dstAddr = new byte[6];
            ns.dstPriority = 0;
            ns.dstVid = 0;
            ns.dstAppId = 0;
        }
    }

    @Override
    protected void syncFromNative() {
        svEna = ns.svEna != 0;
        msvID = new String(ns.msvID).trim();
        datSet = new String(ns.datSet).trim();
        confRev = ns.confRev & 0xFFFFFFFFL;
        smpMod = ns.smpMod_present != 0 ? ns.smpMod : null;
        smpRate = ns.smpRate & 0xFFFF;
        optFlds = ns.optFlds.clone();
        if (ns.dstAddress_present != 0) {
            dstAddr = ns.dstAddr.clone();
            dstPriority = ns.dstPriority & 0xFF;
            dstVid = ns.dstVid & 0xFFFF;
            dstAppId = ns.dstAppId & 0xFFFF;
        } else {
            dstAddr = null;
            dstPriority = 0;
            dstVid = 0;
            dstAppId = 0;
        }
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_msvcb_encode(ns, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_msvcb_decode(data, data.length, ns);
        ns.read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    public static CmsMSVCB from(byte[] data) {
        return new CmsMSVCB().decode(data);
    }
}
