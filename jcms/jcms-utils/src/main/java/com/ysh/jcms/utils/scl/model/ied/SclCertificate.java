package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * The {@code <GOOSESecurity>} / {@code <SMVSecurity>} certificates of
 * AccessPoint (tCertificate / tCert).
 * <p>
 * Flattened modeling: the commonName + idHierarchy of the two tCert child
 * elements Subject / IssuerName are exposed directly as fields.
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclCertificate {

    private String xferNumber;
    private String serialNumber;

    private String subjectCommonName;
    private String subjectIdHierarchy;

    private String issuerCommonName;
    private String issuerIdHierarchy;
}
