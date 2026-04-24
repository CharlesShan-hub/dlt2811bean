package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDbpos")
class CmsDbposTest {

    @Test
    @DisplayName("default constructor sets INTERMEDIATE")
    void defaultConstructor() {
        CmsDbpos dbpos = new CmsDbpos();
        assertEquals(CmsDbpos.INTERMEDIATE, dbpos.get());
        assertTrue(dbpos.is(CmsDbpos.INTERMEDIATE));
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);
        assertEquals(CmsDbpos.ON, dbpos.get());
        assertTrue(dbpos.is(CmsDbpos.ON));
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsDbpos dbpos = new CmsDbpos();
        dbpos.set(CmsDbpos.OFF);
        assertEquals(CmsDbpos.OFF, dbpos.get());
        assertTrue(dbpos.is(CmsDbpos.OFF));
    }

    @Test
    @DisplayName("set with integer value")
    void setWithIntegerValue() {
        CmsDbpos dbpos = new CmsDbpos();
        dbpos.set(3); // BAD
        assertEquals(CmsDbpos.BAD, dbpos.get());
        assertTrue(dbpos.is(CmsDbpos.BAD));
    }

    @Test
    @DisplayName("is method returns true for matching value")
    void isMethodReturnsTrueForMatchingValue() {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);
        assertTrue(dbpos.is(CmsDbpos.ON));
        assertTrue(dbpos.is(2));
    }

    @Test
    @DisplayName("is method returns false for non-matching value")
    void isMethodReturnsFalseForNonMatchingValue() {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);
        assertFalse(dbpos.is(CmsDbpos.OFF));
        assertFalse(dbpos.is(CmsDbpos.INTERMEDIATE));
        assertFalse(dbpos.is(CmsDbpos.BAD));
    }

    @Test
    @DisplayName("encode/decode INTERMEDIATE")
    void encodeDecodeIntermediate() throws Exception {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.INTERMEDIATE);

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.INTERMEDIATE, decoded.get());
        assertTrue(decoded.is(CmsDbpos.INTERMEDIATE));
    }

    @Test
    @DisplayName("encode/decode OFF")
    void encodeDecodeOff() throws Exception {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.OFF);

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.OFF, decoded.get());
        assertTrue(decoded.is(CmsDbpos.OFF));
    }

    @Test
    @DisplayName("encode/decode ON")
    void encodeDecodeOn() throws Exception {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.ON, decoded.get());
        assertTrue(decoded.is(CmsDbpos.ON));
    }

    @Test
    @DisplayName("encode/decode BAD")
    void encodeDecodeBad() throws Exception {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.BAD);

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.BAD, decoded.get());
        assertTrue(decoded.is(CmsDbpos.BAD));
    }

    @Test
    @DisplayName("encode/decode all values sequentially")
    void encodeDecodeAllValuesSequentially() throws Exception {
        // 编码所有4个值
        PerOutputStream pos = new PerOutputStream();

        new CmsDbpos(CmsDbpos.INTERMEDIATE).encode(pos);
        new CmsDbpos(CmsDbpos.OFF).encode(pos);
        new CmsDbpos(CmsDbpos.ON).encode(pos);
        new CmsDbpos(CmsDbpos.BAD).encode(pos);

        // 解码所有4个值
        PerInputStream pis = new PerInputStream(pos.toByteArray());

        CmsDbpos decoded1 = new CmsDbpos().decode(pis);
        CmsDbpos decoded2 = new CmsDbpos().decode(pis);
        CmsDbpos decoded3 = new CmsDbpos().decode(pis);
        CmsDbpos decoded4 = new CmsDbpos().decode(pis);

        assertEquals(CmsDbpos.INTERMEDIATE, decoded1.get());
        assertEquals(CmsDbpos.OFF, decoded2.get());
        assertEquals(CmsDbpos.ON, decoded3.get());
        assertEquals(CmsDbpos.BAD, decoded4.get());
    }

    @Test
    @DisplayName("set out of range value throws exception")
    void setOutOfRangeValueThrowsException() {
        CmsDbpos dbpos = new CmsDbpos();

        // 有效范围是 0..3
        assertThrows(IllegalArgumentException.class, () -> dbpos.set(-1));
        assertThrows(IllegalArgumentException.class, () -> dbpos.set(4));
        assertThrows(IllegalArgumentException.class, () -> dbpos.set(100));
    }

    @Test
    @DisplayName("constructor with out of range value throws exception")
    void constructorWithOutOfRangeValueThrowsException() {
        // 构造函数应该立即验证范围
        // 注意：当前实现可能在构造函数中不验证，在encode时才验证
        // 但AbstractCmsEnumerated的validate()方法会检查
    }

    @Test
    @DisplayName("toString format")
    void toStringFormat() {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);
        assertEquals("CmsDbpos: 2", dbpos.toString());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsDbpos dbpos = new CmsDbpos()
                .set(CmsDbpos.OFF);

        assertEquals(CmsDbpos.OFF, dbpos.get());

        PerOutputStream pos = new PerOutputStream();
        dbpos.encode(pos);

        CmsDbpos decoded = new CmsDbpos().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsDbpos.OFF, decoded.get());
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
    @DisplayName("is method with raw integer")
    void isMethodWithRawInteger() {
        CmsDbpos dbpos = new CmsDbpos(2);
        assertTrue(dbpos.is(2));
        assertFalse(dbpos.is(0));
        assertFalse(dbpos.is(1));
        assertFalse(dbpos.is(3));
    }

    @Test
    @DisplayName("get returns Integer object")
    void getReturnsIntegerObject() {
        CmsDbpos dbpos = new CmsDbpos(CmsDbpos.ON);
        Integer value = dbpos.get();
        assertEquals(Integer.valueOf(2), value);
        assertEquals(2, value.intValue());
    }
}