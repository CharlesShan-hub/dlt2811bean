package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * 子功能 (SubFunction)，Function 的嵌套子级。
 * <p>
 * SubFunction 可以递归嵌套，包含 LNode、GeneralEquipment、ConductingEquipment
 * 以及更细粒度的 SubFunction。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tSubFunction">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="GeneralEquipment" type="tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="ConductingEquipment" type="tConductingEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="SubFunction" type="tSubFunction" minOccurs="0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString"/>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclSubFunction {
    /** 子功能名称 (name) */
    private String name;
    /** 描述 (desc) */
    private String desc;
    /** 子功能类型 (type) */
    private String type;
    /** 逻辑节点引用列表 (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** 通用一次设备列表 (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** 导电设备列表 (ConductingEquipment) */
    private final List<SclConductingEquipment> conductingEquipments = new ArrayList<>();
    /** 子功能列表 (SubFunction)，支持递归嵌套 */
    private final List<SclSubFunction> subFunctions = new ArrayList<>();

    public SclSubFunction addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }

    public SclSubFunction addGeneralEquipment(SclGeneralEquipment generalEquipment) { generalEquipments.add(generalEquipment); return this; }

    public SclSubFunction addConductingEquipment(SclConductingEquipment conductingEquipment) { conductingEquipments.add(conductingEquipment); return this; }

    public SclSubFunction addSubFunction(SclSubFunction subFunction) { subFunctions.add(subFunction); return this; }
}
