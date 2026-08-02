package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.data.choice.CmsCbValueChoice;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.control.*;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class CbConverterTest {

    private SclDocument parseFullScd() {
        try {
            com.ysh.jcms.utils.scl.reader.SclReader reader = new com.ysh.jcms.utils.scl.reader.SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            return reader.read(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBrcb() {
        SclDocument doc = parseFullScd();
        SclReportControl rc = doc.ied("E1Q1SB1").accessPoints().get(0).server().lDevices().get(0).findLnByFullName("LLN0")
                .findReportControlByName("PosReport");
        assertNotNull(rc);

        CmsCbValueChoice result = CbConverter.brcbFrom(rc);
        assertEquals(CmsCbValueChoice.BRCB, result.choice());
        assertNotNull(result.altBrcb);
    }

    @Test
    public void testUrcb() {
        SclDocument doc = parseFullScd();
        SclReportControl rc = doc.ied("E1Q1SB1").accessPoints().get(0).server().lDevices().get(0).findLnByFullName("LLN0")
                .findReportControlByName("PosReport");
        assertNotNull(rc);

        CmsCbValueChoice result = CbConverter.urcbFrom(rc);
        assertEquals(CmsCbValueChoice.URCB, result.choice());
    }

    @Test
    public void testGocb() {
        SclDocument doc = parseFullScd();
        SclGSEControl gse = doc.ied("E1Q1SB1").accessPoints().get(0).server().lDevices().get(0).findLnByFullName("LLN0")
                .findGseControlByName("ItlPositions");
        assertNotNull(gse);

        CmsCbValueChoice result = CbConverter.gocbFrom(gse);
        assertEquals(CmsCbValueChoice.GOCB, result.choice());
    }

    @Test
    public void testLcb() {
        SclDocument doc = parseFullScd();
        SclLogControl lc = doc.ied("E1Q1SB1").accessPoints().get(0).server().lDevices().get(0).findLnByFullName("LLN0").logControls()
                .get(0);
        assertNotNull(lc);

        CmsCbValueChoice result = CbConverter.lcbFrom(lc);
        assertEquals(CmsCbValueChoice.LCB, result.choice());
    }

    @Test
    public void testMsvcb() {
        SclDocument doc = parseFullScd();
        SclSampledValueControl sv = doc.ied("E1Q1SB1").accessPoints().get(0).server().lDevices().get(0).findLnByFullName("LLN0")
                .findSmvControlByName("Volt");
        assertNotNull(sv);

        CmsCbValueChoice result = CbConverter.msvcbFrom(sv);
        assertEquals(CmsCbValueChoice.MSVCB, result.choice());
        assertEquals(4800, result.altMsvcb.smpRate.value());
    }
}
