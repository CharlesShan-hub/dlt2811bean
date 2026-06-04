package com.ysh.jcms.services.connect;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.services.type.CmsFFIServices;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CmsAbort extends AbstractCmsCompound<CmsAbort> {

    public static class NativeStruct extends Structure {
        public byte[] assocId = new byte[64];
        public int assocIdLen;
        public int reason;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("assocId", "assocIdLen", "reason");
        }
    }

    private NativeStruct ns = new NativeStruct();

    public CmsAssociationId assocId;
    public int reason;

    public CmsAbort() {
        super("Abort");
        this.assocId = new CmsAssociationId();
    }

    public CmsAbort(byte[] assocId, int reason) {
        this();
        this.assocId = new CmsAssociationId(assocId);
        this.reason = reason;
    }

    public CmsAbort(CmsAssociationId assocId, int reason) {
        this();
        this.assocId = assocId;
        this.reason = reason;
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
    public CmsAbort decode(byte[] data) {
        ffiDecode(data);
        ns.read();
        syncFromNative();
        return this;
    }

    @Override
    protected void syncToNative() {
        byte[] val = assocId.get();
        int len = Math.min(val.length, CmsAssociationId.MAX_LEN);
        System.arraycopy(val, 0, ns.assocId, 0, len);
        ns.assocIdLen = len;
        ns.reason = reason;
    }

    @Override
    protected void syncFromNative() {
        int len = Math.min(ns.assocIdLen, CmsAssociationId.MAX_LEN);
        byte[] val = new byte[len];
        System.arraycopy(ns.assocId, 0, val, 0, len);
        assocId.set(val);
        reason = ns.reason;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIServices.INSTANCE.cms_abort_encode(ns, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIServices.INSTANCE.cms_abort_decode(data, data.length, ns);
        ns.read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 128;
    }

    public static CmsAbort from(byte[] data) {
        return new CmsAbort().decode(data);
    }
}
