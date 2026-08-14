package com.ysh.jcms.utils.scl.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Data attribute value element (Val), representing the value of DAI or similar
 * elements.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tVal">
 *     <xs:simpleContent>
 *         <xs:extension base="xs:normalizedString">
 *             <xs:attribute name="sGroup" type="xs:unsignedInt" use=
"optional"/>
 *         </xs:extension>
 *     </xs:simpleContent>
 * </xs:complexType>
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclVal {
    /** Numeric string (normalizedString) */
    private String value;
    /** Setting group number (sGroup), used by the setting group service */
    private Integer sGroup;
}
