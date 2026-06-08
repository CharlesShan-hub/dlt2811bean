package com.ysh.jcms.datatype.control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOriginator")
class CmsOriginatorTest {

    private CmsOriginator get() { return (CmsOriginator)(new CmsOriginator().test()); }

    @Test
    void roundtrip() {
        CmsOriginator original = get();
        original.or_cat().value(CmsOriginator.ORCAT_BAY_CONTROL);
        original.or_ident().value("bay01");

        CmsOriginator decoded = get().decode(original.encode());
        assertEquals(CmsOriginator.ORCAT_BAY_CONTROL, decoded.or_cat().value());
        assertEquals("bay01", new String(decoded.or_ident().value()).trim());
    }

    @Test
    void stationControl() {
        CmsOriginator original = get();
        original.or_cat().value(CmsOriginator.ORCAT_STATION_CONTROL);
        original.or_ident().value("station_ctrl");

        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void remoteControl() {
        CmsOriginator original = get();
        original.or_cat().value(CmsOriginator.ORCAT_REMOTE_CONTROL);
        original.or_ident().value("remote-01");

        CmsOriginator decoded = get().decode(original.encode());
        assertEquals("remote-01", new String(decoded.or_ident().value()).trim());
    }

    @Test
    void emptyIdent() {
        CmsOriginator original = get();
        original.or_cat().value(CmsOriginator.ORCAT_PROCESS);
        original.or_ident().value("");

        CmsOriginator decoded = get().decode(original.encode());
        assertEquals("", new String(decoded.or_ident().value()).trim());
    }

    @Test
    void allCategories() {
        for (int cat = 0; cat <= 8; cat++) {
            CmsOriginator original = get();
            original.or_cat().value(cat);
            original.or_ident().value("test");
            CmsOriginator decoded = get().decode(original.encode());
            assertEquals(cat, decoded.or_cat().value(), "category " + cat);
        }
    }

    @Test
    void decodeOverwrites() {
        CmsOriginator src = get();
        src.or_cat().value(CmsOriginator.ORCAT_STATION_CONTROL);
        src.or_ident().value("new_ident");

        CmsOriginator target = get();
        target.or_cat().value(CmsOriginator.ORCAT_BAY_CONTROL);
        target.or_ident().value("original");

        target.decode(src.encode());
        assertEquals(CmsOriginator.ORCAT_STATION_CONTROL, target.or_cat().value());
        assertEquals("new_ident", new String(target.or_ident().value()).trim());
    }
}
