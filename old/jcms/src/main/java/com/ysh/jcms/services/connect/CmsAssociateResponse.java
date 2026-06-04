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
public class CmsAssociateResponse extends AbstractCmsCompound<CmsAssociateResponse> {

    public static class NativeStruct extends Structure {
        public byte[] assocId = new byte[64];
        public int assocIdLen;
        public int serviceError;
        public int hasAuth;
        public byte[] cert = new byte[2048];
        public int certLen;
        public long signedTime;
        public byte[] sigVal = new byte[2048];
        public int sigLen;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("assocId", "assocIdLen", "serviceError", "hasAuth",
                "cert", "certLen", "signedTime", "sigVal", "sigLen");
        }
    }

    private NativeStruct ns = new NativeStruct();

    public CmsAssociationId assocId;
    public int serviceError;
    public int hasAuth;
    public byte[] cert;
    public long signedTime;
    public byte[] sigVal;

    public CmsAssociateResponse() {
        super("AssociateResponse");
        this.assocId = new CmsAssociationId();
    }

    public CmsAssociateResponse(byte[] assocId, int serviceError, boolean hasAuthenticationParameter,
                                byte[] cert, long signedTime, byte[] sigVal) {
        this();
        this.assocId = new CmsAssociationId(assocId);
        this.serviceError = serviceError;
        this.hasAuth = hasAuthenticationParameter ? 1 : 0;
        this.cert = cert != null ? cert : new byte[0];
        this.signedTime = signedTime;
        this.sigVal = sigVal != null ? sigVal : new byte[0];
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
    public CmsAssociateResponse decode(byte[] data) {
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
        ns.hasAuth = hasAuth;
        if (cert != null) {
            ns.certLen = Math.min(cert.length, ns.cert.length);
            System.arraycopy(cert, 0, ns.cert, 0, ns.certLen);
        }
        ns.signedTime = signedTime;
        if (sigVal != null) {
            ns.sigLen = Math.min(sigVal.length, ns.sigVal.length);
            System.arraycopy(sigVal, 0, ns.sigVal, 0, ns.sigLen);
        }
    }

    @Override
    protected void syncFromNative() {
        int idLen = Math.min(ns.assocIdLen, CmsAssociationId.MAX_LEN);
        byte[] idVal = new byte[idLen];
        System.arraycopy(ns.assocId, 0, idVal, 0, idLen);
        assocId.set(idVal);
        serviceError = ns.serviceError;
        hasAuth = ns.hasAuth;
        cert = ns.certLen > 0 ? Arrays.copyOf(ns.cert, ns.certLen) : new byte[0];
        signedTime = ns.signedTime;
        sigVal = ns.sigLen > 0 ? Arrays.copyOf(ns.sigVal, ns.sigLen) : new byte[0];
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIServices.INSTANCE.cms_associate_response_encode(ns, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIServices.INSTANCE.cms_associate_response_decode(data, data.length, ns);
        ns.read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 4096;
    }

    public static CmsAssociateResponse from(byte[] data) {
        return new CmsAssociateResponse().decode(data);
    }
}
