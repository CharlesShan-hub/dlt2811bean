package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Logical node reference (LNode), mounted at any level of the Substation
 * structure.
 * <p>
 * Used to associate SA system functions with primary equipment. In SSD files
 * the functions are not yet assigned to an IED, while in SCD files they point
 * to the LN in a specific IED.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tLNode">
 *     <xs:attribute name="ldInst" type="xs:normalizedString"/>
 *     <xs:attribute name="lnClass" type="xs:normalizedString" use="required"/>
 *     <xs:attribute name="lnInst" type="xs:normalizedString"/>
 *     <xs:attribute name="iedName" type="xs:normalizedString"/>
 *     <xs:attribute name="prefix" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 * </xs:complexType>
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclLNode {
    /** Logical device instance (ldInst) */
    private String ldInst;
    /** Logical node class (lnClass) */
    private String lnClass;
    /** Logical node instance number (lnInst) */
    private String lnInst;
    /** Logical node type (lnType) */
    private String lnType;
    /** Name of the IED this LN belongs to (iedName) */
    private String iedName;
    /** Prefix (prefix) */
    private String prefix;
    /** Description (desc) */
    private String desc;
}
