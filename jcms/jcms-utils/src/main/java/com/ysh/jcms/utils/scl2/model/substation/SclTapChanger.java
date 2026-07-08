package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * 分接头 (TapChanger)，变压器绕组上的分接头调节设备。
 * <p>
 * TapChanger 包含 LNode、SubEquipment 和 EqFunction，
 * 用于描述变压器调压功能及相关的一次/二次设备。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tTapChanger">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="SubEquipment" type="tSubEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="EqFunction" type="tEqFunction" minOccurs="0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString"/>
 *     <xs:attribute name="virtual" type="xs:boolean"/>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclTapChanger {
    /** 分接头名称 (name) */
    private String name;
    /** 描述 (desc) */
    private String desc;
    /** 分接头类型 (type) */
    private String type;
    /** 是否为虚拟设备 (virtual) */
    private Boolean virtual;
    /** 逻辑节点引用列表 (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** 子设备列表 (SubEquipment) */
    private final List<SclSubEquipment> subEquipments = new ArrayList<>();
    /** 设备功能列表 (EqFunction) */
    private final List<SclEqFunction> eqFunctions = new ArrayList<>();

    public SclTapChanger addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }

    public SclTapChanger addSubEquipment(SclSubEquipment subEquipment) { subEquipments.add(subEquipment); return this; }

    public SclTapChanger addEqFunction(SclEqFunction eqFunction) { eqFunctions.add(eqFunction); return this; }
}
