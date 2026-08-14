package com.ysh.jcms.utils.scl.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Vendor private extension element (Private), which may appear in all elements
 * derived from tBaseElement.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tPrivate" mixed="true">
 *     <xs:complexContent mixed="true">
 *         <xs:extension base="tAnyContentFromOtherNamespace">
 *             <xs:attribute name="type" use="required"/>
 *             <xs:attribute name="source" type="xs:anyURI" use="optional"/>
 *         </xs:extension>
 *     </xs:complexContent>
 * </xs:complexType>
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclPrivate {
    /** Private type identifier (type, required) */
    private String type;
    /** URI referencing an external file (source, optional) */
    private String source;
    /** Private content text */
    private String value;
}
