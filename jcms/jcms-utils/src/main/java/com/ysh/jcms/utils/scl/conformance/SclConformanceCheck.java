package com.ysh.jcms.utils.scl.conformance;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.communication.SclAddress;
import com.ysh.jcms.utils.scl.model.communication.SclCommunication;
import com.ysh.jcms.utils.scl.model.communication.SclConnectedAP;
import com.ysh.jcms.utils.scl.model.communication.SclGSE;
import com.ysh.jcms.utils.scl.model.communication.SclSMV;
import com.ysh.jcms.utils.scl.model.communication.SclSubNetwork;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.SclVal;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Q/GDW 1396-2012 conformance checker for SCL documents.
 * <p>
 * Pure static utility (no state), in the same style as the service layer. The
 * checker walks the parsed {@link SclDocument} model and produces
 * {@link SclConformanceIssue} findings grouped by rule family:
 * <ul>
 * <li>R1 naming — LD instance names (§7.1.3), SubNetwork names (§6.5.1), LN
 * prefixes (Appendix I)</li>
 * <li>R2 structure — minimal LN set per LD (§7.1.1), GOOSE/SV access-point
 * separation (§7.1.2), IED nameplate and Chinese descriptions (§6.2), data set
 * scope (§7.1.3 / §7.2.2)</li>
 * <li>R3 communication parameters — APPID / VLAN-ID ranges and typical GSE
 * timings (§6.5.2 / §6.5.3)</li>
 * </ul>
 * Severity follows the standard wording: hard "shall/not allowed (应/不应/必须)"
 * rules produce ERROR; "should (宜)" and typical-value rules produce WARN;
 * informative Appendix I suggestions produce INFO.
 * <p>
 * LOOSE mode returns no findings at all - the historical international-standard
 * behaviour is untouched.
 */
public final class SclConformanceCheck {

    private static final String CAT_LD_NAMING = "LD-NAMING";
    private static final String CAT_SUB_NETWORK = "SUB-NETWORK";
    private static final String CAT_LN_PREFIX = "LN-PREFIX";
    private static final String CAT_STRUCTURE = "STRUCTURE";
    private static final String CAT_DATASET = "DATASET";
    private static final String CAT_IED_INFO = "IED-INFO";
    private static final String CAT_COMM_PARAM = "COMM-PARAM";
    private static final String CAT_LN_TEMPLATE = "LN-TEMPLATE";

    private SclConformanceCheck() {
    }

    /**
     * Checks a document in STRICT mode.
     *
     * @param doc
     *            parsed SCL document
     * @return list of findings (empty in LOOSE mode / for null documents)
     */
    public static List<SclConformanceIssue> check(SclDocument doc) {
        return check(doc, SclConformanceMode.STRICT);
    }

    /**
     * Checks a document under the given mode.
     *
     * @param doc
     *            parsed SCL document
     * @param mode
     *            LOOSE skips all checks; STRICT runs the full rule set
     * @return list of findings, never null
     */
    public static List<SclConformanceIssue> check(SclDocument doc, SclConformanceMode mode) {
        List<SclConformanceIssue> issues = new ArrayList<>();
        if (doc == null || mode == null || mode == SclConformanceMode.LOOSE) {
            return issues;
        }
        checkLdInstNaming(doc, issues);
        checkSubNetworkNaming(doc, issues);
        checkLnPrefix(doc, issues);
        checkLdMinLn(doc, issues);
        checkRequiredDo(doc, issues);
        checkDataSetFc(doc, issues);
        checkDataSetNotCrossLd(doc, issues);
        checkAccessPointSeparation(doc, issues);
        checkIedBasicInfo(doc, issues);
        checkGseAppId(doc, issues);
        checkSmvAppId(doc, issues);
        checkVlanId(doc, issues);
        checkGseMinMaxTime(doc, issues);
        return issues;
    }

    // ==================== R1 naming ====================

