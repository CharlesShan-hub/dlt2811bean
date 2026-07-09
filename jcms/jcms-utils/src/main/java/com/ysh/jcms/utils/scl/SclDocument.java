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

    /** 跨所有 IED 查找包含指定 LDevice inst 的 IED */
    public SclIED findIedByLdInst(String ldInst) {
        for (SclIED ied : ieds) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer server = ap.server();
                if (server != null && server.findLDeviceByInst(ldInst) != null) {
                    return ied;
                }
            }
        }
        return null;
    }
}
