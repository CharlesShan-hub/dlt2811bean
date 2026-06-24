package com.ysh.jcms.app.handler.connection.associate;

/**
 * Pure data object for Associate-Request parameters.
 *
 * <p>These are the fields a user would input from CLI or config.
 * Certificate and signature are generated internally by the handler.
 */
public class AssociateClientDao {

    /** Server access point reference (e.g. "IED1/AP1") */
    public String sapRef;

    /** Whether to include GM authentication certificate */
    public boolean secure;
}
