package com.ysh.jcms.app.handler.connection.associate;

import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Pure data object for Associate-Request parameters.
 *
 * <p>These are the fields a user would input from CLI or config.
 * Certificate and signature are generated internally by the handler.
 */
@Setter
@Getter
@Accessors(fluent = true)
public class AssociateClientDao {

    /** Server access point reference (e.g. "IED1/AP1") */
    private String sapRef;

    /** Whether to include GM authentication certificate */
    private boolean secure;
}
