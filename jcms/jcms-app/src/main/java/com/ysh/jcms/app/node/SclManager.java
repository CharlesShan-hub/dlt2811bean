package com.ysh.jcms.app.node;

import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.conformance.SclConformanceCheck;
import com.ysh.jcms.utils.scl.conformance.SclConformanceIssue;
import com.ysh.jcms.utils.scl.conformance.SclConformanceMode;
import com.ysh.jcms.utils.scl.conformance.SclConformanceSeverity;
import com.ysh.jcms.utils.scl.reader.SclReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class SclManager {

    private static final Logger log = LoggerFactory.getLogger(SclManager.class);

    private SclDocument document;
    private String source;
    private SclConformanceMode conformanceMode = SclConformanceMode.LOOSE;
    private List<SclConformanceIssue> conformanceIssues = Collections.emptyList();

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
                runConformanceCheck();
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
            runConformanceCheck();
        } catch (Exception e) {
            log.warn("Failed to load SCL from {}: {}", filePath, e.getMessage());
            this.document = null;
        }
        return this;
    }

    /**
     * Runs the Q/GDW 1396 conformance check when the configured mode is STRICT.
     * <p>
     * Findings are cached for later querying via {@link #conformanceIssues()}
     * and logged once per load; LOOSE mode keeps the historical behaviour.
     */
    private void runConformanceCheck() {
        this.conformanceMode = SclConformanceMode
                .from(CmsConfigLoader.load().scl().conformanceMode());
        if (document == null || conformanceMode == SclConformanceMode.LOOSE) {
            this.conformanceIssues = Collections.emptyList();
            return;
        }
        this.conformanceIssues = SclConformanceCheck.check(document, conformanceMode);
        int errors = 0;
        for (SclConformanceIssue issue : conformanceIssues) {
            if (issue.severity() == SclConformanceSeverity.ERROR) {
                errors++;
                log.warn("[Q/GDW 1396 {}] {} | clause {} | ref {}", issue.severity(), issue.message(), issue.clause(),
                        issue.ref());
            } else {
                log.info("[Q/GDW 1396 {}] {} | clause {} | ref {}", issue.severity(), issue.message(), issue.clause(),
                        issue.ref());
            }
        }
        log.info("Q/GDW 1396 conformance check: {} findings ({} errors, {} warnings)",
                conformanceIssues.size(), errors, conformanceIssues.size() - errors);
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

    /** Conformance mode used for the last load (LOOSE by default). */
    public SclConformanceMode conformanceMode() {
        return conformanceMode;
    }

    /** Findings of the last conformance check (empty in LOOSE mode). */
    public List<SclConformanceIssue> conformanceIssues() {
        return conformanceIssues;
    }
}
