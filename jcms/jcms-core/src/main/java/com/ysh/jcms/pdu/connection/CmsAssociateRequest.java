package com.ysh.jcms.pdu.connection;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAssociateRequestPDU;
import com.ysh.jcms.data.scalar.CmsString;
import com.ysh.jcms.data.sequence.connection.CmsAuthenticationParameter;

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
        if (v != null) {
            this.authenticationParameter.value(v);
            setPresent("authenticationParameter", true);
        } else {
            setPresent("authenticationParameter", false);
        }
        return this;
    }
}
