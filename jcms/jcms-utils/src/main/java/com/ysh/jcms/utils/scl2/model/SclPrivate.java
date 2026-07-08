package com.ysh.jcms.utils.scl2.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 供应商私有扩展元素 (Private)，可出现在所有继承自 tBaseElement 的元素中。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tPrivate" mixed="true">
 *     <xs:complexContent mixed="true">
 *         <xs:extension base="tAnyContentFromOtherNamespace">
 *             <xs:attribute name="type" use="required"/>
 *             <xs:attribute name="source" type="xs:anyURI" use="optional"/>
 *         </xs:extension>
 *     </xs:complexContent>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclPrivate {
    /** 私有类型标识 (type, required) */
    private String type;
    /** 引用外部文件的 URI (source, optional) */
    private String source;
    /** 私有内容文本 */
    private String value;
}
