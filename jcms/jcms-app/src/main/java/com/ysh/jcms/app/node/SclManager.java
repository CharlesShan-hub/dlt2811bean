package com.ysh.jcms.app.node;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.reader.SclReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class SclManager {

    private static final Logger log = LoggerFactory.getLogger(SclManager.class);

    private SclDocument document;
    private String source;

    public SclManager load(String filePath) {
        if (filePath == null) {
            log.warn("No SCL file path provided");
            return this;
        }
        if (isAbsolutePath(filePath)) {
            return loadFromFile(filePath);
        }
        String classpathResource = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        InputStream is = getClass().getClassLoader().getResourceAsStream(classpathResource);
        if (is != null) {
            try {
                this.document = new SclReader().read(is);
                this.source = "classpath:" + classpathResource;
                log.info("SCL model loaded from classpath: {} (type={}, IEDs={})", classpathResource, document.fileType(),
                        document.ieds().size());
                return this;
            } catch (Exception e) {
                log.warn("Failed to load SCL from classpath {}: {}", classpathResource, e.getMessage());
            }
        }
        return loadFromFile(filePath);
    }

    public String source() {
        return source;
    }

    private boolean isAbsolutePath(String path) {
        if (path.length() >= 3 && Character.isLetter(path.charAt(0)) && path.charAt(1) == ':'
                && (path.charAt(2) == '\\' || path.charAt(2) == '/')) {
            return true;
        }
        return path.startsWith("/");
    }

    private SclManager loadFromFile(String filePath) {
        try {
            this.document = new SclReader().read(filePath);
            this.source = filePath;
            log.info("SCL model loaded from file: {} (type={}, IEDs={})", filePath, document.fileType(), document.ieds().size());
        } catch (Exception e) {
            log.warn("Failed to load SCL from {}: {}", filePath, e.getMessage());
            this.document = null;
        }
        return this;
    }

    public SclDocument document() {
        return document;
    }
    public java.util.List<com.ysh.jcms.utils.scl.model.ied.SclIED> ieds() {
        return document != null ? document.ieds() : java.util.Collections.emptyList();
    }

    public boolean loaded() {
        return document != null;
    }
}
