package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFloat32")
class CmsFloat32Test {

    @Test
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat32.encode(pos, 3.14f);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(3.14f, CmsFloat32.decode(pis).getValue(), 0.001f);
    }

    @Test
    @DisplayName("negative value")
    void negative() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat32.encode(pos, -100.5f);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(-100.5f, CmsFloat32.decode(pis).getValue(), 0.001f);
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat32.encode(pos, 0.0f);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0.0f, CmsFloat32.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero uses only 1 bit")
    void zero_oneBit() {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat32.encode(pos, 0.0f);
        assertEquals(1, pos.getBitLength());
    }

    @Test
    @DisplayName("non-zero uses 1 bit + 4 bytes (aligned)")
    void nonZero_size() {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat32.encode(pos, 1.0f);
        // 1 bit (flag) + align to byte (7 pad) + 4 bytes = 40 bits
        assertEquals(40, pos.getBitLength());
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsFloat32.encode(pos, new CmsFloat32(3.14f));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(3.14f, CmsFloat32.decode(pis).getValue(), 0.001f);
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsFloat32 val = new CmsFloat32().setValue(1.5f);
        assertEquals(1.5f, val.getValue());
    }
}
