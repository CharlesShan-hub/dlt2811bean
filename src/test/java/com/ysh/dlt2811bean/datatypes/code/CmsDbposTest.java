package com.ysh.dlt2811bean.datatypes.code;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDbpos")
class CmsDbposTest {

    @Test
    @DisplayName("default constructor sets INTERMEDIATE")
    void defaultConstructor() {
        CmsDbpos dbpos = new CmsDbpos();
        assertEquals(CmsDbpos.INTERMEDIATE, (long) dbpos.get());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);
        assertEquals(CmsDbpos.ON, (long) dbpos.get());
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsDbpos dbpos = new CmsDbpos();
        dbpos.set((long) CmsDbpos.OFF);
        assertEquals(CmsDbpos.OFF, (long) dbpos.get());
    }

    @Test
    @DisplayName("set with long value")
    void setWithLongValue() {
        CmsDbpos dbpos = new CmsDbpos();
        dbpos.set(3L);
        assertEquals(CmsDbpos.BAD, (long) dbpos.get());
    }

    @Test
    @DisplayName("encode/decode INTERMEDIATE")
    void encodeDecodeIntermediate() throws Exception {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.INTERMEDIATE);

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.INTERMEDIATE, (long) decoded.get());
    }

    @Test
    @DisplayName("encode/decode OFF")
    void encodeDecodeOff() throws Exception {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.OFF);

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.OFF, (long) decoded.get());
    }

    @Test
    @DisplayName("encode/decode ON")
    void encodeDecodeOn() throws Exception {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.ON, (long) decoded.get());
    }

    @Test
    @DisplayName("encode/decode BAD")
    void encodeDecodeBad() throws Exception {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.BAD);

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.BAD, (long) decoded.get());
    }

    @Test
    @DisplayName("encode/decode all values sequentially")
    void encodeDecodeAllValuesSequentially() throws Exception {
        PerOutputStream pos = new PerOutputStream();

        new CmsDbpos(CmsDbpos.INTERMEDIATE).encode(pos);
        new CmsDbpos(CmsDbpos.OFF).encode(pos);
        new CmsDbpos(CmsDbpos.ON).encode(pos);
        new CmsDbpos(CmsDbpos.BAD).encode(pos);

        PerInputStream pis = new PerInputStream(pos.toByteArray());

        CmsDbpos decoded1 = new CmsDbpos().decode(pis);
        CmsDbpos decoded2 = new CmsDbpos().decode(pis);
        CmsDbpos decoded3 = new CmsDbpos().decode(pis);
        CmsDbpos decoded4 = new CmsDbpos().decode(pis);

        assertEquals(CmsDbpos.INTERMEDIATE, (long) decoded1.get());
        assertEquals(CmsDbpos.OFF, (long) decoded2.get());
        assertEquals(CmsDbpos.ON, (long) decoded3.get());
        assertEquals(CmsDbpos.BAD, (long) decoded4.get());
    }

    @Test
    @DisplayName("set out of range value throws exception")
    void setOutOfRangeValueThrowsException() {
        CmsDbpos dbpos = new CmsDbpos();
        assertThrows(IllegalArgumentException.class, () -> dbpos.set(-1L));
        assertThrows(IllegalArgumentException.class, () -> dbpos.set(4L));
        assertThrows(IllegalArgumentException.class, () -> dbpos.set(100L));
    }

    @Test
    @DisplayName("toString format")
    void toStringFormat() {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);
        assertEquals("(CmsDbpos) 2", dbpos.toString());
    }

    @Test
    @DisplayName("static write and read")
    void staticWriteRead() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsDbpos.write(pos, CmsDbpos.ON);

        CmsDbpos decoded = CmsDbpos.read(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.ON, (long) decoded.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsDbpos dbpos = new CmsDbpos()
                .set((long) CmsDbpos.OFF);

        assertEquals(CmsDbpos.OFF, (long) dbpos.get());

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.OFF, (long) decoded.get());
    }

    @Test
    @DisplayName("static constants are correct")
    void staticConstantsAreCorrect() {
        assertEquals(0, CmsDbpos.INTERMEDIATE);
        assertEquals(1, CmsDbpos.OFF);
        assertEquals(2, CmsDbpos.ON);
        assertEquals(3, CmsDbpos.BAD);
    }

    @Test
    @DisplayName("get returns Long object")
    void getReturnsLongObject() {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);
        Long value = dbpos.get();
        assertEquals(Long.valueOf(2L), value);
        assertEquals(2L, value.longValue());
    }
}
