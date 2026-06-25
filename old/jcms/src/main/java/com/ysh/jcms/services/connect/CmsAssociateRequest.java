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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CmsAssociateRequest extends AbstractCmsCompound<CmsAssociateRequest> {

    public static class NativeStruct extends Structure {
        public byte[] sapRef = new byte[65];
        public int hasAuth;
        public byte[] cert = new byte[2048];
        public int certLen;
        public long signedTime;
        public byte[] sigVal = new byte[2048];
        public int sigLen;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("sapRef", "hasAuth", "cert", "certLen", "signedTime", "sigVal", "sigLen");
        }
    }

    private NativeStruct ns = new NativeStruct();

    public byte[] sapRef;
    public int hasAuth;
    public byte[] cert;
    public long signedTime;
    public byte[] sigVal;

    public CmsAssociateRequest() {
        super("AssociateRequest");
    }

    public CmsAssociateRequest(String serverAccessPointReference, boolean hasAuthenticationParameter,
                               byte[] cert, long signedTime, byte[] sigVal) {
        this();
        byte[] sapBytes = serverAccessPointReference.getBytes(StandardCharsets.UTF_8);
        this.sapRef = Arrays.copyOf(sapBytes, sapBytes.length + 1);
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
        PointerByReference outBuf = new PointerByReference();
        LongByReference outLen = new LongByReference();
        CmsFFIServices.INSTANCE.cms_associate_request_encode(ns, outBuf, outLen);
        byte[] result = outBuf.getValue().getByteArray(0, (int)outLen.getValue());
        Native.free(Pointer.nativeValue(outBuf.getValue()));
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsAssociateRequest decode(byte[] data) {
        ffiDecode(data);
        ns.read();
        syncFromNative();
        return this;
    }

    @Override
    protected void syncToNative() {
        if (sapRef != null) {
            int len = Math.min(sapRef.length, ns.sapRef.length);
            System.arraycopy(sapRef, 0, ns.sapRef, 0, len);
            if (len < ns.sapRef.length) ns.sapRef[len] = 0;
        }
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
        int sapLen = 0;
        while (sapLen < ns.sapRef.length && ns.sapRef[sapLen] != 0) sapLen++;
        sapRef = Arrays.copyOf(ns.sapRef, sapLen);
        hasAuth = ns.hasAuth;
        cert = ns.certLen > 0 ? Arrays.copyOf(ns.cert, ns.certLen) : new byte[0];
        signedTime = ns.signedTime;
        sigVal = ns.sigLen > 0 ? Arrays.copyOf(ns.sigVal, ns.sigLen) : new byte[0];
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return 0; // unused — encode() uses dynamic stream directly
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIServices.INSTANCE.cms_associate_request_decode(data, data.length, ns);
        ns.read();
        syncFromNative();
    }

    public static CmsAssociateRequest from(byte[] data) {
        return new CmsAssociateRequest().decode(data);
    }
}
