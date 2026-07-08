package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * 过程 (Process)，描述变电站自动化系统中的过程层级。
 * <p>
 * Process 是 Substation 结构之外的最高层级元素，可以包含 LNode、
 * GeneralEquipment、Function、ConductingEquipment、Substation、
 * Line 以及递归的 Process。
 * <p>
 * Schema:
 * <pre>{@code
 * <xs:complexType name="tProcess">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="GeneralEquipment" type="tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="Function" type="tFunction" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="ConductingEquipment" type="tConductingEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="Substation" type="tSubstation" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="Line" type="tLine" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="Process" type="tProcess" minOccurs="0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString"/>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclProcess {
    /** 过程名称 (name) */
    private String name;
    /** 描述 (desc) */
    private String desc;
    /** 过程类型 (type) */
    private String type;
    /** 逻辑节点引用列表 (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** 通用一次设备列表 (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** 功能列表 (Function) */
    private final List<SclFunction> functions = new ArrayList<>();
    /** 导电设备列表 (ConductingEquipment) */
    private final List<SclConductingEquipment> conductingEquipments = new ArrayList<>();
    /** 变电站列表 (Substation) */
    private final List<SclSubstation> substations = new ArrayList<>();
    /** 线路列表 (Line) */
    private final List<SclLine> lines = new ArrayList<>();
    /** 子过程列表 (Process)，支持递归嵌套 */
    private final List<SclProcess> processes = new ArrayList<>();

    public SclProcess addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }

    public SclProcess addGeneralEquipment(SclGeneralEquipment generalEquipment) { generalEquipments.add(generalEquipment); return this; }

    public SclProcess addFunction(SclFunction function) { functions.add(function); return this; }

    public SclProcess addConductingEquipment(SclConductingEquipment conductingEquipment) { conductingEquipments.add(conductingEquipment); return this; }

    public SclProcess addSubstation(SclSubstation substation) { substations.add(substation); return this; }

    public SclProcess addLine(SclLine line) { lines.add(line); return this; }

    public SclProcess addProcess(SclProcess process) { processes.add(process); return this; }
}
