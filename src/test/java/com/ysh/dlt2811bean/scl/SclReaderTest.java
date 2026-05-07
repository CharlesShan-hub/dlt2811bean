package com.ysh.dlt2811bean.scl;

import com.ysh.dlt2811bean.scl.model.SclIED;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SclReaderTest {

    @Test
    void testParseFullScd() throws Exception {
        SclReader reader = new SclReader();
        SclDocument doc = reader.read("config/sample-scd-full.scd");

        assertNotNull(doc);
        assertNotNull(doc.getHeader());
        assertNotNull(doc.getHeader().getId());
        System.out.println("=== sample-scd-full.scd ===");
        System.out.println("Header id: " + doc.getHeader().getId());
        System.out.println("Header version: " + doc.getHeader().getVersion());

        assertNotNull(doc.getSubstation());
        System.out.println("Substation: " + doc.getSubstation().getName());

        assertNotNull(doc.getCommunication());
        System.out.println("Communication SubNetworks: " + doc.getCommunication().getSubNetworks().size());

        assertNotNull(doc.getIeds());
        System.out.println("IED count: " + doc.getIeds().size());
        for (SclIED ied : doc.getIeds()) {
            System.out.println("  IED: " + ied.getName());
            for (SclIED.SclAccessPoint ap : ied.getAccessPoints()) {
                System.out.println("    AccessPoint: " + ap.getName());
                if (ap.getServer() != null) {
                    for (SclIED.SclLDevice ld : ap.getServer().getLDevices()) {
                        System.out.println("      LDevice: " + ld.getInst());
                        if (ld.getLn0() != null) {
                            System.out.println("        LN0: " + ld.getLn0().getLnType());
                            System.out.println("          DataSets: " + ld.getLn0().getDataSets().size());
                            System.out.println("          ReportControls: " + ld.getLn0().getReportControls().size());
                            System.out.println("          GSEControls: " + ld.getLn0().getGseControls().size());
                            System.out.println("          SampledValueControls: " + ld.getLn0().getSampledValueControls().size());
                        }
                        System.out.println("        LNs: " + ld.getLns().size());
                    }
                }
            }
        }

        assertNotNull(doc.getDataTypeTemplates());
        System.out.println("LNodeTypes: " + doc.getDataTypeTemplates().getLNodeTypes().size());
        System.out.println("DOTypes: " + doc.getDataTypeTemplates().getDoTypes().size());
        System.out.println("DATypes: " + doc.getDataTypeTemplates().getDaTypes().size());
        System.out.println("EnumTypes: " + doc.getDataTypeTemplates().getEnumTypes().size());
    }

    @Test
    void testParseRelayScd() throws Exception {
        SclReader reader = new SclReader();
        SclDocument doc = reader.read("config/sample-scd-relay.scd");

        assertNotNull(doc);
        assertNotNull(doc.getHeader());
        assertNotNull(doc.getHeader().getId());
        System.out.println("\n=== sample-scd-relay.scd ===");
        System.out.println("Header id: " + doc.getHeader().getId());
        System.out.println("Header version: " + doc.getHeader().getVersion());

        assertNotNull(doc.getIeds());
        System.out.println("IED count: " + doc.getIeds().size());
        for (SclIED ied : doc.getIeds()) {
            System.out.println("  IED: " + ied.getName());
            for (SclIED.SclAccessPoint ap : ied.getAccessPoints()) {
                System.out.println("    AccessPoint: " + ap.getName());
                if (ap.getServer() != null) {
                    for (SclIED.SclLDevice ld : ap.getServer().getLDevices()) {
                        System.out.println("      LDevice: " + ld.getInst());
                        if (ld.getLn0() != null) {
                            System.out.println("        LN0: " + ld.getLn0().getLnType());
                            System.out.println("          DataSets: " + ld.getLn0().getDataSets().size());
                            System.out.println("          ReportControls: " + ld.getLn0().getReportControls().size());
                            System.out.println("          GSEControls: " + ld.getLn0().getGseControls().size());
                        }
                        System.out.println("        LNs: " + ld.getLns().size());
                        for (SclIED.SclLN ln : ld.getLns()) {
                            System.out.println("          LN: " + ln.getLnClass() + " " + (ln.getPrefix() != null ? ln.getPrefix() : "") + ln.getInst());
                            if (ln.getInputs() != null && !ln.getInputs().isEmpty()) {
                                System.out.println("            Inputs (ExtRefs): " + ln.getInputs().get(0).getExtRefs().size());
                            }
                        }
                    }
                }
            }
        }

        assertNotNull(doc.getDataTypeTemplates());
        System.out.println("LNodeTypes: " + doc.getDataTypeTemplates().getLNodeTypes().size());
        System.out.println("DOTypes: " + doc.getDataTypeTemplates().getDoTypes().size());
        System.out.println("DATypes: " + doc.getDataTypeTemplates().getDaTypes().size());
        System.out.println("EnumTypes: " + doc.getDataTypeTemplates().getEnumTypes().size());
    }
}