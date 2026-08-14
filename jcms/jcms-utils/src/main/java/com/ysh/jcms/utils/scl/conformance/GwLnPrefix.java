package com.ysh.jcms.utils.scl.conformance;

/**
 * Logical node prefix examples from Q/GDW 1396-2012 Appendix I (informative).
 * <p>
 * I.1 mandates that breakers use "CB", disconnectors "QG1-QG4", earthing
 * switches "QGD1-QGD4" (with A/B/C suffix for per-phase breakers). I.2 suggests
 * functional English abbreviations for other LNs, e.g. "LinMMXU1",
 * "PctDifPDIF". The appendix is informative, so this table only backs an INFO
 * suggestion - never an ERROR.
 */
public enum GwLnPrefix {

    // I.1 primary-equipment prefixes
    CB("Circuit breaker (XCBR/CSWI/CILO)"), QG("Disconnector (XSWI)"), QGD("Earthing switch"),

    // I.2 functional abbreviations (prefix examples)
    Lin("Line measurement (LinMMXU1 / LinMMXN1)"), Bus("Bus measurement (BusMMXU1 / BusMMXN1)"), CBSyn(
            "Breaker synchronising control (CBSynCSWI1)"), CBDea("Breaker dead-line control (CBDeaCSWI1)"), BinIn(
                    "Binary input status (BinInGGIOx)"), DevAlm("Device alarm (DevAlmGGIOx)"), GOAlm("GOOSE alarm (GOAlmGGIOx)"), PP(
                            "Phase-phase distance (PPPDIS2)"), PG("Phase-ground distance (PGPDIS3)"), Ph(
                                    "Phase overcurrent (PhPTOC2)"), Zer("Zero-sequence overcurrent (ZerPTOC3)"), CarDis(
                                            "Carrier distance (CarDisPDIS / CarDisPSCH)"), CarZer(
                                                    "Carrier zero-sequence (CarZerPTOC / CarZerPSCH)"), PctDif(
                                                            "Percentage differential (PctDifPDIF)"), HiSet(
                                                                    "High-set differential (HiSetPDIF)"), RemTr(
                                                                            "Remote trip (RemTrPSCH)"), Cha(
                                                                                    "Charging protection (ChaPTOC)"), PTFail(
                                                                                            "PT failure overcurrent (PTFailPTOC)");

    private final String desc;

    GwLnPrefix(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    /**
     * Whether a non-empty LN prefix follows one of the Appendix I examples.
     * <p>
     * Null/empty prefixes are always accepted (plain LNs without a prefix are
     * legitimate, e.g. LLN0).
     *
     * @param prefix
     *            LN prefix attribute
     * @return true if empty or matching a known example
     */
    public static boolean matches(String prefix) {
        if (prefix == null || prefix.isEmpty())
            return true;
        for (GwLnPrefix p : values()) {
            if (prefix.startsWith(p.name()))
                return true;
        }
        return false;
    }
}
