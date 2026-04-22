package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsServiceErrorTest {

    @Test
    void encodeDecode_noError() throws Exception {
        CmsServiceError err = new CmsServiceError(CmsServiceError.NO_ERROR);
        PerOutputStream pos = new PerOutputStream();
        CmsServiceError.encode(pos, err);
        CmsServiceError r = CmsServiceError.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsServiceError.NO_ERROR, r.getValue());
        assertTrue(r.isNoError());
    }

    @Test
    void encodeDecode_accessViolation() throws Exception {
        CmsServiceError err = new CmsServiceError(CmsServiceError.ACCESS_VIOLATION);
        PerOutputStream pos = new PerOutputStream();
        CmsServiceError.encode(pos, err);
        CmsServiceError r = CmsServiceError.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsServiceError.ACCESS_VIOLATION, r.getValue());
        assertTrue(r.isAccessViolation());
    }

    @Test
    void encodeDecode_serverConstraint() throws Exception {
        CmsServiceError err = new CmsServiceError(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        PerOutputStream pos = new PerOutputStream();
        CmsServiceError.encode(pos, err);
        CmsServiceError r = CmsServiceError.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(12, r.getValue());
    }

    @Test
    void chainSetter() {
        CmsServiceError err = new CmsServiceError();
        err.setClassNotSupported();
        assertEquals(CmsServiceError.CLASS_NOT_SUPPORTED, err.getValue());
        assertTrue(err.isClassNotSupported());
    }

    @Test
    void constructor_rejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new CmsServiceError(-1));
        assertThrows(IllegalArgumentException.class, () -> new CmsServiceError(13));
    }
}