    /**
     * §7.1.3 - LD instance name must be one of the ten reserved names (or + 2-digit
     * suffix).
     */
    private static void checkLdInstNaming(SclDocument doc, List<SclConformanceIssue> issues) {
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv == null)
                    continue;
                for (SclLDevice ld : srv.lDevices()) {
                    String inst = ld.inst();
                    if (!GwLdInst.isAllowed(inst)) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_LD_NAMING).clause("7.1.3")
                                .ref(ied.name() + "/" + inst)
                                .message("LD instance '" + inst + "' is not a Q/GDW 1396 logical device name (allowed: "
                                        + GwLdInst.allowedNames() + ", optionally with a 2-digit suffix)"));
                    }
                }
            }
        }
    }

    /**
     * §6.5.1 - SubNetwork should be named Subnetwork_Stationbus /
     * Subnetwork_Processbus.
     */
    private static void checkSubNetworkNaming(SclDocument doc, List<SclConformanceIssue> issues) {
        SclCommunication comm = doc.communication();
        if (comm == null)
            return;
        for (SclSubNetwork sn : comm.subNetworks()) {
            if (!GwSubNetwork.isAllowed(sn.name())) {
                issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.WARN).category(CAT_SUB_NETWORK).clause("6.5.1")
                        .ref(sn.name()).message("SubNetwork '" + sn.name() + "' deviates from the recommended Q/GDW 1396 names (allowed: "
                                + GwSubNetwork.allowedNames() + ")"));
            }
        }
    }

    /**
     * Appendix I (informative) - LN prefix should follow the
     * functional-abbreviation examples.
     */
    private static void checkLnPrefix(SclDocument doc, List<SclConformanceIssue> issues) {
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv == null)
                    continue;
                for (SclLDevice ld : srv.lDevices()) {
                    for (SclLN ln : ld.lns()) {
                        String prefix = ln.prefix();
                        if (!GwLnPrefix.matches(prefix)) {
                            issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.INFO).category(CAT_LN_PREFIX)
                                    .clause("Appendix I").ref(ied.name() + "/" + ld.inst() + "/" + ln.getFullName()).message("LN prefix '"
                                            + prefix + "' does not match any Q/GDW 1396 Appendix I example (e.g. CB, QG, PctDif, Lin)"));
                        }
                    }
                }
            }
        }
    }

    // ==================== R2 structure ====================

    /** §7.1.1 - every LD must contain LLN0, LPHD and at least 3 LNs in total. */
    private static void checkLdMinLn(SclDocument doc, List<SclConformanceIssue> issues) {
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv == null)
                    continue;
                for (SclLDevice ld : srv.lDevices()) {
                    String ref = ied.name() + "/" + ld.inst();
                    boolean hasLln0 = false;
                    boolean hasLphd = false;
                    for (SclLN ln : ld.lns()) {
                        String cls = ln.lnClass();
                        if ("LLN0".equals(cls))
                            hasLln0 = true;
                        else if ("LPHD".equals(cls))
                            hasLphd = true;
                    }
                    if (!hasLln0) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_STRUCTURE).clause("7.1.1")
                                .ref(ref).message("LD has no LLN0 logical node"));
                    }
                    if (!hasLphd) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_STRUCTURE).clause("7.1.1")
                                .ref(ref).message("LD has no LPHD logical node"));
                    }
                    if (ld.lns().size() < 3) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_STRUCTURE).clause("7.1.1")
                                .ref(ref)
                                .message("LD must contain at least 3 LNs (LLN0, LPHD and one application LN); found " + ld.lns().size()));
                    }
                }
            }
        }
    }

    /**
     * §7.1.2 - process-bus GOOSE and SV services must be modelled on separate
     * access points.
     */
    private static void checkAccessPointSeparation(SclDocument doc, List<SclConformanceIssue> issues) {
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv == null)
                    continue;
                boolean hasGse = false;
                boolean hasSmv = false;
                for (SclLDevice ld : srv.lDevices()) {
                    for (SclLN ln : ld.lns()) {
                        if (!ln.gseControls().isEmpty())
                            hasGse = true;
                        if (!ln.svControls().isEmpty())
                            hasSmv = true;
                    }
                }
                if (hasGse && hasSmv) {
                    issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_STRUCTURE).clause("7.1.2")
                            .ref(ied.name() + "/" + ap.name())
                            .message("Access point hosts both GOOSE and SV services; they must be modelled on separate access points"));
                }
            }
        }
    }

    /**
     * §7.2.2 - dsParameter must be an FC=SP set; dsSetting must be an FC=SG set.
     */
    private static void checkDataSetFc(SclDocument doc, List<SclConformanceIssue> issues) {
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv == null)
                    continue;
                for (SclLDevice ld : srv.lDevices()) {
                    for (SclLN ln : ld.lns()) {
                        for (SclDataSet ds : ln.dataSets()) {
                            String expectedFc = null;
                            if ("dsParameter".equals(ds.name())) {
                                expectedFc = "SP";
                            } else if ("dsSetting".equals(ds.name())) {
                                expectedFc = "SG";
                            }
                            if (expectedFc == null)
                                continue;
                            String ref = ied.name() + "/" + ld.inst() + "/" + ln.getFullName() + "." + ds.name();
                            for (SclFCDA fcda : ds.fcDas()) {
                                if (!expectedFc.equals(fcda.fc())) {
                                    issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_DATASET)
                                            .clause("7.2.2").ref(ref).message("Data set '" + ds.name() + "' must be an FC=" + expectedFc
                                                    + " set, but member " + fcda.buildFcdaRef() + " has fc=" + fcda.fc()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /** §7.1.3 - data set members must not cross logical devices. */
    private static void checkDataSetNotCrossLd(SclDocument doc, List<SclConformanceIssue> issues) {
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv == null)
                    continue;
                for (SclLDevice ld : srv.lDevices()) {
                    for (SclLN ln : ld.lns()) {
                        for (SclDataSet ds : ln.dataSets()) {
                            String ref = ied.name() + "/" + ld.inst() + "/" + ln.getFullName() + "." + ds.name();
                            for (SclFCDA fcda : ds.fcDas()) {
                                String memberLd = fcda.ldInst();
                                if (memberLd != null && !memberLd.isEmpty() && !memberLd.equals(ld.inst())) {
                                    issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_DATASET)
                                            .clause("7.1.3").ref(ref)
                                            .message("Data set member " + fcda.buildFcdaRef() + " crosses logical device '" + memberLd
                                                    + "'; data set members must stay within the owning LD"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * §6.2 - IED must carry manufacturer/type/configVersion; LD/LN/DOI should carry
     * Chinese desc and dU.
     */
    private static void checkIedBasicInfo(SclDocument doc, List<SclConformanceIssue> issues) {
        for (SclIED ied : doc.ieds()) {
            String ref = ied.name();
            if (isBlank(ied.manufacturer())) {
                issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_IED_INFO).clause("6.2 c").ref(ref)
                        .message("IED is missing the manufacturer attribute"));
            }
            if (isBlank(ied.type())) {
                issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_IED_INFO).clause("6.2 c").ref(ref)
                        .message("IED is missing the type (model) attribute"));
            }
            if (isBlank(ied.configVersion())) {
                issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_IED_INFO).clause("6.2 c").ref(ref)
                        .message("IED is missing the configVersion attribute"));
            }
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv == null)
                    continue;
                for (SclLDevice ld : srv.lDevices()) {
                    String ldRef = ied.name() + "/" + ld.inst();
                    if (isBlank(ld.desc())) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.WARN).category(CAT_IED_INFO).clause("6.2 a")
                                .ref(ldRef).message("LD should carry a Chinese desc attribute"));
                    }
                    for (SclLN ln : ld.lns()) {
                        String lnRef = ldRef + "/" + ln.getFullName();
                        if (isBlank(ln.desc())) {
                            issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.WARN).category(CAT_IED_INFO)
                                    .clause("6.2 a").ref(lnRef).message("LN should carry a Chinese desc attribute"));
                        }
                        for (SclDOI doi : ln.dois()) {
                            String doiRef = lnRef + "." + doi.name();
                            if (isBlank(doi.desc())) {
                                issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.WARN).category(CAT_IED_INFO)
                                        .clause("6.2 b").ref(doiRef).message("DOI should carry a Chinese desc attribute"));
                            }
                            if (!hasDuValue(doi)) {
                                issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.WARN).category(CAT_IED_INFO)
                                        .clause("6.2 b").ref(doiRef)
                                        .message("DOI should assign the dU data attribute (display unit) consistent with its desc"));
                            }
                        }
                    }
                }
            }
        }
    }

    // ==================== R3 communication parameters ====================

    /** §6.5.2 - GSE APPID must be a 4-digit hex value in 0000..3FFF. */
    private static void checkGseAppId(SclDocument doc, List<SclConformanceIssue> issues) {
        SclCommunication comm = doc.communication();
        if (comm == null)
            return;
        for (SclSubNetwork sn : comm.subNetworks()) {
            for (SclConnectedAP cap : sn.connectedAPs()) {
                for (SclGSE gse : cap.gses()) {
                    String ref = cap.iedName() + "/" + cap.apName() + "/GSE(" + gse.cbName() + ")";
                    String appId = addressValue(gse, "APPID");
                    if (appId == null)
                        continue;
                    Integer hex = parseHex(appId);
                    if (hex == null || appId.length() != 4) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_COMM_PARAM).clause("6.5.2")
                                .ref(ref).message("GSE APPID '" + appId + "' must be a 4-digit hexadecimal value"));
                    } else if (hex > 0x3FFF) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_COMM_PARAM).clause("6.5.2")
                                .ref(ref).message("GSE APPID '" + appId + "' is out of the 0000-3FFF range"));
                    }
                }
            }
        }
    }

    /** §6.5.3 - SMV APPID must be a 4-digit hex value in 4000..7FFF. */
    private static void checkSmvAppId(SclDocument doc, List<SclConformanceIssue> issues) {
        SclCommunication comm = doc.communication();
        if (comm == null)
            return;
        for (SclSubNetwork sn : comm.subNetworks()) {
            for (SclConnectedAP cap : sn.connectedAPs()) {
                for (SclSMV smv : cap.smvs()) {
                    String ref = cap.iedName() + "/" + cap.apName() + "/SMV(" + smv.cbName() + ")";
                    String appId = addressValue(smv, "APPID");
                    if (appId == null)
                        continue;
                    Integer hex = parseHex(appId);
                    if (hex == null || appId.length() != 4) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_COMM_PARAM).clause("6.5.3")
                                .ref(ref).message("SMV APPID '" + appId + "' must be a 4-digit hexadecimal value"));
                    } else if (hex < 0x4000 || hex > 0x7FFF) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_COMM_PARAM).clause("6.5.3")
                                .ref(ref).message("SMV APPID '" + appId + "' is out of the 4000-7FFF range"));
                    }
                }
            }
        }
    }

    /** §6.5.2/6.5.3 - VLAN-ID must be a 3-digit hex value. */
    private static void checkVlanId(SclDocument doc, List<SclConformanceIssue> issues) {
        SclCommunication comm = doc.communication();
        if (comm == null)
            return;
        for (SclSubNetwork sn : comm.subNetworks()) {
            for (SclConnectedAP cap : sn.connectedAPs()) {
                for (SclGSE gse : cap.gses()) {
                    checkVlanIdValue(issues, cap, "GSE(" + gse.cbName() + ")", addressValue(gse, "VLAN-ID"));
                }
                for (SclSMV smv : cap.smvs()) {
                    checkVlanIdValue(issues, cap, "SMV(" + smv.cbName() + ")", addressValue(smv, "VLAN-ID"));
                }
            }
        }
    }

    private static void checkVlanIdValue(List<SclConformanceIssue> issues, SclConnectedAP cap, String block, String vlanId) {
        if (vlanId == null)
            return;
        if (!isHexDigits(vlanId, 3)) {
            issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.ERROR).category(CAT_COMM_PARAM).clause("6.5.2")
                    .ref(cap.iedName() + "/" + cap.apName() + "/" + block)
                    .message("VLAN-ID '" + vlanId + "' must be a 3-digit hexadecimal value"));
        }
    }

    /** §6.5.2 - typical GSE MinTime 2ms / MaxTime 5000ms (recommended values). */
    private static void checkGseMinMaxTime(SclDocument doc, List<SclConformanceIssue> issues) {
        SclCommunication comm = doc.communication();
        if (comm == null)
            return;
        for (SclSubNetwork sn : comm.subNetworks()) {
            for (SclConnectedAP cap : sn.connectedAPs()) {
                for (SclGSE gse : cap.gses()) {
                    String ref = cap.iedName() + "/" + cap.apName() + "/GSE(" + gse.cbName() + ")";
                    Integer minTime = parseInt(gse.minTime());
                    if (minTime != null && minTime != 2) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.WARN).category(CAT_COMM_PARAM).clause("6.5.2")
                                .ref(ref).message("GSE MinTime is " + minTime + "ms; typical value is 2ms"));
                    }
                    Integer maxTime = parseInt(gse.maxTime());
                    if (maxTime != null && maxTime != 5000) {
                        issues.add(new SclConformanceIssue().severity(SclConformanceSeverity.WARN).category(CAT_COMM_PARAM).clause("6.5.2")
                                .ref(ref).message("GSE MaxTime is " + maxTime + "ms; typical value is 5000ms"));
                    }
                }
            }
        }
    }

    // ==================== helpers ====================

    /**
     * Whether the DOI assigns the dU data attribute (display unit, template DA dU
     * of the LPL CDC, fc=DC) with a non-empty instance value.
     */
    private static boolean hasDuValue(SclDOI doi) {
        for (SclDAI dai : doi.dais()) {
            if ("dU".equals(dai.name())) {
                for (SclVal val : dai.vals()) {
                    if (!isBlank(val.value()))
                        return true;
                }
            }
        }
        return false;
    }

    private static String addressValue(SclGSE gse, String type) {
        SclAddress a = gse.findAddressByType(type);
        return a != null ? a.value() : null;
    }

    private static String addressValue(SclSMV smv, String type) {
        for (SclAddress a : smv.addresses()) {
            if (type.equals(a.type()))
                return a.value();
        }
        return null;
    }

    private static boolean isHexDigits(String s, int len) {
        if (s == null || s.length() != len)
            return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')))
                return false;
        }
        return true;
    }

    private static Integer parseHex(String s) {
        if (s == null || !isHexDigits(s, s.length()))
            return null;
        try {
            return Integer.parseInt(s, 16);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseInt(String s) {
        if (s == null)
            return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
