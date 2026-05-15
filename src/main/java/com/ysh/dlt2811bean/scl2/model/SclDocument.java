package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclDocument {

    public enum SclFileType {
        SCD, ICD, CID, UNKNOWN
    }

    private String xmlns = "http://www.iec.ch/61850/2003/SCL";
    private String xsiSchemaLocation = "http://www.iec.ch/61850/2003/SCL SCL.xsd";
    private SclFileType fileType = SclFileType.UNKNOWN;
    private String originalFilePath;

    private SclHeader header;
    private SclSubstation substation;
    private SclCommunication communication;
    private final List<SclIED> ieds = new ArrayList<>();
    private SclDataTypeTemplates dataTypeTemplates;
    private final List<String> unsupportedElements = new ArrayList<>();

    public void addIed(SclIED ied) { this.ieds.add(ied); }

    public void addUnsupportedElement(String elementName) { unsupportedElements.add(elementName); }

    public boolean hasUnsupportedElements() { return !unsupportedElements.isEmpty(); }

    public SclIED findIedByName(String name) {
        for (SclIED ied : ieds) {
            if (ied.getName().equals(name)) return ied;
        }
        return null;
    }

    public String getDefaultAccessPointReference() {
        if (ieds.isEmpty()) return null;
        SclIED first = ieds.get(0);
        if (first.getAccessPoints().isEmpty()) return null;
        return first.getName() + "." + first.getAccessPoints().get(0).getName();
    }
}
