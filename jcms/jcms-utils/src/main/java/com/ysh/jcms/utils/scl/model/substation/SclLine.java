package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * Line, a transmission line between substations or a connection line within a substation.
 * <p>
 * Line may contain LNode, GeneralEquipment, Function, ConductingEquipment and
 * ConnectivityNode, and is associated with Voltage information.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tLine">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="GeneralEquipment" type=
"tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="Function" type="tFunction" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="ConductingEquipment" type=
"tConductingEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="ConnectivityNode" type=
"tConnectivityNode" minOccurs="0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString"/>
 *     <xs:attribute name="nomFreq" type="xs:normalizedString"/>
 *     <xs:attribute name="numPhases" type="xs:normalizedString"/>
 * </xs:complexType>
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclLine {
    /** Line name (name) */
    private String name;
    /** Description (desc) */
    private String desc;
    /** Line type (type) */
    private String type;
    /** Rated frequency (nomFreq) */
    private String nomFreq;
    /** Number of phases (numPhases) */
    private Integer numPhases;
    /** Associated voltage level (Voltage) */
    private SclVoltage voltage;
    /** List of logical node references (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** List of general equipment (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** List of functions (Function) */
    private final List<SclFunction> functions = new ArrayList<>();
    /** List of conducting equipment (ConductingEquipment) */
    private final List<SclConductingEquipment> conductingEquipments = new ArrayList<>();
    /** List of connectivity nodes (ConnectivityNode) */
    private final List<SclConnectivityNode> connectivityNodes = new ArrayList<>();

    public SclLine addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }

    public SclLine addGeneralEquipment(SclGeneralEquipment generalEquipment) {
        generalEquipments.add(generalEquipment);
        return this;
    }

    public SclLine addFunction(SclFunction function) {
        functions.add(function);
        return this;
    }

    public SclLine addConductingEquipment(SclConductingEquipment conductingEquipment) {
        conductingEquipments.add(conductingEquipment);
        return this;
    }

    public SclLine addConnectivityNode(SclConnectivityNode connectivityNode) {
        connectivityNodes.add(connectivityNode);
        return this;
    }
}
