package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsObjectReference")
class CmsObjectReferenceTest {

    private CmsObjectReference get() { return (CmsObjectReference)(new CmsObjectReference().test()); }

    @Test
    void roundtrip() {
        CmsObjectReference original = (CmsObjectReference) CmsObjectReference.of("IED1", "CTRL", "GGIO1", "SPCSO1").test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void ldNameExplicit() {
        CmsObjectReference ref = CmsObjectReference.of("STATION_CTRL", "CILO", "Clc1");
        assertEquals("STATION_CTRL", ref.ldName());
    }

    @Test
    void ldNameComposed() {
        CmsObjectReference ref = CmsObjectReference.of("P1A1/PROT", "GGIO1", "SPCSO1");
        assertEquals("P1A1/PROT", ref.ldName());
        assertEquals("P1A1", ref.iedName());
        assertEquals("PROT", ref.ldInst());
    }

    @Test
    void lnName() {
        CmsObjectReference ref = CmsObjectReference.of("IED1", "GGIO1", "SPCSO1");
        assertEquals("GGIO1", ref.lnName());
    }

    @Test
    void lnNameWithoutDot() {
        CmsObjectReference ref = CmsObjectReference.of("IED1", "LLN0");
        assertEquals("LLN0", ref.lnName());
    }

    @Test
    void dataSetReference() {
        CmsObjectReference ref = CmsObjectReference.dataSet("MyDataSet");
        assertEquals("@MyDataSet", new String(ref.value()).trim());
        assertEquals("MyDataSet", ref.dataSetName());
    }

    @Test
    void iedNameEmptyForExplicitLdName() {
        CmsObjectReference ref = CmsObjectReference.of("STATION_CTRL", "CILO");
        assertEquals("", ref.iedName());
        assertEquals("", ref.ldInst());
    }
}
