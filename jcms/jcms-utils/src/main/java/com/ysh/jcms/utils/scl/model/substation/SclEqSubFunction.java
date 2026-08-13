package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * Equipment sub-function (EqSubFunction), the nested child level of EqFunction.
 * <p>
 * EqSubFunction can be nested recursively, containing LNode, GeneralEquipment as
 * well as more fine-grained EqSubFunction.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tEqSubFunction">
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
public class SclEqSubFunction {
    /** Equipment sub-function name (name) */
    private String name;
    /** Description (desc) */
    private String desc;
    /** Equipment sub-function type (type) */
    private String type;
    /** List of logical node references (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** List of general equipment (GeneralEquipment) */
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    /** List of equipment sub-functions (EqSubFunction), supporting recursive nesting */
    private final List<SclEqSubFunction> eqSubFunctions = new ArrayList<>();

    public SclEqSubFunction addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }

    public SclEqSubFunction addGeneralEquipment(SclGeneralEquipment generalEquipment) {
        generalEquipments.add(generalEquipment);
        return this;
    }

    public SclEqSubFunction addEqSubFunction(SclEqSubFunction eqSubFunction) {
        eqSubFunctions.add(eqSubFunction);
        return this;
    }
}
