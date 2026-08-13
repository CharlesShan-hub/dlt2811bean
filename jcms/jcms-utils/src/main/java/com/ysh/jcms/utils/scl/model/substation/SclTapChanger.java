package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * Tap changer (TapChanger), the tap-adjusting device on a transformer winding.
 * <p>
 * TapChanger contains LNode, SubEquipment and EqFunction, describing the
 * transformer voltage-regulation function and the related primary/secondary
 * equipment.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tTapChanger">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="SubEquipment" type="tSubEquipment" minOccurs=
"0" maxOccurs="unbounded"/>
 *         <xs:element name="EqFunction" type="tEqFunction" minOccurs=
"0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString"/>
 *     <xs:attribute name="virtual" type="xs:boolean"/>
 * </xs:complexType>
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclTapChanger {
    /** Tap changer name (name) */
    private String name;
    /** Description (desc) */
    private String desc;
    /** Tap changer type (type) */
    private String type;
    /** Whether it is a virtual device (virtual) */
    private Boolean virtual;
    /** List of logical node references (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** List of sub-equipment (SubEquipment) */
    private final List<SclSubEquipment> subEquipments = new ArrayList<>();
    /** List of equipment functions (EqFunction) */
    private final List<SclEqFunction> eqFunctions = new ArrayList<>();

    public SclTapChanger addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }

    public SclTapChanger addSubEquipment(SclSubEquipment subEquipment) {
        subEquipments.add(subEquipment);
        return this;
    }

    public SclTapChanger addEqFunction(SclEqFunction eqFunction) {
        eqFunctions.add(eqFunction);
        return this;
    }
}
