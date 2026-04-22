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
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, new BigInteger("12345678901234567890"));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(new BigInteger("12345678901234567890"), CmsInt64U.decode(pis).get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, CmsInt64U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt64U.MIN, CmsInt64U.decode(pis).get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, CmsInt64U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt64U.MAX, CmsInt64U.decode(pis).get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, BigInteger.ZERO);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(BigInteger.ZERO, CmsInt64U.decode(pis).get());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.encode(pos, new BigInteger("-1")));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.encode(pos, new BigInteger("18446744073709551616")));
    }

    @Test
    @DisplayName("validateValue method")
    void validateValue() {
        CmsInt64U.validateValue(BigInteger.ZERO);
        CmsInt64U.validateValue(new BigInteger("9223372036854775807"));
        CmsInt64U.validateValue(CmsInt64U.MAX);

        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.validateValue(new BigInteger("-1")));
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.validateValue(new BigInteger("18446744073709551616")));
        assertThrows(IllegalArgumentException.class, () -> CmsInt64U.validateValue(null));
    }

    @Test
    @DisplayName("instance encode+decode")
    void instanceRoundTrip() throws PerDecodeException {
        CmsInt64U val = new CmsInt64U(new BigInteger("9876543210987654321"));

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt64U val2 = CmsInt64U.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(new BigInteger("9876543210987654321"), val2.get());
        assertEquals(val.get(), val2.get());
    }

    @Test
    @DisplayName("set method with null value throws exception")
    void setNullValue() {
        CmsInt64U val = new CmsInt64U(new BigInteger("12345678901234567890"));
        assertThrows(IllegalArgumentException.class, () -> val.set(null));
        assertEquals(new BigInteger("12345678901234567890"), val.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws PerDecodeException {
        CmsInt64U val = new CmsInt64U().set(new BigInteger("12345678901234567890"));
        assertEquals(new BigInteger("12345678901234567890"), val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt64U decoded = CmsInt64U.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(new BigInteger("12345678901234567890"), decoded.get());
    }

    @Test
    @DisplayName("static encode with CmsInt64U object")
    void staticEncodeObject() throws PerDecodeException {
        CmsInt64U val = new CmsInt64U(new BigInteger("5000000000000000000"));

        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, val);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(new BigInteger("5000000000000000000"), CmsInt64U.decode(pis).get());
    }

    @Test
    @DisplayName("static encode with null object")
    void staticEncodeNullObject() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64U.encode(pos, (CmsInt64U) null);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(BigInteger.ZERO, CmsInt64U.decode(pis).get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsInt64U val = new CmsInt64U(new BigInteger("12345678901234567890"));
        assertEquals("12345678901234567890", val.toString());

        CmsInt64U val2 = new CmsInt64U(BigInteger.ZERO);
        assertEquals("0", val2.toString());
    }
}