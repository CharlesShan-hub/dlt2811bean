package com.ysh.dlt2811bean.scl.model;

public class SclHeader {

    private String id;
    private String version;
    private String revision;
    private String toolId;
    private String nameStructure;

    public SclHeader() {
    }

    public SclHeader(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getNameStructure() {
        return nameStructure;
    }

    public void setNameStructure(String nameStructure) {
        this.nameStructure = nameStructure;
    }
}
