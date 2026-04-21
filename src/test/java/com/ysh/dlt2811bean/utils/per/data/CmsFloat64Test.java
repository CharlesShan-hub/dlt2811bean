package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFloat64")
class CmsFloat64Test {

    @Test
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat64.encode(pos, 220.5);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(220.5, CmsFloat64.decode(pis).getValue());
    }

    @Test
    @DisplayName("negative value")
    void negative() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat64.encode(pos, -3.14);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(-3.14, CmsFloat64.decode(pis).getValue(), 1e-10);
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat64.encode(pos, 0.0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0.0, CmsFloat64.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero uses only 1 bit")
    void zero_oneBit() {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat64.encode(pos, 0.0);
        assertEquals(1, pos.getBitLength());
    }

    @Test
    @DisplayName("non-zero uses 1 bit + 8 bytes (aligned)")
    void nonZero_size() {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat64.encode(pos, 1.0);
        // 1 bit (flag) + align to byte (7 pad) + 8 bytes = 72 bits
        assertEquals(72, pos.getBitLength());
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat64.encode(pos, new CmsFloat64(220.5));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(220.5, CmsFloat64.decode(pis).getValue());
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsFloat64 val = new CmsFloat64().setValue(3.14);
        assertEquals(3.14, val.getValue());
    }
}
