package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.InnerAssociateRequestPDU;
import com.ysh.jcms.data.string.CmsString;

/**
 * Associate-RequestPDU ::= SEQUENCE {
 *     serverAccessPointReference    [0] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,
 *     authenticationParameter       [1] IMPLICIT SEQUENCE { ... } OPTIONAL
 * } — 8.2.1
 */
public class CmsAssociateRequest extends CmsSequence {

    @CmsField(optional = true)
    public CmsString serverAccessPointReference;

    @CmsField(optional = true)
    public CmsAuthenticationParameter authenticationParameter;

    public CmsAssociateRequest() {
        super(new InnerAssociateRequestPDU());
    }

    public CmsAssociateRequest serverAccessPointReference(String v) {
        setPresent("serverAccessPointReference", v != null);
        if (v != null)
            this.serverAccessPointReference.value(v);
        return this;
    }
    public CmsAssociateRequest authenticationParameter(CmsAuthenticationParameter v) {
        this.authenticationParameter = v;
        bindWrapper("authenticationParameter", v);
        setPresent("authenticationParameter", true);
        return this;
    }
}
