package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * Function, describing a function in the substation automation system.
 * <p>
 * Function may contain LNode, SubFunction, GeneralEquipment and
 * ConductingEquipment, used to associate primary equipment with secondary
 * functions.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tFunction">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="SubFunction" type="tSubFunction" minOccurs=
"0" maxOccurs="unbounded"/>
 *         <xs:element name="GeneralEquipment" type=
"tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="ConductingEquipment" type=
"tConductingEquipment" minOccurs="0" maxOccurs="unbounded"/>
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
public class SclFunction {
    /** Function name (name) */
    private String name;
    /** Description (desc) */
    private String desc;
    /** Function type (type) */
    private String type;
    /** List of logical node references (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** List of sub-functions (SubFunction) */
    private final List<SclSubFunction> subFunctions = new ArrayList<>();
    /** List of general equipment (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** List of conducting equipment (ConductingEquipment) */
    private final List<SclConductingEquipment> conductingEquipments = new ArrayList<>();

    public SclFunction addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }

    public SclFunction addSubFunction(SclSubFunction subFunction) {
        subFunctions.add(subFunction);
        return this;
    }

    public SclFunction addGeneralEquipment(SclGeneralEquipment generalEquipment) {
        generalEquipments.add(generalEquipment);
        return this;
    }

    public SclFunction addConductingEquipment(SclConductingEquipment conductingEquipment) {
        conductingEquipments.add(conductingEquipment);
        return this;
    }
}
