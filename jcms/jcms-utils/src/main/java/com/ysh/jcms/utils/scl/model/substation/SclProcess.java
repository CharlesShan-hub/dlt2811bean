package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * Process, describing the process level in a substation automation system.
 * <p>
 * Process is the top-level element outside the Substation structure, and may
 * contain LNode, GeneralEquipment, Function, ConductingEquipment, Substation,
 * Line as well as recursive Process.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tProcess">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="GeneralEquipment" type=
"tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="Function" type="tFunction" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="ConductingEquipment" type=
"tConductingEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="Substation" type="tSubstation" minOccurs=
"0" maxOccurs="unbounded"/>
 *         <xs:element name="Line" type="tLine" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="Process" type="tProcess" minOccurs="0" maxOccurs=
"unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString"/>
 * </xs:complexType>
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclProcess {
    /** Process name (name) */
    private String name;
    /** Description (desc) */
    private String desc;
    /** Process type (type) */
    private String type;
    /** List of logical node references (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** List of general equipment (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** List of functions (Function) */
    private final List<SclFunction> functions = new ArrayList<>();
    /** List of conducting equipment (ConductingEquipment) */
    private final List<SclConductingEquipment> conductingEquipments = new ArrayList<>();
    /** List of substations (Substation) */
    private final List<SclSubstation> substations = new ArrayList<>();
    /** List of lines (Line) */
    private final List<SclLine> lines = new ArrayList<>();
    /** List of sub-processes (Process), supporting recursive nesting */
    private final List<SclProcess> processes = new ArrayList<>();

    public SclProcess addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }

    public SclProcess addGeneralEquipment(SclGeneralEquipment generalEquipment) {
        generalEquipments.add(generalEquipment);
        return this;
    }

    public SclProcess addFunction(SclFunction function) {
        functions.add(function);
        return this;
    }

    public SclProcess addConductingEquipment(SclConductingEquipment conductingEquipment) {
        conductingEquipments.add(conductingEquipment);
        return this;
    }

    public SclProcess addSubstation(SclSubstation substation) {
        substations.add(substation);
        return this;
    }

    public SclProcess addLine(SclLine line) {
        lines.add(line);
        return this;
    }

    public SclProcess addProcess(SclProcess process) {
        processes.add(process);
        return this;
    }
}
