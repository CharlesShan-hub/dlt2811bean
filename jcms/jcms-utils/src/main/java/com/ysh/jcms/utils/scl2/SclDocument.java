package com.ysh.jcms.utils.scl2;

import com.ysh.jcms.utils.scl2.model.header.SclHeader;
import com.ysh.jcms.utils.scl2.model.substation.SclSubstation;
import com.ysh.jcms.utils.scl2.model.communication.SclCommunication;
import com.ysh.jcms.utils.scl2.model.ied.SclIED;
import com.ysh.jcms.utils.scl2.model.template.SclDataTypeTemplates;
import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclDocument {
    public enum SclFileType { SCD, ICD, CID, UNKNOWN }

    private String xmlns = "http://www.iec.ch/61850/2006/SCL";
    private String xsiSchemaLocation = "SCL.xsd";
    private SclFileType fileType = SclFileType.UNKNOWN;
    private String originalFilePath;
    private SclHeader header;
    private SclSubstation substation;
    private SclCommunication communication;
    private final List<SclIED> ieds = new ArrayList<>();
    private SclDataTypeTemplates dataTypeTemplates;
    private final List<String> unsupportedElements = new ArrayList<>();

    public SclDocument addIed(SclIED ied) { ieds.add(ied); return this; }
    public SclDocument addUnsupportedElement(String element) { unsupportedElements.add(element); return this; }
    public boolean hasUnsupportedElements() { return !unsupportedElements.isEmpty(); }
    public SclIED findIedByName(String name) {
        return ieds.stream().filter(i -> name.equals(i.name())).findFirst().orElse(null);
    }
}
