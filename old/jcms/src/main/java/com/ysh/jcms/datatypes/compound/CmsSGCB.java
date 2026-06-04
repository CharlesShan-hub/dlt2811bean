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
        public CmsUtcTime tActEdt = new CmsUtcTime();
        public short resvTms;
        public int resvTms_present;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("numOfSG", "actSG", "editSG", "tActEdt",
                    "resvTms", "resvTms_present");
        }
    }

    private NativeStruct ns = new NativeStruct();

    public int numOfSG;
    public int actSG;
    public int editSG;
    public CmsUtcTime tActEdt;
    public Integer resvTms;      // null if not present

    public CmsSGCB() {
        super("SGCB");
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
    public CmsSGCB decode(byte[] data) {
        ffiDecode(data);
        ns.read();
        syncFromNative();
        return this;
    }

    @Override
    protected void syncToNative() {
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

    @Override
    protected void syncFromNative() {
        numOfSG = ns.numOfSG & 0xFF;
        actSG = ns.actSG & 0xFF;
        editSG = ns.editSG & 0xFF;
        tActEdt = new CmsUtcTime();
        tActEdt.seconds_since_epoch = ns.tActEdt.seconds_since_epoch;
        tActEdt.fraction_of_second = ns.tActEdt.fraction_of_second;
        tActEdt.time_quality = ns.tActEdt.time_quality;
        resvTms = ns.resvTms_present != 0 ? (ns.resvTms & 0xFFFF) : null;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_sgcb_encode(ns, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_sgcb_decode(data, data.length, ns);
        ns.read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    public static CmsSGCB from(byte[] data) {
        return new CmsSGCB().decode(data);
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
