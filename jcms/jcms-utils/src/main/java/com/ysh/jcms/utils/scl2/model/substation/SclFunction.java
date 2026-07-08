package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能 (Function)，描述变电站自动化系统中的一项功能。
 * <p>
 * Function 可以包含 LNode、SubFunction、GeneralEquipment 和 ConductingEquipment，
 * 用于将一次设备和二次功能关联起来。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tFunction">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="SubFunction" type="tSubFunction" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="GeneralEquipment" type="tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="ConductingEquipment" type="tConductingEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString"/>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclFunction {
    /** 功能名称 (name) */
    private String name;
    /** 描述 (desc) */
    private String desc;
    /** 功能类型 (type) */
    private String type;
    /** 逻辑节点引用列表 (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** 子功能列表 (SubFunction) */
    private final List<SclSubFunction> subFunctions = new ArrayList<>();
    /** 通用一次设备列表 (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** 导电设备列表 (ConductingEquipment) */
    private final List<SclConductingEquipment> conductingEquipments = new ArrayList<>();

    public SclFunction addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }

    public SclFunction addSubFunction(SclSubFunction subFunction) { subFunctions.add(subFunction); return this; }

    public SclFunction addGeneralEquipment(SclGeneralEquipment generalEquipment) { generalEquipments.add(generalEquipment); return this; }

    public SclFunction addConductingEquipment(SclConductingEquipment conductingEquipment) { conductingEquipments.add(conductingEquipment); return this; }
}
