package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

import java.util.Arrays;
import java.util.List;

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

    public boolean goEna;
    public String goID;
    public String datSet;
    public long confRev;
    public boolean ndsCom;
    public byte[] dstAddr;       // null if not present
    public int dstPriority;
    public int dstVid;
    public int dstAppId;

    private final NativeStruct nativeStruct;

    public CmsGoCB() {
        super("GoCB");
        this.nativeStruct = new NativeStruct();
        setNativeStruct(nativeStruct);
    }

    private void syncToNative() {
        nativeStruct.goEna = goEna ? 1 : 0;
        byte[] idBuf = new byte[130];
        if (goID != null) {
            byte[] src = goID.getBytes();
            System.arraycopy(src, 0, idBuf, 0, Math.min(src.length, 129));
        }
        nativeStruct.goID = idBuf;
        byte[] dsBuf = new byte[256];
        if (datSet != null) {
            byte[] src = datSet.getBytes();
            System.arraycopy(src, 0, dsBuf, 0, Math.min(src.length, 255));
        }
        nativeStruct.datSet = dsBuf;
        nativeStruct.confRev = (int) confRev;
        nativeStruct.ndsCom = ndsCom ? 1 : 0;
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
        goEna = nativeStruct.goEna != 0;
        goID = new String(nativeStruct.goID).trim();
        datSet = new String(nativeStruct.datSet).trim();
        confRev = nativeStruct.confRev & 0xFFFFFFFFL;
        ndsCom = nativeStruct.ndsCom != 0;
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
        CmsFFIDatatypes.INSTANCE.cms_gocb_encode(nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsGoCB decode(byte[] data) {
        CmsGoCB obj = new CmsGoCB();
        CmsFFIDatatypes.INSTANCE.cms_gocb_decode(data, data.length, obj.nativeStruct);
        obj.nativeStruct.read();
        obj.syncFromNative();
        return obj;
    }

    public CmsGoCB copy() {
        CmsGoCB clone = new CmsGoCB();
        clone.goEna = this.goEna;
        clone.goID = this.goID;
        clone.datSet = this.datSet;
        clone.confRev = this.confRev;
        clone.ndsCom = this.ndsCom;
        clone.dstAddr = this.dstAddr != null ? this.dstAddr.clone() : null;
        clone.dstPriority = this.dstPriority;
        clone.dstVid = this.dstVid;
        clone.dstAppId = this.dstAppId;
        return clone;
    }
}
