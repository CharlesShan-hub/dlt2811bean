package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsServiceError")
class CmsServiceErrorTest {

    private CmsServiceError get() { return (CmsServiceError)(new CmsServiceError().test()); }

    @Test
    void noError() {
        CmsServiceError a = get().value(CmsServiceError.NO_ERROR);
        CmsServiceError b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void instanceNotAvailable() {
        CmsServiceError a = get().value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        CmsServiceError b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void accessViolation() {
        CmsServiceError a = get().value(CmsServiceError.ACCESS_VIOLATION);
        CmsServiceError b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void maxValue() {
        CmsServiceError a = get().value(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        CmsServiceError b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, get().value());
    }

    @Test
    void decodeOverwrites() {
        CmsServiceError src = get().value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        CmsServiceError target = get().decode(src.encode());
        assertEquals(src, target);
    }
}
