package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.connection.CmsAuthenticationParameter;
import com.ysh.jcms.core.pdu.connection.CmsAssociateRequest;
import java.util.Objects;
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
        Objects.requireNonNull(sapRef, "sapRef must not be null");
        Objects.requireNonNull(authParam, "authParam must not be null");
        return new CmsAssociateRequest()
            .authenticationParameter(authParam)
            .serverAccessPointReference(sapRef);
    }
}
