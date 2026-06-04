package com.ysh.jcms.datatypes2.svc.other;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.data.extended.CmsUtcTime;

import java.util.Arrays;
import java.util.List;

/**
 * cms_authentication_parameter_t — Authentication Parameter.
 *
 * C: typedef struct {
 *     uint8_t        cert[2048];
 *     int            cert_len;
 *     cms_utc_time_t signed_time;
 *     uint8_t        sig_val[2048];
 *     int            sig_len;
 * } cms_authentication_parameter_t;
 */
public class CmsAuthenticationParameter extends Structure {
    public byte[] cert = new byte[2048];
    public int certLen;
    public CmsUtcTime signedTime = new CmsUtcTime();
    public byte[] sigVal = new byte[2048];
    public int sigLen;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("cert", "certLen", "signedTime", "sigVal", "sigLen");
    }
}
