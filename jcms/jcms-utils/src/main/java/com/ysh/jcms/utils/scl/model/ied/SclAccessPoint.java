package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclAccessPoint {

    private String name;
    private Boolean router = false;
    private Boolean clock = false;
    private Boolean kdc = false;
    private SclServer server;
    private SclServerAt serverAt;

    /** {@code <GOOSESecurity>} 证书（tAccessPoint，maxOccurs=7）。 */
    private final List<SclCertificate> gooseSecurity = new ArrayList<>();
    /** {@code <SMVSecurity>} 证书（tAccessPoint，maxOccurs=7）。 */
    private final List<SclCertificate> smvSecurity = new ArrayList<>();

    public SclAccessPoint addGooseSecurity(SclCertificate cert) {
        this.gooseSecurity.add(cert);
        return this;
    }

    public SclAccessPoint addSmvSecurity(SclCertificate cert) {
        this.smvSecurity.add(cert);
        return this;
    }
}
