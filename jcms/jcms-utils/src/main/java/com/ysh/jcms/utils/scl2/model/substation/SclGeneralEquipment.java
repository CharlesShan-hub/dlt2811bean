package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用一次设备 (GeneralEquipment)，挂载在 Substation 结构的任意层级。
 * <p>
 * 表示无法归入 ConductingEquipment 的其他一次设备，如电池 (BAT)、
 * 交流滤波器 (AXN) 等。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tGeneralEquipment">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="EqFunction" type="tEqFunction" minOccurs="0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString" use="required"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString" use="required"/>
 *     <xs:attribute name="virtual" type="xs:boolean"/>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclGeneralEquipment {
    /** 设备名称 (name) */
    private String name;
    /** 描述 (desc) */
    private String desc;
    /** 设备类型 (type)，例如 "AXN"、"BAT" */
    private String type;
    /** 是否为虚拟设备 (virtual) */
    private Boolean virtual;
    /** 逻辑节点引用列表 (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** 设备功能列表 (EqFunction) */
    private final List<SclEqFunction> eqFunctions = new ArrayList<>();

    public SclGeneralEquipment addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }

    public SclGeneralEquipment addEqFunction(SclEqFunction eqFunction) { eqFunctions.add(eqFunction); return this; }
}
