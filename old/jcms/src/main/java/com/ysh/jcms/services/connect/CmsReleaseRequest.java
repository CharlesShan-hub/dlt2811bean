package com.ysh.jcms.services.connect;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.services.type.CmsFFIServices;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CmsReleaseRequest extends AbstractCmsCompound<CmsReleaseRequest> {

    public static class NativeStruct extends Structure {
        public byte[] assocId = new byte[64];
        public int assocIdLen;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("assocId", "assocIdLen");
        }
    }

    private NativeStruct ns = new NativeStruct();

    public CmsAssociationId assocId;

    public CmsReleaseRequest() {
        super("ReleaseRequest");
        this.assocId = new CmsAssociationId();
    }

    public CmsReleaseRequest(byte[] assocId) {
        this();
        this.assocId = new CmsAssociationId(assocId);
    }

    public CmsReleaseRequest(CmsAssociationId assocId) {
        this();
        this.assocId = assocId;
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList();
    }

    @Override
    public byte[] encode() {
        syncToNative();
        ns.write();
        PointerByReference outBuf = new PointerByReference();
        LongByReference outLen = new LongByReference();
        CmsFFIServices.INSTANCE.cms_release_request_encode(ns, outBuf, outLen);
        byte[] result = outBuf.getValue().getByteArray(0, (int)outLen.getValue());
        Native.free(Pointer.nativeValue(outBuf.getValue()));
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsReleaseRequest decode(byte[] data) {
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
    }

    @Override
    protected void syncFromNative() {
        int len = Math.min(ns.assocIdLen, CmsAssociationId.MAX_LEN);
        byte[] val = new byte[len];
        System.arraycopy(ns.assocId, 0, val, 0, len);
        assocId.set(val);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return 0; // unused — encode() uses dynamic stream directly
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIServices.INSTANCE.cms_release_request_decode(data, data.length, ns);
        ns.read();
        syncFromNative();
    }

    public static CmsReleaseRequest from(byte[] data) {
        return new CmsReleaseRequest().decode(data);
    }
}
