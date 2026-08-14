package com.ysh.jcms.utils.scl.conformance;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.communication.SclAddress;
import com.ysh.jcms.utils.scl.model.communication.SclCommunication;
import com.ysh.jcms.utils.scl.model.communication.SclConnectedAP;
import com.ysh.jcms.utils.scl.model.communication.SclGSE;
import com.ysh.jcms.utils.scl.model.communication.SclSMV;
import com.ysh.jcms.utils.scl.model.communication.SclSubNetwork;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.reader.SclReader;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Q/GDW 1396 conformance check tests — sample SCD findings plus crafted
 * documents covering every rule family (R1 naming / R2 structure / R3 comm
 * parameters).
 */
public class SclConformanceCheckTest {

    // ==================== sample SCD ====================

    private SclDocument loadSample() {
        try {
            SclReader reader = new SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            assertNotNull("sample-scd-full.scd not found on classpath", is);
            return reader.read(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<SclConformanceIssue> checkSample(SclConformanceMode mode) {
        return SclConformanceCheck.check(loadSample(), mode);
    }

    @Test
    public void testLooseModeReturnsNothing() {
        assertTrue(checkSample(SclConformanceMode.LOOSE).isEmpty());
    }

    @Test
    public void testNullSafety() {
        assertTrue(SclConformanceCheck.check(null, SclConformanceMode.STRICT).isEmpty());
        assertTrue(SclConformanceCheck.check(loadSample(), null).isEmpty());
        assertTrue(SclConformanceCheck.check(loadSample(), SclConformanceMode.LOOSE).isEmpty());
    }

    @Test
    public void testSampleDetectsLdNamingError() {
        List<SclConformanceIssue> issues = checkSample(SclConformanceMode.STRICT);
        assertTrue(issues.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                && "LD-NAMING".equals(i.category()) && "E1Q1SB1/C1".equals(i.ref())));
        assertTrue(issues.stream().anyMatch(i -> "LD-NAMING".equals(i.category()) && "D1Q1SB4/C1".equals(i.ref())));
    }

    @Test
    public void testSampleDetectsSubNetworkWarning() {
        List<SclConformanceIssue> issues = checkSample(SclConformanceMode.STRICT);
        assertTrue(issues.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.WARN
                && "SUB-NETWORK".equals(i.category()) && "W01".equals(i.ref())));
    }

    @Test
    public void testSampleDetectsGooseSmvOnSameAp() {
        List<SclConformanceIssue> issues = checkSample(SclConformanceMode.STRICT);
        assertTrue(issues.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                && "STRUCTURE".equals(i.category()) && "E1Q1SB1/S1".equals(i.ref())
                && i.message().contains("GOOSE and SV")));
    }

