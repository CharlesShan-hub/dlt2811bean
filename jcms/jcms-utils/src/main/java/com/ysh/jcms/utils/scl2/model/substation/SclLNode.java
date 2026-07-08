package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 逻辑节点引用 (LNode)，挂载在 Substation 结构的任意层级。
 * <p>
 * 用于将 SA 系统功能关联到一次设备上。在 SSD 文件中功能尚未分配到 IED，
 * 在 SCD 文件中则指向具体的 IED 中的 LN。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tLNode">
 *     <xs:attribute name="ldInst" type="xs:normalizedString"/>
 *     <xs:attribute name="lnClass" type="xs:normalizedString" use="required"/>
 *     <xs:attribute name="lnInst" type="xs:normalizedString"/>
 *     <xs:attribute name="iedName" type="xs:normalizedString"/>
 *     <xs:attribute name="prefix" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclLNode {
    /** 逻辑设备实例 (ldInst) */
    private String ldInst;
    /** 逻辑节点类 (lnClass) */
    private String lnClass;
    /** 逻辑节点实例号 (lnInst) */
    private String lnInst;
    /** 逻辑节点类型 (lnType) */
    private String lnType;
    /** 所属 IED 名称 (iedName) */
    private String iedName;
    /** 前缀 (prefix) */
    private String prefix;
    /** 描述 (desc) */
    private String desc;
}
