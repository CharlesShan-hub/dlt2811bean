package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.connection.CmsAuthenticationParameter;
import com.ysh.jcms.pdu.connection.CmsAssociateRequest;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Pure data object for Associate-Request parameters.
 *
 * <p>
 * These are the fields a user would input from CLI or config. Certificate and
 * signature are generated internally by the handler.
 */
@Setter
@Getter
@Accessors(fluent = true)
public class AssociateDao extends BaseDao {

    /** Server access point reference (e.g. "IED1/AP1") */
    private String sapRef;

    /** Whether to include GM authentication certificate */
    private boolean secure;

    /** Authentication parameter (built internally by AssociateClient) */
    private CmsAuthenticationParameter authParam;

    @Override
    public CmsType toRequest() {
        CmsAssociateRequest req = new CmsAssociateRequest();
        if (sapRef != null && !sapRef.isEmpty()) {
            req.serverAccessPointReference(sapRef);
        }
        if (authParam != null) {
            req.authenticationParameter(authParam);
        }
        return req;
    }
}
