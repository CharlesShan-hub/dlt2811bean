package com.ysh.dlt2811bean.scl;

import com.ysh.dlt2811bean.scl.model.SclHeader;
import com.ysh.dlt2811bean.scl.model.SclSubstation;
import com.ysh.dlt2811bean.scl.model.SclCommunication;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import java.util.ArrayList;
import java.util.List;

public class SclDocument {

    public enum SclFileType {
        SCD,   // Substation Configuration Description — 全站配置文件
        ICD,   // IED Capability Description — 装置能力描述文件
        CID,   // Configured IED Description — 已配置的 IED 描述文件
        UNKNOWN
    }

    private String xmlns = "http://www.iec.ch/61850/2006/SCL";
    private String xsiSchemaLocation = "http://www.iec.ch/61850/2006/SCL SCL.xsd";

    private SclHeader header;
    private SclSubstation substation;
    private SclCommunication communication;
    private List<SclIED> ieds = new ArrayList<>();
    private SclDataTypeTemplates dataTypeTemplates;

    private SclFileType fileType = SclFileType.UNKNOWN;
    private String originalFilePath;

    public SclDocument() {
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getXsiSchemaLocation() {
        return xsiSchemaLocation;
    }

    public void setXsiSchemaLocation(String xsiSchemaLocation) {
        this.xsiSchemaLocation = xsiSchemaLocation;
    }

    public SclHeader getHeader() {
        return header;
    }

    public void setHeader(SclHeader header) {
        this.header = header;
    }

    public SclSubstation getSubstation() {
        return substation;
    }

    public void setSubstation(SclSubstation substation) {
        this.substation = substation;
    }

    public SclCommunication getCommunication() {
        return communication;
    }

    public void setCommunication(SclCommunication communication) {
        this.communication = communication;
    }

    public List<SclIED> getIeds() {
        return ieds;
    }

    public void setIeds(List<SclIED> ieds) {
        this.ieds = ieds;
    }

    public void addIed(SclIED ied) {
        this.ieds.add(ied);
    }

    public SclDataTypeTemplates getDataTypeTemplates() {
        return dataTypeTemplates;
    }

    public void setDataTypeTemplates(SclDataTypeTemplates dataTypeTemplates) {
        this.dataTypeTemplates = dataTypeTemplates;
    }

    public SclIED findIedByName(String name) {
        for (SclIED ied : ieds) {
            if (ied.getName().equals(name)) {
                return ied;
            }
        }
        return null;
    }

    /**
     * Returns the default server access point reference ("IEDName.AccessPoint").
     * Uses the first IED and its first AccessPoint.
     *
     * @return the default reference, or null if no IED or AccessPoint exists
     */
    public String getDefaultAccessPointReference() {
        if (ieds.isEmpty()) {
            return null;
        }
        SclIED firstIed = ieds.get(0);
        List<SclIED.SclAccessPoint> aps = firstIed.getAccessPoints();
        if (aps.isEmpty()) {
            return null;
        }
        return firstIed.getName() + "." + aps.get(0).getName();
    }

    public SclFileType getFileType() {
        return fileType;
    }

    public void setFileType(SclFileType fileType) {
        this.fileType = fileType;
    }

    public String getOriginalFilePath() {
        return originalFilePath;
    }

    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
    }
}
