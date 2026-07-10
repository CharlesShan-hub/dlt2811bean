package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 额定电压 (Voltage)，继承自 tValueWithUnit，单位固定为 V（伏特）。
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
    /** 电压数值 (decimal) */
    private String value;
    /** 单位乘数 (multiplier), 如 "k"(千伏)、"M"(兆伏)、""(伏) */
    private String multiplier;
    /** 单位 (unit), 固定为 "V" */
    private String unit;
}
