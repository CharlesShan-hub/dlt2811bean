package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclAssociation {
    /** Associated IED name (iedName) */
    private String iedName;
    /** Logical device instance (ldInst) */
    private String ldInst;
    /** Prefix (prefix) */
    private String prefix;
    /** LN class (lnClass) */
    private String lnClass;
    /** LN instance number (lnInst) */
    private String lnInst;
    /** Association kind (kind): pre-established / predefined */
    private String kind;
    /** Association identifier (associationID) */
    private String associationID;
}