    @Test
    public void testSampleMissingIedNameplate() {
        List<SclConformanceIssue> issues = checkSample(SclConformanceMode.STRICT);
        assertTrue(issues.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                && "IED-INFO".equals(i.category()) && "E1Q1SB1".equals(i.ref())
                && i.message().contains("manufacturer")));
    }

    @Test
    public void testSampleAppIdsAndVlanPass() {
        List<SclConformanceIssue> issues = checkSample(SclConformanceMode.STRICT);
        // GSE APPID 3001/3000, SMV APPID 4000, VLAN-ID 123 are all legal
        assertFalse(issues.stream().anyMatch(i -> "COMM-PARAM".equals(i.category())
                && (i.message().contains("APPID") || i.message().contains("VLAN-ID"))));
    }

    // ==================== crafted documents ====================

    private static SclDocument buildIedDoc(String ldInst, String... lnClasses) {
        SclDocument doc = new SclDocument();
        SclIED ied = new SclIED();
        ied.name("PL2201A");
        ied.manufacturer("NR");
        ied.type("PCS-931");
        ied.configVersion("1.0");
        SclAccessPoint ap = new SclAccessPoint();
        ap.name("S1");
        SclServer srv = new SclServer();
        SclLDevice ld = new SclLDevice();
        ld.inst(ldInst);
        int n = 1;
        for (String cls : lnClasses) {
            SclLN ln = new SclLN();
            ln.lnClass(cls);
            ln.inst("LLN0".equals(cls) ? "" : String.valueOf(n++));
            ld.addLn(ln);
        }
        srv.addLDevice(ld);
        ap.server(srv);
        ied.addAccessPoint(ap);
        doc.addIed(ied);
        return doc;
    }

    private static SclLN firstLln0(SclDocument doc) {
        return doc.ieds().get(0).accessPoints().get(0).server().lDevices().get(0).lns().get(0);
    }

    private static SclLN addDataset(SclDocument doc, String name, String fc, String memberLd) {
        SclLN ln0 = firstLln0(doc);
        SclDataSet ds = new SclDataSet();
        ds.name(name);
        SclFCDA fcda = new SclFCDA();
        fcda.ldInst(memberLd);
        fcda.lnClass("PTRC");
        fcda.lnInst("1");
        fcda.doName("Mod");
        fcda.fc(fc);
        ds.addFcda(fcda);
        ln0.addDataSet(ds);
        return ln0;
    }

    private static List<SclConformanceIssue> errors(List<SclConformanceIssue> issues) {
        return issues.stream().filter(i -> i.severity() == SclConformanceSeverity.ERROR)
                .collect(Collectors.toList());
    }

    @Test
    public void testValidGwDocumentHasNoErrors() {
        SclDocument doc = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        assertTrue(errors(SclConformanceCheck.check(doc, SclConformanceMode.STRICT)).isEmpty());
    }

    @Test
    public void testReservedLdNamesWithSuffixAllowed() {
        for (String inst : new String[] {"PROT", "MEAS", "PIGO01", "MUSV02"}) {
            SclDocument doc = buildIedDoc(inst, "LLN0", "LPHD", "PTRC");
            assertFalse("LD " + inst + " should be accepted",
                    SclConformanceCheck.check(doc, SclConformanceMode.STRICT).stream()
                            .anyMatch(i -> "LD-NAMING".equals(i.category())));
        }
    }

    @Test
    public void testInvalidLdNameReported() {
        SclDocument doc = buildIedDoc("FOO", "LLN0", "LPHD", "PTRC");
        List<SclConformanceIssue> issues = SclConformanceCheck.check(doc, SclConformanceMode.STRICT);
        assertTrue(issues.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                && "LD-NAMING".equals(i.category()) && "PL2201A/FOO".equals(i.ref())));
    }

    @Test
    public void testDsSettingMustBeSg() {
        SclDocument ok = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        addDataset(ok, "dsSetting", "SG", "PROT");
        assertTrue(errors(SclConformanceCheck.check(ok, SclConformanceMode.STRICT)).stream()
                .noneMatch(i -> i.message().contains("dsSetting")));

        SclDocument bad = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        addDataset(bad, "dsSetting", "ST", "PROT");
        assertTrue(SclConformanceCheck.check(bad, SclConformanceMode.STRICT).stream()
                .anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                        && i.message().contains("dsSetting") && i.message().contains("FC=SG")));
    }

    @Test
    public void testDsParameterMustBeSp() {
        SclDocument bad = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        addDataset(bad, "dsParameter", "SG", "PROT");
        assertTrue(SclConformanceCheck.check(bad, SclConformanceMode.STRICT).stream()
                .anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                        && i.message().contains("dsParameter") && i.message().contains("FC=SP")));
    }

    @Test
    public void testDataSetMustNotCrossLd() {
        SclDocument doc = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        addDataset(doc, "ds", "ST", "MEAS");
        assertTrue(SclConformanceCheck.check(doc, SclConformanceMode.STRICT).stream()
                .anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                        && i.message().contains("crosses logical device")));
    }

    @Test
    public void testGooseSmvOnSameApReported() {
        SclDocument doc = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        SclLN ln0 = firstLln0(doc);
        ln0.addGseControl(new SclGSEControl().name("gcb"));
        ln0.addSvControl(new SclSampledValueControl().name("svb"));
        assertTrue(SclConformanceCheck.check(doc, SclConformanceMode.STRICT).stream()
                .anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                        && "STRUCTURE".equals(i.category()) && "PL2201A/S1".equals(i.ref())));
    }

    // ==================== communication parameters ====================

    private static SclDocument withGseComm(SclDocument doc, String appId, String vlanId) {
        SclCommunication comm = new SclCommunication();
        SclSubNetwork sn = new SclSubNetwork();
        sn.name("Subnetwork_Processbus");
        SclConnectedAP cap = new SclConnectedAP();
        cap.iedName("PL2201A");
        cap.apName("G1");
        SclGSE gse = new SclGSE();
        gse.cbName("gcb");
        if (appId != null)
            gse.addAddress(new SclAddress().type("APPID").value(appId));
        if (vlanId != null)
            gse.addAddress(new SclAddress().type("VLAN-ID").value(vlanId));
        cap.addGse(gse);
        sn.addConnectedAP(cap);
        comm.addSubNetwork(sn);
        doc.communication(comm);
        return doc;
    }

    private static SclDocument withSmvComm(SclDocument doc, String appId) {
        SclCommunication comm = new SclCommunication();
        SclSubNetwork sn = new SclSubNetwork();
        sn.name("Subnetwork_Processbus");
        SclConnectedAP cap = new SclConnectedAP();
        cap.iedName("PL2201A");
        cap.apName("M1");
        SclSMV smv = new SclSMV();
        smv.cbName("svb");
        smv.addAddress(new SclAddress().type("APPID").value(appId));
        cap.addSmv(smv);
        sn.addConnectedAP(cap);
        comm.addSubNetwork(sn);
        doc.communication(comm);
        return doc;
    }

    private static boolean hasAppIdError(SclDocument doc, String expected) {
        return SclConformanceCheck.check(doc, SclConformanceMode.STRICT).stream()
                .anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                        && i.message().contains("APPID") && i.message().contains(expected));
    }

    @Test
    public void testGseAppIdRange() {
        SclDocument base = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        assertFalse(hasAppIdError(withGseComm(base, "3000", null), "3000"));
        assertFalse(hasAppIdError(withGseComm(base, "3FFF", null), "3FFF"));
        assertTrue(hasAppIdError(withGseComm(base, "4000", null), "4000")); // reserved for SMV
        assertTrue(hasAppIdError(withGseComm(base, "ABCD", null), "ABCD")); // > 3FFF
        assertTrue(hasAppIdError(withGseComm(base, "300", null), "300"));   // not 4 digits
    }

    @Test
    public void testSmvAppIdRange() {
        SclDocument base = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        assertFalse(hasAppIdError(withSmvComm(base, "4000"), "4000"));
        assertFalse(hasAppIdError(withSmvComm(base, "7FFF"), "7FFF"));
        assertTrue(hasAppIdError(withSmvComm(base, "3FFF"), "3FFF")); // GOOSE range
        assertTrue(hasAppIdError(withSmvComm(base, "8000"), "8000")); // > 7FFF
    }

    @Test
    public void testVlanIdLength() {
        SclDocument base = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        List<SclConformanceIssue> ok = SclConformanceCheck.check(withGseComm(base, "3000", "123"),
                SclConformanceMode.STRICT);
        assertFalse(ok.stream().anyMatch(i -> i.message().contains("VLAN-ID")));

        List<SclConformanceIssue> bad = SclConformanceCheck.check(withGseComm(base, "3000", "12"),
                SclConformanceMode.STRICT);
        assertTrue(bad.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                && i.message().contains("VLAN-ID") && i.message().contains("12")));
    }

    @Test
    public void testSubNetworkNameWarning() {
        SclDocument good = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        assertFalse(SclConformanceCheck.check(withGseComm(good, "3000", null), SclConformanceMode.STRICT).stream()
                .anyMatch(i -> "SUB-NETWORK".equals(i.category())));

        SclDocument bad = buildIedDoc("PROT", "LLN0", "LPHD", "PTRC");
        SclCommunication comm = new SclCommunication();
        SclSubNetwork sn = new SclSubNetwork();
        sn.name("W01");
        comm.addSubNetwork(sn);
        bad.communication(comm);
        assertTrue(SclConformanceCheck.check(bad, SclConformanceMode.STRICT).stream()
                .anyMatch(i -> i.severity() == SclConformanceSeverity.WARN
                        && "SUB-NETWORK".equals(i.category()) && "W01".equals(i.ref())));
    }

    @Test
    public void testLdMustContainLln0AndLphd() {
        SclDocument doc = buildIedDoc("PROT", "PTRC");
        List<SclConformanceIssue> issues = SclConformanceCheck.check(doc, SclConformanceMode.STRICT);
        assertTrue(issues.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                && i.message().contains("no LLN0")));
        assertTrue(issues.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                && i.message().contains("no LPHD")));
        assertTrue(issues.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                && i.message().contains("at least 3 LNs")));
    }

    // ==================== R4 mandatory DO (Appendix A/B) ====================

    @Test
    public void testSampleRequiredDo() {
        List<SclConformanceIssue> issues = checkSample(SclConformanceMode.STRICT);
        // sample TVTRa lacks NamPlt, which is mandatory for TVTR
        assertTrue(issues.stream().anyMatch(i -> i.severity() == SclConformanceSeverity.ERROR
                && "LN-TEMPLATE".equals(i.category()) && "E1Q1SB1/C1/TVTR1".equals(i.ref())
                && i.message().contains("NamPlt")));
        // RSYNa carries the full mandatory set
        assertFalse(issues.stream().anyMatch(i -> "LN-TEMPLATE".equals(i.category())
                && i.ref().contains("RSYN")));
    }

    @Test
    public void testRequiredDoChecked() {
        // complete PDIF type passes
        SclDocument ok = buildIedDoc("PROT", "PDIF");
        SclLN pdifOk = lnOfClass(ok, "PDIF");
        pdifOk.lnType("PDIFt");
        attachTemplate(ok, "PDIFt", "Mod", "Beh", "Health", "NamPlt", "Str", "Op");
        assertFalse(errors(SclConformanceCheck.check(ok, SclConformanceMode.STRICT)).stream()
                .anyMatch(i -> "LN-TEMPLATE".equals(i.category())));

        // PDIF type missing Str fails
        SclDocument bad = buildIedDoc("PROT", "PDIF");
        SclLN pdifBad = lnOfClass(bad, "PDIF");
        pdifBad.lnType("PDIFt");
        attachTemplate(bad, "PDIFt", "Mod", "Beh", "Health", "NamPlt", "Op");
        assertTrue(errors(SclConformanceCheck.check(bad, SclConformanceMode.STRICT)).stream()
                .anyMatch(i -> "LN-TEMPLATE".equals(i.category()) && i.message().contains("Str")));
    }

    private static SclLN lnOfClass(SclDocument doc, String lnClass) {
        for (SclLN ln : doc.ieds().get(0).accessPoints().get(0).server().lDevices().get(0).lns()) {
            if (lnClass.equals(ln.lnClass()))
                return ln;
        }
        return null;
    }

    private static void attachTemplate(SclDocument doc, String id, String... dos) {
        SclLNodeType lnt = new SclLNodeType();
        lnt.id(id);
        for (String d : dos) {
            lnt.addDo(new SclDO().name(d));
        }
        SclDataTypeTemplates templates = new SclDataTypeTemplates();
        templates.addLNodeType(lnt);
        doc.dataTypeTemplates(templates);
    }
}
