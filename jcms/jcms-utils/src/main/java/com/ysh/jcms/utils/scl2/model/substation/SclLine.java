package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * 线路 (Line)，变电站之间的输电线路或变电站内部的连接线。
 * <p>
 * Line 可以包含 LNode、GeneralEquipment、Function、ConductingEquipment
 * 和 ConnectivityNode，并关联 Voltage 信息。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tLine">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="GeneralEquipment" type="tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="Function" type="tFunction" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="ConductingEquipment" type="tConductingEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="ConnectivityNode" type="tConnectivityNode" minOccurs="0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString"/>
 *     <xs:attribute name="nomFreq" type="xs:normalizedString"/>
 *     <xs:attribute name="numPhases" type="xs:normalizedString"/>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclLine {
    /** 线路名称 (name) */
    private String name;
    /** 描述 (desc) */
    private String desc;
    /** 线路类型 (type) */
    private String type;
    /** 额定频率 (nomFreq) */
    private String nomFreq;
    /** 相数 (numPhases) */
    private Integer numPhases;
    /** 关联的电压等级 (Voltage) */
    private SclVoltage voltage;
    /** 逻辑节点引用列表 (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** 通用一次设备列表 (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** 功能列表 (Function) */
    private final List<SclFunction> functions = new ArrayList<>();
    /** 导电设备列表 (ConductingEquipment) */
    private final List<SclConductingEquipment> conductingEquipments = new ArrayList<>();
    /** 连接点列表 (ConnectivityNode) */
    private final List<SclConnectivityNode> connectivityNodes = new ArrayList<>();

    public SclLine addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }

    public SclLine addGeneralEquipment(SclGeneralEquipment generalEquipment) { generalEquipments.add(generalEquipment); return this; }

    public SclLine addFunction(SclFunction function) { functions.add(function); return this; }

    public SclLine addConductingEquipment(SclConductingEquipment conductingEquipment) { conductingEquipments.add(conductingEquipment); return this; }

    public SclLine addConnectivityNode(SclConnectivityNode connectivityNode) { connectivityNodes.add(connectivityNode); return this; }
}
