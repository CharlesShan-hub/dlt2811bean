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
public class CmsReleaseResponse extends AbstractCmsCompound<CmsReleaseResponse> {

    public static class NativeStruct extends Structure {
        public byte[] assocId = new byte[64];
        public int assocIdLen;
        public int serviceError;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("assocId", "assocIdLen", "serviceError");
        }
    }

    private NativeStruct ns = new NativeStruct();

    public CmsAssociationId assocId;
    public int serviceError;

    public CmsReleaseResponse() {
        super("ReleaseResponse");
        this.assocId = new CmsAssociationId();
    }

    public CmsReleaseResponse(byte[] assocId, int serviceError) {
        this();
        this.assocId = new CmsAssociationId(assocId);
        this.serviceError = serviceError;
    }

    public CmsReleaseResponse(CmsAssociationId assocId, int serviceError) {
        this();
        this.assocId = assocId;
        this.serviceError = serviceError;
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
    public CmsReleaseResponse decode(byte[] data) {
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
        ns.serviceError = serviceError;
    }

    @Override
    protected void syncFromNative() {
        int len = Math.min(ns.assocIdLen, CmsAssociationId.MAX_LEN);
        byte[] val = new byte[len];
        System.arraycopy(ns.assocId, 0, val, 0, len);
        assocId.set(val);
        serviceError = ns.serviceError;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIServices.INSTANCE.cms_release_response_encode(ns, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIServices.INSTANCE.cms_release_response_decode(data, data.length, ns);
        ns.read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 128;
    }

    public static CmsReleaseResponse from(byte[] data) {
        return new CmsReleaseResponse().decode(data);
    }
}
