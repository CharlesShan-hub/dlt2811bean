package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Rated voltage (Voltage), derived from tValueWithUnit, with unit fixed to V (volt).
 * <p>
 * Schema:
 *
 * <pre>
 * {@code
 * <xs:complexType name="tVoltage">
 *     <xs:simpleContent>
 *         <xs:restriction base="tValueWithUnit">
 *             <xs:attribute name="unit" type="xs:token" use="required" fixed=
"V"/>
 *             <xs:attribute name="multiplier" type="tUnitMultiplierEnum" use=
"optional" default=""/>
 *         </xs:restriction>
 *     </xs:simpleContent>
 * </xs:complexType>
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclVoltage {
    /** Voltage value (decimal) */
    private String value;
    /** Unit multiplier (multiplier), e.g. "k" (kilovolt), "M" (megavolt), "" (volt) */
    private String multiplier;
    /** Unit (unit), fixed to "V" */
    private String unit;
}
