package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSgcb")
class CmsSgcbTest {

    private CmsSgcb get() { return (CmsSgcb)(new CmsSgcb().test()); }

    @Test
    void roundtrip() {
        CmsSgcb original = get();
        original.numOfSG().value((byte) 4);
        original.actSG().value((byte) 2);
        original.editSG().value((byte) 1);
        original.resvTms().value((short) 500);
        original.resvTms_present().value(true);

        CmsSgcb decoded = get().decode(original.encode());
        assertEquals((byte) 4, decoded.numOfSG().value());
        assertEquals((byte) 2, decoded.actSG().value());
        assertEquals((byte) 1, decoded.editSG().value());
        assertEquals((short) 500, decoded.resvTms().value());
        assertEquals(true, decoded.resvTms_present().value());
    }

    @Test
    void resvTmsNotPresent() {
        CmsSgcb original = get();
        original.numOfSG().value((byte) 8);
        original.actSG().value((byte) 1);

        CmsSgcb decoded = get().decode(original.encode());
        assertEquals((byte) 8, decoded.numOfSG().value());
        assertEquals(false, decoded.resvTms_present().value());
    }
}
