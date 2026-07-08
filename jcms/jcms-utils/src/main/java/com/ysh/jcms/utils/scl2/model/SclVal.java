package com.ysh.jcms.utils.scl2.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 数据属性值元素 (Val)，用于 DAI 或类似元素的数值表示。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tVal">
 *     <xs:simpleContent>
 *         <xs:extension base="xs:normalizedString">
 *             <xs:attribute name="sGroup" type="xs:unsignedInt" use="optional"/>
 *         </xs:extension>
 *     </xs:simpleContent>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclVal {
    /** 数值字符串 (normalizedString) */
    private String value;
    /** 定值组编号 (sGroup), 用于定值组服务 */
    private Integer sGroup;
}
