package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * Equipment function (EqFunction), a function definition mounted under GeneralEquipment or TapChanger.
 * <p>
 * EqFunction may contain LNode, GeneralEquipment and EqSubFunction, describing
 * the specific automation functions carried by the primary equipment.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tEqFunction">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="GeneralEquipment" type=
"tGeneralEquipment" minOccurs="0" maxOccurs="unbounded"/>
 *         <xs:element name="EqSubFunction" type="tEqSubFunction" minOccurs=
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
public class SclEqFunction {
    /** Equipment function name (name) */
    private String name;
    /** Description (desc) */
    private String desc;
    /** Equipment function type (type) */
    private String type;
    /** List of logical node references (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** List of general equipment (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** List of equipment sub-functions (EqSubFunction) */
    private final List<SclEqSubFunction> eqSubFunctions = new ArrayList<>();

    public SclEqFunction addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }

    public SclEqFunction addGeneralEquipment(SclGeneralEquipment generalEquipment) {
        generalEquipments.add(generalEquipment);
        return this;
    }

    public SclEqFunction addEqSubFunction(SclEqSubFunction eqSubFunction) {
        eqSubFunctions.add(eqSubFunction);
        return this;
    }
}
