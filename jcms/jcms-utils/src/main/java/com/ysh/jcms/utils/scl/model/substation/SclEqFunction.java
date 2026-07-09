package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备功能 (EqFunction)，挂载在 GeneralEquipment 或 TapChanger 下的功能定义。
 * <p>
 * EqFunction 可以包含 LNode、GeneralEquipment 和 EqSubFunction，
 * 用于描述一次设备所承载的具体自动化功能。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tEqFunction">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="GeneralEquipment" type="tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="EqSubFunction" type="tEqSubFunction" minOccurs="0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString"/>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclEqFunction {
    /** 设备功能名称 (name) */
    private String name;
    /** 描述 (desc) */
    private String desc;
    /** 设备功能类型 (type) */
    private String type;
    /** 逻辑节点引用列表 (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** 通用一次设备列表 (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** 设备子功能列表 (EqSubFunction) */
    private final List<SclEqSubFunction> eqSubFunctions = new ArrayList<>();

    public SclEqFunction addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }

    public SclEqFunction addGeneralEquipment(SclGeneralEquipment generalEquipment) { generalEquipments.add(generalEquipment); return this; }

    public SclEqFunction addEqSubFunction(SclEqSubFunction eqSubFunction) { eqSubFunctions.add(eqSubFunction); return this; }
}
