package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAuthenticationParameter")
class CmsAuthenticationParameterTest {

    @Test
    void roundtrip() {
        CmsAuthenticationParameter p = new CmsAuthenticationParameter();
        p.cert = new byte[]{0x01, 0x02, 0x03};
        p.cert_len = 3;
        p.signed_time_ms = 1700000000000L;
        p.sig_val = new byte[]{(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD};
        p.sig_len = 4;

        byte[] enc = p.encode();
        CmsAuthenticationParameter dec = CmsAuthenticationParameter.decode(enc);

        assertArrayEquals(p.cert, dec.cert);
        assertEquals(p.cert_len, dec.cert_len);
        assertEquals(p.signed_time_ms, dec.signed_time_ms);
        assertArrayEquals(p.sig_val, dec.sig_val);
        assertEquals(p.sig_len, dec.sig_len);
    }

    @Test
    void empty() {
        CmsAuthenticationParameter p = new CmsAuthenticationParameter();
        p.cert = new byte[0];
        p.cert_len = 0;
        p.signed_time_ms = 0;
        p.sig_val = new byte[0];
        p.sig_len = 0;

        byte[] enc = p.encode();
        CmsAuthenticationParameter dec = CmsAuthenticationParameter.decode(enc);

        assertEquals(0, dec.cert_len);
        assertEquals(0, dec.sig_len);
        assertEquals(0, dec.signed_time_ms);
    }

    @Test
    void copy() {
        CmsAuthenticationParameter p = new CmsAuthenticationParameter();
        p.cert = new byte[]{0x10, 0x20};
        p.cert_len = 2;
        p.signed_time_ms = 999888777L;
        p.sig_val = new byte[]{0x30};
        p.sig_len = 1;

        CmsAuthenticationParameter c = p.copy();
        assertArrayEquals(p.cert, c.cert);
        assertEquals(p.signed_time_ms, c.signed_time_ms);
        assertArrayEquals(p.sig_val, c.sig_val);
        assertNotSame(p, c);
    }
}
