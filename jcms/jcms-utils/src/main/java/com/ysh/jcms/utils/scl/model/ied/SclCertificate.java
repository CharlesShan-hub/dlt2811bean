package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * AccessPoint 的 {@code <GOOSESecurity>} / {@code <SMVSecurity>} 证书（tCertificate
 * / tCert）。
 * <p>
 * 扁平化建模：Subject / IssuerName 两个 tCert 子元素的 commonName + idHierarchy 直接作为字段。
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
