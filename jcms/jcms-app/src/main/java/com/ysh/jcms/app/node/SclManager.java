package com.ysh.jcms.app.node;

import com.ysh.jcms.utils.scl.model.document.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.reader.SclReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class SclManager {

    private static final Logger log = LoggerFactory.getLogger(SclManager.class);

    private SclDocument document;

    public SclManager load(String filePath) {
        if (filePath == null) {
            log.warn("No SCL file path provided");
            return this;
        }
        String classpathResource = filePath.startsWith("/") ? filePath : filePath;
        InputStream is = getClass().getClassLoader().getResourceAsStream(classpathResource);
        if (is != null) {
            try {
                this.document = new SclReader().read(is);
                log.info("SCL model loaded from classpath: {} (type={}, IEDs={})",
                    classpathResource, document.getFileType(),
                    document.getIeds() != null ? document.getIeds().size() : 0);
                return this;
            } catch (Exception e) {
                log.warn("Failed to load SCL from classpath {}: {}", classpathResource, e.getMessage());
            }
        }
        try {
            this.document = new SclReader().read(filePath);
            log.info("SCL model loaded from file: {} (type={}, IEDs={})",
                filePath, document.getFileType(),
                document.getIeds() != null ? document.getIeds().size() : 0);
        } catch (Exception e) {
            log.warn("Failed to load SCL from {}: {}", filePath, e.getMessage());
            this.document = null;
        }
        return this;
    }

    public SclDocument getDocument() { return document; }

    public SclDataTypeTemplates getDataTypeTemplates() {
        return document != null ? document.getDataTypeTemplates() : null;
    }

    public SclIED findIed(String name) {
        if (document == null) return null;
        return document.findIedByName(name);
    }

    public SclAccessPoint findAccessPoint(String sapRef) {
        if (document == null || sapRef == null) return null;
        int slashIdx = sapRef.indexOf('/');
        String iedName = slashIdx >= 0 ? sapRef.substring(0, slashIdx) : sapRef;
        String apName = slashIdx >= 0 ? sapRef.substring(slashIdx + 1) : "S1";
        SclIED ied = findIed(iedName);
        return ied != null ? ied.findAccessPointByName(apName) : null;
    }

    public SclServer findServer(String sapRef) {
        SclAccessPoint ap = findAccessPoint(sapRef);
        return ap != null ? ap.getServer() : null;
    }

    public List<SclIED> getIeds() {
        return document != null ? document.getIeds() : Collections.emptyList();
    }

    public boolean isLoaded() { return document != null; }
}