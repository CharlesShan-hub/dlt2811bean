package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CmsEntryIDTest {

    @Test
    void encodeDecode_basic() throws Exception {
        byte[] id = {(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, (byte) 0x08};
        PerOutputStream pos = new PerOutputStream();
        CmsEntryID.encode(pos, id);
        byte[] result = CmsEntryID.decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(id, result);
    }

    @Test
    void encodeDecode_allZeros() throws Exception {
        byte[] id = new byte[8];
        PerOutputStream pos = new PerOutputStream();
        CmsEntryID.encode(pos, id);
        byte[] result = CmsEntryID.decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(id, result);
    }

    @Test
    void encodeDecode_allOnes() throws Exception {
        byte[] id = new byte[8];
        Arrays.fill(id, (byte) 0xFF);
        PerOutputStream pos = new PerOutputStream();
        CmsEntryID.encode(pos, id);
        byte[] result = CmsEntryID.decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(id, result);
    }
}
