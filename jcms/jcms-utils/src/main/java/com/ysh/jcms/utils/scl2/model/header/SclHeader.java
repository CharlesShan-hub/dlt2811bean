package com.ysh.jcms.utils.scl2.model.header;

import com.ysh.jcms.utils.scl2.model.SclText;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

/**
 * Header - 61850.6.9.1
 * <p>
 * Schema
 * <pre>{@code
 * <xs:complexType name="tHeader">
 *     <xs:sequence>
 *         <xs:element name="Text" type="tText" minOccurs="0"/>
 *         <xs:element name="History" minOccurs="0">
 *             <xs:complexType>
 *                 <xs:sequence>
 *                     <xs:element name="Hitem" type="tHItem" maxOccurs="unbounded"/>
 *                 </xs:sequence>
 *             </xs:complexType>
 *         </xs:element>
 *     </xs:sequence>
 *     <xs:attribute name="id" type="xs:normalizedString" use="required"/>
 *     <xs:attribute name="version" type="xs:normalizedString"/>
 *     <xs:attribute name="revision" type="xs:normalizedString" default=""/>
 *     <xs:attribute name="toolID" type="xs:normalizedString"/>
 *     <xs:attribute name="nameStructure" use="optional" default="IEDName">
 *         <xs:simpleType>
 *             <xs:restriction base="xs:Name">
 *                 <xs:enumeration value="IEDName"/>
 *             </xs:restriction>
 *         </xs:simpleType>
 *     </xs:attribute>
 * </xs:complexType>
 * }</pre>
 */
@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclHeader {
    /** A string identifying this SCL file, mandatory (can be empty) */
    private String id;
    /** The project specific version of this SCL configuration file
     *  (can be empty, if only one version exists) */
    private String version;
    /** The project specific revision of this SCL configuration file, 
     * by default the empty string meaning the original before any 
     * revision / change. */
    private String revision;
    /** The manufacturer specific identification of the tool that was 
     * used to create the SCL file */
    private String toolId;
    /** Element provided optional only for backward compatibility with 
     * previous SCL schema version. If given at all, only the IEDName 
     * value is allowed */
    private String nameStructure;
    /** The Text element is optional */
    private SclText text;
    /** The revision history is optional */
    private final List<SclHitem> history = new ArrayList<>();

    public SclHeader addHitem(SclHitem hitem) { history.add(hitem); return this; }
}
