package com.ysh.jcms.services.connect;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;
import com.ysh.jcms.per.types.PerOctetString;
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

    @Override
    protected void syncToNative() {
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

    @Override
    protected void syncFromNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        cert_len = ns.cert_len;
        cert = cert_len > 0 ? Arrays.copyOf(ns.cert, cert_len) : new byte[0];
        signed_time_ms = ns.signed_time_ms;
        sig_len = ns.sig_len;
        sig_val = sig_len > 0 ? Arrays.copyOf(ns.sig_val, sig_len) : new byte[0];
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_authentication_parameter_encode(
                (NativeStruct) nativeStruct, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_authentication_parameter_decode(
                data, data.length, nativeStruct);
        ((NativeStruct) nativeStruct).read();
        syncFromNative();
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        byte[] certBytes = cert != null ? cert : new byte[0];
        PerOctetString.encodeConstrained(pos, certBytes, 0, MAX_CERT_LEN);
        PerInteger.encodeUnconstrained(pos, signed_time_ms);
        byte[] sigBytes = sig_val != null ? sig_val : new byte[0];
        PerOctetString.encodeConstrained(pos, sigBytes, 0, MAX_CERT_LEN);
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        cert = PerOctetString.decodeConstrained(pis, 0, MAX_CERT_LEN);
        cert_len = cert != null ? cert.length : 0;
        signed_time_ms = PerInteger.decodeUnconstrained(pis);
        sig_val = PerOctetString.decodeConstrained(pis, 0, MAX_CERT_LEN);
        sig_len = sig_val != null ? sig_val.length : 0;
    }

    public static CmsAuthenticationParameter from(byte[] data) {
        return new CmsAuthenticationParameter().decode(data);
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
