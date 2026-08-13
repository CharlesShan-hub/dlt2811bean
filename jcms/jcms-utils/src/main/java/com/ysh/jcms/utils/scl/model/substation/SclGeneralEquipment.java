package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * General equipment (GeneralEquipment), mounted at any level of the Substation structure.
 * <p>
 * Represents other primary equipment that cannot be classified under
 * ConductingEquipment, such as batteries (BAT), AC filters (AXN), etc.
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tGeneralEquipment">
 *     <xs:sequence>
 *         <xs:element name="LNode" type="tLNode" minOccurs="0" maxOccurs=
"unbounded"/>
 *         <xs:element name="EqFunction" type="tEqFunction" minOccurs=
"0" maxOccurs="unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name="name" type="xs:normalizedString" use="required"/>
 *     <xs:attribute name="desc" type="xs:normalizedString"/>
 *     <xs:attribute name="type" type="xs:normalizedString" use="required"/>
 *     <xs:attribute name="virtual" type="xs:boolean"/>
 * </xs:complexType>
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclGeneralEquipment {
    /** Equipment name (name) */
    private String name;
    /** Description (desc) */
    private String desc;
    /** Equipment type (type), e.g. "AXN", "BAT" */
    private String type;
    /** Whether it is a virtual device (virtual) */
    private Boolean virtual;
    /** List of logical node references (LNode) */
    private final List<SclLNode> lNodes = new ArrayList<>();
    /** List of equipment functions (EqFunction) */
    private final List<SclEqFunction> eqFunctions = new ArrayList<>();

    public SclGeneralEquipment addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }

    public SclGeneralEquipment addEqFunction(SclEqFunction eqFunction) {
        eqFunctions.add(eqFunction);
        return this;
    }
}
