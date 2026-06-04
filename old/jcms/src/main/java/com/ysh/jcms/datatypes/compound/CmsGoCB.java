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
public class CmsGoCB extends AbstractCmsCompound<CmsGoCB> {

    public static class NativeStruct extends Structure {
        public int goEna;
        public byte[] goID = new byte[130];
        public byte[] datSet = new byte[256];
        public int confRev;
        public int ndsCom;
        public byte[] dstAddr = new byte[6];
        public byte dstPriority;
        public short dstVid;
        public short dstAppId;
        public int dstAddress_present;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("goEna", "goID", "datSet", "confRev", "ndsCom",
                    "dstAddr", "dstPriority", "dstVid", "dstAppId", "dstAddress_present");
        }
    }

    private NativeStruct ns = new NativeStruct();

    public boolean goEna;
    public String goID;
    public String datSet;
    public long confRev;
    public boolean ndsCom;
    public byte[] dstAddr;       // null if not present
    public int dstPriority;
    public int dstVid;
    public int dstAppId;

    public CmsGoCB() {
        super("GoCB");
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
    public CmsGoCB decode(byte[] data) {
        ffiDecode(data);
        ns.read();
        syncFromNative();
        return this;
    }

    @Override
    protected void syncToNative() {
        ns.goEna = goEna ? 1 : 0;
        byte[] idBuf = new byte[130];
        if (goID != null) {
            byte[] src = goID.getBytes();
            System.arraycopy(src, 0, idBuf, 0, Math.min(src.length, 129));
        }
        ns.goID = idBuf;
        byte[] dsBuf = new byte[256];
        if (datSet != null) {
            byte[] src = datSet.getBytes();
            System.arraycopy(src, 0, dsBuf, 0, Math.min(src.length, 255));
        }
        ns.datSet = dsBuf;
        ns.confRev = (int) confRev;
        ns.ndsCom = ndsCom ? 1 : 0;
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
        goEna = ns.goEna != 0;
        goID = new String(ns.goID).trim();
        datSet = new String(ns.datSet).trim();
        confRev = ns.confRev & 0xFFFFFFFFL;
        ndsCom = ns.ndsCom != 0;
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
        return CmsFFIDatatypes.INSTANCE.cms_gocb_encode(ns, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_gocb_decode(data, data.length, ns);
        ns.read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    public static CmsGoCB from(byte[] data) {
        return new CmsGoCB().decode(data);
    }
}
