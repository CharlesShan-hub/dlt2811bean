package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsMsvcb")
class CmsMsvcbTest {

    @Test
    void roundtrip() {
        CmsMsvcb original = new CmsMsvcb();
        original.svEna().value(true);
        original.msvID().value("MSVCB_01");
        original.datSet().value("DataSet_01");
        original.confRev().value(1);
        original.smpMod().value(CmsSmpMod.SAMPLES_PER_SECOND);
        original.smpMod_present().value(true);
        original.smpRate().value((short) 80);
        original.optFlds().refresh_time().value(true);
        original.dstAddress().addr().value(new byte[]{0x01, 0x0C, (byte)0xCD, 0x01, 0x00, 0x01});
        original.dstAddress().priority().value((byte) 4);
        original.dstAddress_present().value(true);

        byte[] data = original.encode();
        CmsMsvcb decoded = new CmsMsvcb().decode(data);

        assertEquals(true, decoded.svEna().value());
        assertEquals("MSVCB_01", new String(decoded.msvID().value()).trim());
        assertEquals(1, decoded.confRev().value());
        assertEquals(CmsSmpMod.SAMPLES_PER_SECOND, decoded.smpMod().value());
        assertEquals(true, decoded.smpMod_present().value());
        assertEquals((short) 80, decoded.smpRate().value());
        assertEquals(true, decoded.optFlds().refresh_time().value());
        assertEquals(true, decoded.dstAddress_present().value());
    }
}
