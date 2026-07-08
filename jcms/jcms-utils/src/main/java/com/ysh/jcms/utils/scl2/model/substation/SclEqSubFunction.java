package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备子功能 (EqSubFunction)，EqFunction 的嵌套子级。
 * <p>
 * EqSubFunction 可以递归嵌套，包含 LNode、GeneralEquipment 以及
 * 更细粒度的 EqSubFunction。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tEqSubFunction">
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
@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclEqSubFunction {
    /** 设备子功能名称 (name) */
    private String name;
    /** 描述 (desc) */
    private String desc;
    /** 设备子功能类型 (type) */
    private String type;
    /** 逻辑节点引用列表 (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** 通用一次设备列表 (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** 设备子功能列表 (EqSubFunction)，支持递归嵌套 */
    private final List<SclEqSubFunction> eqSubFunctions = new ArrayList<>();

    public SclEqSubFunction addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }

    public SclEqSubFunction addGeneralEquipment(SclGeneralEquipment generalEquipment) { generalEquipments.add(generalEquipment); return this; }

    public SclEqSubFunction addEqSubFunction(SclEqSubFunction eqSubFunction) { eqSubFunctions.add(eqSubFunction); return this; }
}
