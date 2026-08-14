package com.ysh.jcms.utils.scl.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Reusable text element (tText), used by all elements that require a text
 * description.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tText" mixed="true">
 *     <xs:complexContent mixed="true">
 *         <xs:extension base="tAnyContentFromOtherNamespace">
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
public class SclText {
    /** Text content (mixed content) */
    private String value;
    /** URI referencing an external file (source) */
    private String source;
}
