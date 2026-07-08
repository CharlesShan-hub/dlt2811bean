package com.ysh.jcms.utils.scl2.model.header;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Hitem - 61850.6.9.1
 * <p>
 * Schema
 * <pre>{@code
 * <xs:complexType name="tHItem" mixed="true">
 *     <xs:complexContent mixed="true">
 *         <xs:extension base="tAnyContentFromOtherNamespace">
 *             <xs:attribute name="version" type="xs:normalizedString" use="required"/>
 *             <xs:attribute name="revision" type="xs:normalizedString" use="required"/>
 *             <xs:attribute name="when" type="xs:normalizedString" use="required"/>
 *             <xs:attribute name="who" type="xs:normalizedString"/>
 *             <xs:attribute name="what" type="xs:normalizedString"/>
 *             <xs:attribute name="why" type="xs:normalizedString"/>
 *         </xs:extension>
 *     </xs:complexContent>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclHitem {
    /** The version of this history entry */
    private String version;
    /** The revision of this history entry */
    private String revision;
    /** Date when the version/revision was released */
    private String when;
    /** Who made/approved this version/revision (who, optional) */
    private String who;
    /** What has been changed since the last approval (what, optional) */
    private String what;
    /** Why the change has happened (why, optional) */
    private String why;
}
