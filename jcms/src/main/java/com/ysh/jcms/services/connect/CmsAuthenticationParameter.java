package com.ysh.jcms.services.connect;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * AuthenticationParameter — SEQUENCE used in Associate services.
 *
 * <pre>{@code
 * SEQUENCE {
 *     signatureCertificate    OCTET STRING,
 *     signedTime              UtcTime,
 *     signedValue             OCTET STRING
 * }
 * }</pre>
 */
@Getter
@Accessors(fluent = true)
public class CmsAuthenticationParameter
        extends AbstractCmsCompound<CmsAuthenticationParameter> {

    public static final int MAX_CERT_LEN = 2048;

    public static class NativeStruct extends Structure {
        public byte[] cert = new byte[MAX_CERT_LEN];
        public int cert_len;
        public long signed_time_ms;
        public byte[] sig_val = new byte[MAX_CERT_LEN];
        public int sig_len;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("cert", "cert_len", "signed_time_ms",
                    "sig_val", "sig_len");
        }
    }

    public byte[] cert;
    public int cert_len;
    public long signed_time_ms;
    public byte[] sig_val;
    public int sig_len;

    public CmsAuthenticationParameter() {
        super("AuthenticationParameter");
        nativeStruct = new NativeStruct();
    }

    private void syncToNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        ns.cert = new byte[MAX_CERT_LEN];
        if (cert != null && cert_len > 0) {
            System.arraycopy(cert, 0, ns.cert, 0, Math.min(cert_len, MAX_CERT_LEN));
        }
        ns.cert_len = cert_len;
        ns.signed_time_ms = signed_time_ms;
        ns.sig_val = new byte[MAX_CERT_LEN];
        if (sig_val != null && sig_len > 0) {
            System.arraycopy(sig_val, 0, ns.sig_val, 0, Math.min(sig_len, MAX_CERT_LEN));
        }
        ns.sig_len = sig_len;
    }

    private void syncFromNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        cert_len = ns.cert_len;
        cert = cert_len > 0 ? Arrays.copyOf(ns.cert, cert_len) : new byte[0];
        signed_time_ms = ns.signed_time_ms;
        sig_len = ns.sig_len;
        sig_val = sig_len > 0 ? Arrays.copyOf(ns.sig_val, sig_len) : new byte[0];
    }

    public byte[] encode() {
        syncToNative();
        write();
        byte[] buf = new byte[4096];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_authentication_parameter_encode(
                (NativeStruct) nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsAuthenticationParameter decode(byte[] data) {
        CmsAuthenticationParameter p = new CmsAuthenticationParameter();
        CmsFFIDatatypes.INSTANCE.cms_authentication_parameter_decode(
                data, data.length, p.nativeStruct);
        ((NativeStruct) p.nativeStruct).read();
        p.syncFromNative();
        return p;
    }

    public CmsAuthenticationParameter copy() {
        CmsAuthenticationParameter c = new CmsAuthenticationParameter();
        c.cert = cert != null ? cert.clone() : null;
        c.cert_len = cert_len;
        c.signed_time_ms = signed_time_ms;
        c.sig_val = sig_val != null ? sig_val.clone() : null;
        c.sig_len = sig_len;
        return c;
    }
}
