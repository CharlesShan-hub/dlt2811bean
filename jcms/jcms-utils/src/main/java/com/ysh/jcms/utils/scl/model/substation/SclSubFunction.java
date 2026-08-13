package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * Sub-function (SubFunction), the nested child level of Function.
 * <p>
 * SubFunction can be nested recursively, containing LNode, GeneralEquipment,
 * ConductingEquipment as well as more fine-grained SubFunction.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tSubFunction">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="GeneralEquipment" type=
"tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="ConductingEquipment" type=
"tConductingEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="SubFunction" type="tSubFunction" minOccurs=
"0" maxOccurs="unbounded"/>
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
public class SclSubFunction {
    /** Sub-function name (name) */
    private String name;
    /** Description (desc) */
    private String desc;
    /** Sub-function type (type) */
    private String type;
    /** List of logical node references (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** List of general equipment (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** List of conducting equipment (ConductingEquipment) */
    private final List<SclConductingEquipment> conductingEquipments = new ArrayList<>();
    /** List of sub-functions (SubFunction), supporting recursive nesting */
    private final List<SclSubFunction> subFunctions = new ArrayList<>();

    public SclSubFunction addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }

    public SclSubFunction addGeneralEquipment(SclGeneralEquipment generalEquipment) {
        generalEquipments.add(generalEquipment);
        return this;
    }

    public SclSubFunction addConductingEquipment(SclConductingEquipment conductingEquipment) {
        conductingEquipments.add(conductingEquipment);
        return this;
    }

    public SclSubFunction addSubFunction(SclSubFunction subFunction) {
        subFunctions.add(subFunction);
        return this;
    }
}
