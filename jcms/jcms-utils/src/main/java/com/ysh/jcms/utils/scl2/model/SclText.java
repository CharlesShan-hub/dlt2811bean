package com.ysh.jcms.utils.scl2.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 可复用的文本元素 (tText)，用于需要文本描述的所有元素。
 * <p>
 * Schema:
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
    /** 文本内容 (mixed content) */
    private String value;
    /** 引用外部文件的URI (source) */
    private String source;
}
