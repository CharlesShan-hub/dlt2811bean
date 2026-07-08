package com.ysh.jcms.utils.scl2.model.header;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Text - 61850.6.9.1
 * <p>
 * Schema
 * <pre>{@code
 * <xs:complexType name="tText" mixed="true">
 *     <xs:complexContent mixed="true">
 *         <xs:extension base="tAnyContentFromOtherNamespace">
 *             <xs:attribute name="source" type="xs:anyURI" use="optional"/>
 *         </xs:extension>
 *     </xs:complexContent>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclText {
    /** The text content */
    private String value;
    /** The source URI */
    private String source;
}
