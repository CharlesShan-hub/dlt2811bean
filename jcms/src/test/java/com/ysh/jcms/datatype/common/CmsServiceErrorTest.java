package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsServiceError")
class CmsServiceErrorTest {

    @Test
    void noError() {
        CmsServiceError original = new CmsServiceError().value(CmsServiceError.NO_ERROR);
        assertEquals(original, new CmsServiceError().decode(original.encode()));
    }

    @Test
    void instanceNotAvailable() {
        CmsServiceError original = new CmsServiceError().value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        assertEquals(original, new CmsServiceError().decode(original.encode()));
    }

    @Test
    void accessViolation() {
        CmsServiceError original = new CmsServiceError().value(CmsServiceError.ACCESS_VIOLATION);
        assertEquals(original, new CmsServiceError().decode(original.encode()));
    }

    @Test
    void maxValue() {
        CmsServiceError original = new CmsServiceError().value(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        assertEquals(original, new CmsServiceError().decode(original.encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsServiceError().value());
    }

    @Test
    void decodeOverwrites() {
        CmsServiceError target = new CmsServiceError().value(CmsServiceError.ACCESS_VIOLATION);
        target.decode(new CmsServiceError().value(CmsServiceError.INSTANCE_NOT_AVAILABLE).encode());
        assertEquals(new CmsServiceError().value(CmsServiceError.INSTANCE_NOT_AVAILABLE), target);
    }
}
