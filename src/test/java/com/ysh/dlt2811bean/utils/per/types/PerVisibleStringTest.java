package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerVisibleString")
class PerVisibleStringTest {

    // ==================== Fixed-size ====================

    @Test
    @DisplayName("fixed-size: exact fit")
    void fixedSize_exact() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerVisibleString.encodeFixedSize(pos, "hello", 5);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("hello", PerVisibleString.decodeFixedSize(pis, 5));
    }

    @Test
    @DisplayName("fixed-size: shorter string padded with spaces")
    void fixedSize_padded() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerVisibleString.encodeFixedSize(pos, "hi", 5);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("hi", PerVisibleString.decodeFixedSize(pis, 5));
    }

    @Test
    @DisplayName("fixed-size: 129 chars (serverAccessPointReference)")
    void fixedSize_129() throws PerDecodeException {
        String ref = "S1.AccessPoint1";
        PerOutputStream pos = new PerOutputStream();
        PerVisibleString.encodeFixedSize(pos, ref, 129);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(ref, PerVisibleString.decodeFixedSize(pis, 129));
    }

    @Test
    @DisplayName("fixed-size: 0 chars")
    void fixedSize_0() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerVisibleString.encodeFixedSize(pos, "", 0);

        PerInputStream pis = new PerInputStream(new byte[0]);
        assertEquals("", PerVisibleString.decodeFixedSize(pis, 0));
    }

    // ==================== Fixed-size with FROM constraint ====================

    @Test
    @DisplayName("FROM constrained: digits only")
    void fromConstraint_digits() throws PerDecodeException {
        String charset = "0123456789";
        PerOutputStream pos = new PerOutputStream();
        PerVisibleString.encodeFixedSizeConstrained(pos, "12345", 5, charset);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("12345", PerVisibleString.decodeFixedSizeConstrained(pis, 5, charset));
    }

    // ==================== Variable-size (constrained) ====================

    @Test
    @DisplayName("constrained: object reference")
    void constrained_objRef() throws PerDecodeException {
        String objRef = "LD1/LN0.DO1.DA1";
        PerOutputStream pos = new PerOutputStream();
        PerVisibleString.encodeConstrained(pos, objRef, 0, 255);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(objRef, PerVisibleString.decodeConstrained(pis, 0, 255));
    }

    @Test
    @DisplayName("constrained: empty string")
    void constrained_empty() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerVisibleString.encodeConstrained(pos, "", 0, 255);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("", PerVisibleString.decodeConstrained(pis, 0, 255));
    }

    // ==================== Unconstrained ====================

    @Test
    @DisplayName("unconstrained: any string")
    void unconstrained() throws PerDecodeException {
        String value = "any length string";
        PerOutputStream pos = new PerOutputStream();
        PerVisibleString.encodeUnconstrained(pos, value);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(value, PerVisibleString.decodeUnconstrained(pis));
    }

    @Test
    @DisplayName("unconstrained: empty string")
    void unconstrained_empty() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerVisibleString.encodeUnconstrained(pos, "");

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("", PerVisibleString.decodeUnconstrained(pis));
    }

    // ==================== Mixed ====================

    @Test
    @DisplayName("mixed: uint8 + visibleString(129) + boolean")
    void mixed() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encodeUint8(pos, 1);
        PerVisibleString.encodeFixedSize(pos, "S1", 129);
        PerBoolean.encode(pos, true);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(1, PerInteger.decodeUint8(pis));
        assertEquals("S1", PerVisibleString.decodeFixedSize(pis, 129));
        assertTrue(PerBoolean.decode(pis));
    }
}
