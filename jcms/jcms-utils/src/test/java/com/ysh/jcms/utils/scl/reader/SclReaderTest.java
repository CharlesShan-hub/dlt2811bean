package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.header.SclHeader;
import com.ysh.jcms.utils.scl.model.substation.*;
import com.ysh.jcms.utils.scl.model.communication.*;
import com.ysh.jcms.utils.scl.model.ied.*;
import com.ysh.jcms.utils.scl.model.instance.*;
import com.ysh.jcms.utils.scl.model.input.*;
import com.ysh.jcms.utils.scl.model.template.*;
import com.ysh.jcms.utils.scl.model.control.*;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * SclReader integration tests — full parsing verified with sample-scd-full.scd.
 */
public class SclReaderTest {

    private SclDocument parseFullScd() {
        try {
            SclReader reader = new SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            assertNotNull("sample-scd-full.scd not found on classpath", is);
            return reader.read(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Header ====================

    @Test
    public void testParseHeader() {
        SclDocument doc = parseFullScd();
        SclHeader h = doc.header();
        assertNotNull(h);
        assertEquals("SCL Example T1-1", h.id());
        assertEquals("IEDName", h.nameStructure());
        assertTrue(h.history().isEmpty());
    }

    // ==================== Substation ====================

    @Test
    public void testParseSubstation() {
        SclDocument doc = parseFullScd();
        SclSubstation sub = doc.substation();
        assertNotNull(sub);
        assertEquals("S12", sub.name());
        assertEquals("Baden", sub.desc());

        // 2 VoltageLevels
        assertEquals(2, sub.voltageLevels().size());

        SclVoltageLevel d1 = sub.voltageLevels().get(0);
        assertEquals("D1", d1.name());

        // D1: PowerTransformer T1
        assertEquals(1, d1.transformers().size());
        SclPowerTransformer pt = d1.transformers().get(0);
        assertEquals("T1", pt.name());
        assertEquals("PTR", pt.type());

        // T1 has 2 windings
        assertEquals(2, pt.windings().size());
        assertEquals("W1", pt.windings().get(0).name());
        assertEquals("W2", pt.windings().get(1).name());

        // W1 has a Terminal
        SclTerminal w1Term = pt.windings().get(0).terminals().get(0);
        assertNotNull(w1Term);
        assertEquals("S12/D1/Q1/L1", w1Term.connectivityNode());

        // D1: Voltage element (single)
        assertNotNull(d1.voltage());
        assertEquals("220", d1.voltage().value());
        assertEquals("k", d1.voltage().multiplier());
        assertEquals("V", d1.voltage().unit());

        // D1: Bay Q1 (only 1 bay in D1)
        assertEquals(1, d1.bays().size());
        SclBay bayQ1 = d1.bays().get(0);
        assertEquals("Q1", bayQ1.name());

        // Bay Q1: ConductingEquipment I1 with 4 SubEquipments
        SclConductingEquipment ce = bayQ1.equipments().get(0);
        assertEquals("I1", ce.name());
        assertEquals("CTR", ce.type());
        assertEquals(4, ce.subEquipments().size());
        assertEquals("R", ce.subEquipments().get(0).name());

        // Bay Q1: ConnectivityNode L1
        assertEquals(1, bayQ1.connectivityNodes().size());
        assertEquals("L1", bayQ1.connectivityNodes().get(0).name());

        // VoltageLevel E1
        SclVoltageLevel e1 = sub.voltageLevels().get(1);
        assertEquals("E1", e1.name());
        assertEquals(5, e1.bays().size());
    }

    // ==================== Communication ====================

    @Test
    public void testParseCommunication() {
        SclDocument doc = parseFullScd();
        SclCommunication comm = doc.communication();
        assertNotNull(comm);

        assertEquals(1, comm.subNetworks().size());
        SclSubNetwork sn = comm.subNetworks().get(0);
        assertEquals("W01", sn.name());
        assertEquals("8-MMS", sn.type());

        // 4 ConnectedAPs
        assertEquals(4, sn.connectedAPs().size());

        // First ConnectedAP: D1Q1SB4/S1 with GSE + PhysConn
        SclConnectedAP cap1 = sn.connectedAPs().get(0);
        assertEquals("D1Q1SB4", cap1.iedName());
        assertEquals("S1", cap1.apName());

        // Has GSE
        assertFalse(cap1.gses().isEmpty());
        assertEquals("C1", cap1.gses().get(0).ldInst());
        assertEquals("SyckResult", cap1.gses().get(0).cbName());

        // Has PhysConn
        assertFalse(cap1.physConns().isEmpty());

        // Has Addresses
        assertFalse(cap1.addresses().isEmpty());

        // Second ConnectedAP: E1Q1SB1/S1 with GSE + SMV
        SclConnectedAP cap2 = sn.connectedAPs().get(1);
        assertEquals("E1Q1SB1", cap2.iedName());
        assertFalse(cap2.gses().isEmpty());
        assertFalse(cap2.smvs().isEmpty());
    }

    // ==================== IED ====================

    @Test
    public void testParseIedCount() {
        SclDocument doc = parseFullScd();
        assertEquals(12, doc.ieds().size());
    }

    private SclLN findLn0(SclLDevice ld) {
        for (SclLN ln : ld.lns()) {
            if ("LLN0".equals(ln.lnClass())) {
                return ln;
            }
        }
        return null;
    }

    @Test
    public void testParseIedE1Q1SB1() {
        SclDocument doc = parseFullScd();
        SclIED ied = doc.ied("E1Q1SB1");
        assertNotNull("IED E1Q1SB1 not found", ied);

        // Has Services
        assertNotNull(ied.services());

        // 1 AccessPoint: S1
        assertEquals(1, ied.accessPoints().size());
        SclAccessPoint ap = ied.accessPoints().get(0);
        assertEquals("S1", ap.name());

        // Has Server
        assertNotNull(ap.server());
        assertEquals(1, ap.server().lDevices().size());

        SclLDevice ld = ap.server().lDevices().get(0);
        assertEquals("C1", ld.inst());

        // Find LN0
        SclLN ln0 = findLn0(ld);
        assertNotNull("LN0 not found", ln0);

        // DataSets
        assertEquals(3, ln0.dataSets().size());
        assertEquals("Positions", ln0.dataSets().get(0).name());
        assertEquals("Measurands", ln0.dataSets().get(1).name());
        assertEquals("smv", ln0.dataSets().get(2).name());

        // FCDA in "Positions"
        assertEquals(2, ln0.dataSets().get(0).fcDas().size());
        assertEquals("CSWI", ln0.dataSets().get(0).fcDas().get(0).lnClass());

        // ReportControls
        assertEquals(2, ln0.reportControls().size());
        assertEquals("PosReport", ln0.reportControls().get(0).name());

        // GSEControl
        assertEquals(1, ln0.gseControls().size());
        assertEquals("ItlPositions", ln0.gseControls().get(0).name());

        // SampledValueControl
        assertEquals(1, ln0.svControls().size());
        assertEquals("Volt", ln0.svControls().get(0).name());

        // LogControl
        assertEquals(1, ln0.logControls().size());
        assertEquals("Log", ln0.logControls().get(0).name());

        // LNs: LN0(LLN0) + LPHD + CSWIx2 + MMXU + TVTR = 6
        assertEquals(6, ld.lns().size());
    }

    @Test
    public void testParseIedE1Q1SB1DOI() {
        SclDocument doc = parseFullScd();
        SclIED ied = doc.ied("E1Q1SB1");
        SclLDevice ld = ied.accessPoints().get(0).server().lDevices().get(0);

        // LN LPHD (index 1 in lns list, after LN0)
        SclLN lphd = ld.lns().get(1);
        assertEquals("LPHD", lphd.lnClass());

        assertFalse(lphd.dois().isEmpty());
        SclDOI doi = lphd.dois().get(0);
        assertEquals("Proxy", doi.name());

        assertFalse(doi.dais().isEmpty());
        SclDAI dai = doi.dais().get(0);
        assertEquals("stVal", dai.name());

        assertFalse(dai.vals().isEmpty());
        assertEquals("false", dai.vals().get(0).value());
    }

    @Test
    public void testParseIedE1Q1SB1SDI() {
        SclDocument doc = parseFullScd();
        SclIED ied = doc.ied("E1Q1SB1");
        SclLDevice ld = ied.accessPoints().get(0).server().lDevices().get(0);

        // MMXU LN
        SclLN mmxu = ld.findLnsByClass("MMXU").get(0);
        assertEquals("MMXU", mmxu.lnClass());

        SclDOI volDOI = mmxu.dois().get(0);
        assertEquals("Volts", volDOI.name());

        assertFalse(volDOI.sdis().isEmpty());
        SclSDI sdi = volDOI.sdis().get(0);
        assertEquals("sVC", sdi.name());

        // SDI contains DAI
        assertEquals(2, sdi.dais().size());
        assertEquals("offset", sdi.dais().get(0).name());
        assertEquals("10", sdi.dais().get(0).vals().get(0).value());
        assertEquals("scaleFactor", sdi.dais().get(1).name());
        assertEquals("200", sdi.dais().get(1).vals().get(0).value());
    }

    @Test
    public void testParseIedD1Q1SB4() {
        SclDocument doc = parseFullScd();
        SclIED ied = doc.ied("D1Q1SB4");
        assertNotNull(ied);

        // Has Services
        assertNotNull(ied.services());

        // Has Server with LDevice
        SclAccessPoint ap = ied.accessPoints().get(0);
        assertNotNull(ap.server());
        SclLDevice ld = ap.server().lDevices().get(0);

        // Find LN0
        SclLN ln0 = findLn0(ld);
        assertNotNull("LN0 not found in D1Q1SB4", ln0);

        // LN0 with DataSet + GSEControl
        assertEquals(1, ln0.dataSets().size());
        assertEquals("SyckResult", ln0.dataSets().get(0).name());
        assertEquals(1, ln0.gseControls().size());
        assertEquals("SyckResult", ln0.gseControls().get(0).name());
    }

    // ==================== DataTypeTemplates ====================

    @Test
    public void testParseDataTypeTemplates() {
        SclDocument doc = parseFullScd();
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        assertNotNull(templates);

        // 7 LNodeTypes (LN0, LPHDa, CSWIa, MMXUa, CILOa, TVTRa, RSYNa)
        assertEquals(7, templates.lNodeTypes().size());
        SclLNodeType ln0Type = templates.findLNodeTypeById("LN0");
        assertNotNull(ln0Type);
        assertEquals("LLN0", ln0Type.lnClass());
        // 4 DOs: Mod, Health, Beh, NamPlt
        assertEquals(4, ln0Type.dos().size());

        // 12 DOType
        assertEquals(12, templates.doTypes().size());
        SclDOType myMod = templates.findDoTypeById("myMod");
        assertNotNull(myMod);
        assertEquals("INC", myMod.cdc());
        // 4 DAs
        assertEquals(4, myMod.das().size());

        // DOType with SDO (mySEQ)
        SclDOType mySeq = templates.findDoTypeById("mySEQ");
        assertNotNull(mySeq);
        assertEquals(3, mySeq.sdos().size());
        assertEquals("c1", mySeq.sdos().get(0).name());

        // 3 DAType
        assertEquals(3, templates.daTypes().size());
        SclDAType analogVal = templates.findDaTypeById("myAnalogValue");
        assertNotNull(analogVal);
        assertEquals(1, analogVal.bdas().size());
        assertEquals("f", analogVal.bdas().get(0).name());
        assertEquals("FLOAT32", analogVal.bdas().get(0).bType());

        // 7 EnumType
        assertEquals(7, templates.enumTypes().size());
        SclEnumType beh = templates.findEnumTypeById("Beh");
        assertNotNull(beh);
        // 5 values
        assertEquals(5, beh.enumVals().size());
        assertEquals(1, beh.enumVals().get(0).ord());
        assertEquals("on", beh.enumVals().get(0).value());
    }

    // ==================== edge cases ====================

    @Test
    public void testNoUnsupportedElements() {
        SclDocument doc = parseFullScd();
        assertFalse("Unexpected unsupported elements: " + doc.unsupportedElements(), doc.hasUnsupportedElements());
    }

    @Test
    public void testDocumentFileType() {
        SclDocument doc = parseFullScd();
        // determined by content structure: sample contains <Substation> → SCD
        assertEquals(SclDocument.SclFileType.SCD, doc.fileType());
    }

    @Test
    public void testScanLdLns() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
        assertNotNull("sample-scd-full.scd not found on classpath", is);
        java.util.Map<String, java.util.List<String>> dir = SclReader.scanLdLns(is);

        // E1Q1SB1/S1 → 6 LNs (LLN0 + LPHD + CSWI1 + CSWI2 + MMXU1 + TVTR1)
        java.util.List<String> e1 = dir.get("E1Q1SB1/S1");
        assertNotNull("E1Q1SB1/S1 not found", e1);
        assertEquals(6, e1.size());
        assertTrue(e1.contains("C1/LLN0"));
        assertTrue(e1.contains("C1/CSWI1"));
        assertTrue(e1.contains("C1/MMXU1"));
    }

    @Test
    public void testIedE1Q1SB1HasDataSetsOnLN0() {
        SclDocument doc = parseFullScd();
        SclIED ied = doc.ied("E1Q1SB1");
        SclLN ln0 = findLn0(ied.accessPoints().get(0).server().lDevices().get(0));
        assertNotNull("LN0 not found", ln0);

        // Check FCDA fields in "Positions" DataSet
        SclDataSet posDs = ln0.dataSets().get(0);
        List<SclFCDA> fcdas = posDs.fcDas();
        assertEquals(2, fcdas.size());

        SclFCDA fcda1 = fcdas.get(0);
        assertEquals("C1", fcda1.ldInst());
        assertEquals("CSWI", fcda1.lnClass());
        assertEquals("1", fcda1.lnInst());
        assertEquals("Pos", fcda1.doName());
        assertEquals("ST", fcda1.fc());

        SclFCDA fcda2 = fcdas.get(1);
        assertEquals("2", fcda2.lnInst());

        // Check report control details
        SclReportControl rc = ln0.reportControls().get(0);
        assertEquals("PosReport", rc.name());
        assertEquals("Positions", rc.datSet());
        assertEquals("1", rc.confRev());
        // buffered attribute is optional, not present in this sample
        assertNull("buffered should be null when not specified", rc.buffered());

        // Log control
        assertEquals(1, ln0.logControls().size());
        assertEquals("Log", ln0.logControls().get(0).name());

        // GSEControl
        SclGSEControl gse = ln0.gseControls().get(0);
        assertEquals("ItlPositions", gse.name());
        assertEquals("Positions", gse.datSet());
        assertEquals("Itl", gse.appID());

        // SampledValueControl
        SclSampledValueControl svc = ln0.svControls().get(0);
        assertEquals("Volt", svc.name());
        assertEquals("11", svc.svID());
        assertEquals("4800", svc.smpRate());
    }

    @Test
    public void testParseIedCountAndNames() {
        SclDocument doc = parseFullScd();
        String[] expectedNames = {"E1Q1SB1", "E1Q1BP2", "E1Q1BP3", "E1Q2SB1", "E1Q3SB1", "E1Q3KA1", "E1Q3KA2", "E1Q3KA3", "D1Q1SB1",
                "D1Q1BP2", "D1Q1BP3", "D1Q1SB4"};
        for (String name : expectedNames) {
            assertNotNull("IED " + name + " should exist", doc.ied(name));
        }
    }
}
