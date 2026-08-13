package com.ysh.jcms.utils.scl.navigate;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.*;
import com.ysh.jcms.utils.scl.model.instance.*;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference navigator — SclRef → model elements.
 * <p>
 * Core entry: {@link #go(SclDocument, SclRef)}. All lookup operations ultimately locate model elements via SclRef.
 */
public class Navigator {

    private final SclDocument document;
    private final SclIED ied;
    private final SclLDevice ld;
    private final SclLN ln;
    private final SclDOI doi;
    private final SclSDI sdi;
    private final SclDAI dai;
    private final SclRef ref;

    private Navigator(SclDocument document, SclIED ied, SclLDevice ld, SclLN ln, SclDOI doi, SclSDI sdi, SclDAI dai, SclRef ref) {
        this.document = document;
        this.ied = ied;
        this.ld = ld;
        this.ln = ln;
        this.doi = doi;
        this.sdi = sdi;
        this.dai = dai;
        this.ref = ref;
    }

    // ==================== Core entry ====================

    /** Core method: navigate to a model element by SclRef */
    public static Navigator go(SclDocument document, SclRef ref) {
        if (document == null || ref == null)
            return empty();

        // Find the IED
        String iedName = ref.iedName();
        if (iedName == null)
            return empty(); // SclRef must contain an IED name
        SclIED ied = document.ied(iedName);
        if (ied == null)
            return empty();

        return navigate(ied, ref, document);
    }

    /** Navigate by reference string (convenience method) */
    public static Navigator go(SclDocument document, String ref) {
        if (document == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return go(document, SclRefParser.parse(ref));
    }

    /** Navigate by SclRef within the specified IED */
    public static Navigator go(SclIED ied, SclRef ref) {
        if (ied == null || ref == null)
            return empty();
        return navigate(ied, ref, null);
    }

    /** Navigate by string within the specified IED */
    public static Navigator go(SclIED ied, String ref) {
        if (ied == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return navigate(ied, SclRefParser.parse(ref), null);
    }

    /** Navigate by string within the document + specified IED (with dataTypeTemplates access capability) */
    public static Navigator go(SclDocument doc, SclIED ied, String ref) {
        if (doc == null || ied == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return navigate(ied, SclRefParser.parse(ref), doc);
    }

    /** AP scope: navigate by string within the document + specified AP */
    public static Navigator go(SclDocument doc, SclAccessPoint ap, String ref) {
        if (doc == null || ap == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return navigate(ap, SclRefParser.parse(ref), doc);
    }

    /** AP scope: navigate by string within the specified IED + AP */
    public static Navigator go(SclIED ied, SclAccessPoint ap, String ref) {
        if (ied == null || ap == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return navigate(ap, SclRefParser.parse(ref), null);
    }

    // ==================== Navigation logic ====================

    private static Navigator navigate(SclIED ied, SclRef sclRef, SclDocument doc) {
        // Find the LDevice
        SclLDevice ld = findLd(ied, sclRef.ldInst());
        if (ld == null)
            return empty();

        // Find the LN
        SclLN ln = findLn(ld, sclRef.lnName());
        if (ln == null)
            return empty();

        // LN level
        if (sclRef.isLnLevel()) {
            return new Navigator(doc, ied, ld, ln, null, null, null, sclRef);
        }

        // DO level and above: find the DOI
        SclDOI doi = ln.findDoiByName(sclRef.doName());
        if (doi == null) {
            // The DO may be defined in the template but not in the instance (e.g. Beh) → return a Navigator without
            // DOI so downstream falls back to template lookup
            return new Navigator(doc, ied, ld, ln, null, null, null, sclRef);
        }

        if (sclRef.isDoLevel()) {
            return new Navigator(doc, ied, ld, ln, doi, null, null, sclRef);
        }

        // DA level: walk the SDI chain → DAI
        SclSDI currentSdi = null;
        boolean sdiFound = true;
        for (String sdiName : sclRef.sdiChain()) {
            SclSDI next = (currentSdi == null) ? doi.findSdiByName(sdiName) : currentSdi.findSdiByName(sdiName);
            if (next == null) {
                sdiFound = false;
                break;
            }
            currentSdi = next;
        }

        if (!sdiFound) {
            // SDI not found in the instance → may be an SDO-level reference (template-level SDO without an instance
            // SDI); return a partial Navigator (with DOI, without SDI/DAI) so downstream falls back to template lookup
            return new Navigator(doc, ied, ld, ln, doi, null, null, sclRef);
        }

        SclDAI dai = (currentSdi != null) ? currentSdi.findDaiByName(sclRef.daName()) : doi.findDaiByName(sclRef.daName());
        if (dai == null) {
            // DAI not found in the instance → return a partial Navigator (with DOI, without DAI) so downstream falls
            // back to template lookup
            return new Navigator(doc, ied, ld, ln, doi, currentSdi, null, sclRef);
        }

        return new Navigator(doc, ied, ld, ln, doi, currentSdi, dai, sclRef);
    }

    /** AP-scoped navigation: only looks up LD under the specified AP. */
    private static Navigator navigate(SclAccessPoint ap, SclRef sclRef, SclDocument doc) {
        SclLDevice ld = findLd(ap, sclRef.ldInst());
        if (ld == null)
            return empty();

        SclLN ln = findLn(ld, sclRef.lnName());
        if (ln == null)
            return empty();

        if (sclRef.isLnLevel()) {
            return new Navigator(doc, null, ld, ln, null, null, null, sclRef);
        }

        SclDOI doi = ln.findDoiByName(sclRef.doName());
        if (doi == null) {
            // The DO may be defined in the template but not in the instance (e.g. Beh) → return a Navigator without
            // DOI so downstream falls back to template lookup
            return new Navigator(doc, null, ld, ln, null, null, null, sclRef);
        }

        if (sclRef.isDoLevel()) {
            return new Navigator(doc, null, ld, ln, doi, null, null, sclRef);
        }

        SclSDI currentSdi = null;
        boolean sdiFound = true;
        for (String sdiName : sclRef.sdiChain()) {
            SclSDI next = (currentSdi == null) ? doi.findSdiByName(sdiName) : currentSdi.findSdiByName(sdiName);
            if (next == null) {
                sdiFound = false;
                break;
            }
            currentSdi = next;
        }

        if (!sdiFound) {
            return new Navigator(doc, null, ld, ln, doi, null, null, sclRef);
        }

        SclDAI dai = (currentSdi != null) ? currentSdi.findDaiByName(sclRef.daName()) : doi.findDaiByName(sclRef.daName());
        if (dai == null) {
            return new Navigator(doc, null, ld, ln, doi, currentSdi, null, sclRef);
        }

        return new Navigator(doc, null, ld, ln, doi, currentSdi, dai, sclRef);
    }

    private static SclLDevice findLd(SclIED ied, String ldInst) {
        // Use SclIED's lazy LD index (first access O(AP×LD), afterwards O(1))
        return ied.lDevice(ldInst);
    }

    /** AP-scoped lookup: only finds LD under the specified AP. */
    public static SclLDevice findLd(SclAccessPoint ap, String ldInst) {
        SclServer server = ap.server();
        if (server != null) {
            return server.findLDeviceByInst(ldInst);
        }
        return null;
    }

    private static SclLN findLn(SclLDevice ld, String lnName) {
        for (SclLN ln : ld.lns()) {
            if (ln.getFullName().equals(lnName))
                return ln;
        }
        return null;
    }

    /**
     * Resolves a logical node list by LD name or LN reference (AP scope).
     *
     * @param ied
     *            the IED object
     * @param ap
     *            the currently associated access point (when non-null, lookup is limited to this AP)
     * @param ldName
     *            the LD name (when non-empty, returns all LNs under this LD)
     * @param lnReference
     *            the LN reference (LD/LN format, used when ldName is empty)
     * @return the LN list, or null when not found
     */
    public static List<SclLN> resolveLns(SclIED ied, SclAccessPoint ap, String ldName, String lnReference) {
        List<SclLN> result = new ArrayList<>();
        if (ldName != null && !ldName.isEmpty()) {
            SclLDevice device = findLd(ap, ldName);
            if (device != null) {
                result.addAll(device.lns());
                return result;
            }
            return null;
        }
        if (lnReference == null || lnReference.isEmpty() || !SclRefParser.isValid(lnReference))
            return null;
        SclRef sclRef = SclRefParser.parse(lnReference);
        SclLDevice device = findLd(ap, sclRef.ldInst());
        if (device != null) {
            SclLN ln = findLn(device, sclRef.lnName());
            if (ln != null) {
                result.add(ln);
                return result;
            }
        }
        return null;
    }

    /**
     * Resolves a logical node list by LD name or LN reference (across all APs).
     *
     * @param ied
     *            the IED object
     * @param ldName
     *            the LD name (when non-empty, returns all LNs under this LD)
     * @param lnReference
     *            the LN reference (LD/LN format, used when ldName is empty)
     * @return the LN list, or null when not found
     */
    public static List<SclLN> resolveLns(SclIED ied, String ldName, String lnReference) {
        List<SclLN> result = new ArrayList<>();
        if (ldName != null && !ldName.isEmpty()) {
            SclLDevice device = findLd(ied, ldName);
            if (device != null) {
                result.addAll(device.lns());
                return result;
            }
            return null;
        }
        if (lnReference == null || lnReference.isEmpty() || !SclRefParser.isValid(lnReference))
            return null;
        SclRef sclRef = SclRefParser.parse(lnReference);
        SclLDevice device = findLd(ied, sclRef.ldInst());
        if (device != null) {
            SclLN ln = findLn(device, sclRef.lnName());
            if (ln != null) {
                result.add(ln);
                return result;
            }
        }
        return null;
    }

    /**
     * Reverse lookup: finds the LD instance name containing the given LN in the specified IED.
     *
     * @param ied
     *            the IED object
     * @param ln
     *            the LN to look up
     * @return the LD inst value, or null when not found
     */
    public static String findLdInst(SclIED ied, SclLN ln) {
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer server = ap.server();
            if (server != null) {
                for (SclLDevice ld : server.lDevices()) {
                    if (ld.findLnByFullName(ln.getFullName()) != null)
                        return ld.inst();
                }
            }
        }
        return null;
    }

    private static Navigator empty() {
        return new Navigator(null, null, null, null, null, null, null, null);
    }

    // ==================== State ====================

    public boolean isValid() {
        return ln != null;
    }
    public boolean hasDoi() {
        return doi != null;
    }
    public boolean hasSdi() {
        return sdi != null;
    }
    public boolean hasDai() {
        return dai != null;
    }

    // ==================== Accessors ====================

    public SclDocument document() {
        return document;
    }
    public SclIED ied() {
        return ied;
    }
    public SclLDevice ld() {
        return ld;
    }
    public SclLN ln() {
        return ln;
    }
    public SclDOI doi() {
        return doi;
    }
    public SclSDI sdi() {
        return sdi;
    }
    public SclDAI dai() {
        return dai;
    }
    public SclRef ref() {
        return ref;
    }

    public String daiValue() {
        if (dai == null || dai.vals().isEmpty())
            return null;
        return dai.vals().get(0).value();
    }
}
