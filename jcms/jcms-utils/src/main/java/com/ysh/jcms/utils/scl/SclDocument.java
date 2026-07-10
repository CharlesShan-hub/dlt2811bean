package com.ysh.jcms.utils.scl;

import com.ysh.jcms.utils.scl.model.header.SclHeader;
import com.ysh.jcms.utils.scl.model.substation.SclSubstation;
import com.ysh.jcms.utils.scl.model.communication.SclCommunication;
import com.ysh.jcms.utils.scl.model.ied.*;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclDocument {
    public enum SclFileType {
        SCD, ICD, CID, UNKNOWN
    }

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

    public SclDocument addIed(SclIED ied) {
        ieds.add(ied);
        return this;
    }
    public SclDocument addUnsupportedElement(String element) {
        unsupportedElements.add(element);
        return this;
    }
    public boolean hasUnsupportedElements() {
        return !unsupportedElements.isEmpty();
    }
    public SclIED ied(String name) {
        return ieds.stream().filter(i -> name.equals(i.name())).findFirst().orElse(null);
    }

    /** Collect all logical device instance names across all IEDs. */
    public List<String> ldNames() {
        return ieds.stream().flatMap(ied -> ied.lDevices().stream()).map(SclLDevice::inst).collect(java.util.stream.Collectors.toList());
    }
}
