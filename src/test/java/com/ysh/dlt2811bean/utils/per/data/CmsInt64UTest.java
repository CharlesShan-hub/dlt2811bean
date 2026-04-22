package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64U")
class CmsInt64UTest {

    @Test
    @DisplayName("positive value with long")
    void positiveLong() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, 123456789012345L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(BigInteger.valueOf(123456789012345L), CmsInt64U.decode(pis).getValue());
    }

    @Test
    @DisplayName("positive value with BigInteger")
    void positiveBigInteger() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        BigInteger value = new BigInteger("12345678901234567890");
        CmsInt64U.encode(pos, value);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(value, CmsInt64U.decode(pis).getValue());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, CmsInt64U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt64U.MIN, CmsInt64U.decode(pis).getValue());
    }

    @Test
    @DisplayName("maximum value (2^64-1)")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, CmsInt64U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt64U.MAX, CmsInt64U.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, 0L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(BigInteger.ZERO, CmsInt64U.decode(pis).getValue());
    }

    @Test
    @DisplayName("value below minimum throws exception (long)")
    void belowMinLong() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.encode(pos, -1L));
    }

    @Test
    @DisplayName("value below minimum throws exception (BigInteger)")
    void belowMinBigInteger() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.encode(pos, BigInteger.valueOf(-1)));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        BigInteger tooLarge = CmsInt64U.MAX.add(BigInteger.ONE);
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.encode(pos, tooLarge));
    }

    @Test
    @DisplayName("validateValue method for long")
    void validateValueLong() {
        assertDoesNotThrow(() -> CmsInt64U.validateValue(0L));
        assertDoesNotThrow(() -> CmsInt64U.validateValue(100L));
        
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.validateValue(-1L));
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.validateValue(-100L));
    }

    @Test
    @DisplayName("validateValue method for BigInteger")
    void validateValueBigInteger() {
        assertDoesNotThrow(() -> CmsInt64U.validateValue(CmsInt64U.MIN));
        assertDoesNotThrow(() -> CmsInt64U.validateValue(CmsInt64U.MAX));
        assertDoesNotThrow(() -> CmsInt64U.validateValue(new BigInteger("12345678901234567890")));
        
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.validateValue(BigInteger.valueOf(-1)));
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.validateValue(CmsInt64U.MAX.add(BigInteger.ONE)));
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, new CmsInt64U(123456789012345L));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(BigInteger.valueOf(123456789012345L), CmsInt64U.decode(pis).getValue());
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsInt64U val = new CmsInt64U().setValue(BigInteger.valueOf(123456789012345L));
        assertEquals(BigInteger.valueOf(123456789012345L), val.getValue());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsInt64U val = new CmsInt64U();
        assertEquals(BigInteger.ZERO, val.getValue());
    }

    @Test
    @DisplayName("constructor with long value")
    void constructorWithLongValue() {
        CmsInt64U val = new CmsInt64U(123456789012345L);
        assertEquals(BigInteger.valueOf(123456789012345L), val.getValue());
    }

    @Test
    @DisplayName("constructor with BigInteger value")
    void constructorWithBigIntegerValue() {
        BigInteger value = new BigInteger("12345678901234567890");
        CmsInt64U val = new CmsInt64U(value);
        assertEquals(value, val.getValue());
    }

    @Test
    @DisplayName("constructor validates range for long")
    void constructorValidatesRangeLong() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt64U(-1L));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt64U(-100L));
        
        assertDoesNotThrow(() -> new CmsInt64U(0L));
        assertDoesNotThrow(() -> new CmsInt64U(100L));
    }

    @Test
    @DisplayName("constructor validates range for BigInteger")
    void constructorValidatesRangeBigInteger() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt64U(BigInteger.valueOf(-1)));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt64U(CmsInt64U.MAX.add(BigInteger.ONE)));
        
        assertDoesNotThrow(() -> new CmsInt64U(CmsInt64U.MIN));
        assertDoesNotThrow(() -> new CmsInt64U(CmsInt64U.MAX));
    }

    @Test
    @DisplayName("toString method")
    void toStringMethod() {
        CmsInt64U val = new CmsInt64U(123456789012345L);
        assertEquals("123456789012345", val.toString());
    }

    @Test
    @DisplayName("decode full range bytes (0xFFFFFFFFFFFFFFFF)")
    void decodeFullRange() throws PerDecodeException {
        byte[] allOnes = new byte[] {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
                                     (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        PerInputStream pis = new PerInputStream(allOnes);
        CmsInt64U decoded = CmsInt64U.decode(pis);
        
        assertEquals(CmsInt64U.MAX, decoded.getValue());
        assertEquals("18446744073709551615", decoded.toString());
    }

    @Test
    @DisplayName("encode and decode full range")
    void encodeDecodeFullRange() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, CmsInt64U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        CmsInt64U decoded = CmsInt64U.decode(pis);
        
        assertEquals(CmsInt64U.MAX, decoded.getValue());
    }
}