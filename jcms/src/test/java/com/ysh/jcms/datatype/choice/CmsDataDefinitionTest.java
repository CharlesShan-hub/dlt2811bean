package com.ysh.jcms.datatype.choice;

import com.ysh.jcms.datatype.common.CmsServiceError;
import static com.ysh.jcms.datatype.choice.CmsDataType.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDataDefinition")
class CmsDataDefinitionTest {

    private CmsDataDefinition get() { return (CmsDataDefinition)(new CmsDataDefinition().test()); }

    @Test
    void type0Error() {
        CmsDataDefinition original = (CmsDataDefinition) new CmsDataDefinition().test();
        original.choice().value(ERROR);
        original.value.error.value(CmsServiceError.NO_ERROR);

        CmsDataDefinition decoded = get().decode(original.encode());
        assertEquals(ERROR, decoded.choice().value());
        assertEquals(CmsServiceError.NO_ERROR, decoded.value.error.value());
    }

    @Test
    void type0ErrorWithValue() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.ofError(CmsServiceError.INSTANCE_IN_USE).test();

        CmsDataDefinition decoded = get().decode(original.encode());
        assertEquals(ERROR, decoded.choice().value());
        assertEquals(CmsServiceError.INSTANCE_IN_USE, decoded.value.error.value());
    }

    @Test
    void type3Boolean() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(BOOLEAN).test();
        assertEquals(BOOLEAN, get().decode(original.encode()).choice().value());
    }

    @Test
    void type6Int32() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(INT32).test();
        assertEquals(INT32, get().decode(original.encode()).choice().value());
    }

    @Test
    void type10Int32U() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(INT32U).test();
        assertEquals(INT32U, get().decode(original.encode()).choice().value());
    }

    @Test
    void type12Float32() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(FLOAT32).test();
        assertEquals(FLOAT32, get().decode(original.encode()).choice().value());
    }

    @Test
    void type13Float64() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(FLOAT64).test();
        assertEquals(FLOAT64, get().decode(original.encode()).choice().value());
    }

    @Test
    void type14VisibleString() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(VISIBLE_STRING).test();
        assertEquals(VISIBLE_STRING, get().decode(original.encode()).choice().value());
    }

    @Test
    void type14VisibleStringWithLength() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(VISIBLE_STRING, 64).test();

        CmsDataDefinition decoded = get().decode(original.encode());
        assertEquals(VISIBLE_STRING, decoded.choice().value());
        assertEquals(64, decoded.value.string_length.value());
    }

    @Test
    void type15OctetString() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(OCTET_STRING).test();
        assertEquals(OCTET_STRING, get().decode(original.encode()).choice().value());
    }

    @Test
    void type18UtcTime() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(UTC_TIME).test();
        assertEquals(UTC_TIME, get().decode(original.encode()).choice().value());
    }

    @Test
    void type20Quality() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(QUALITY).test();
        assertEquals(QUALITY, get().decode(original.encode()).choice().value());
    }

    @Test
    void type23Check() {
        CmsDataDefinition original = (CmsDataDefinition) CmsDataDefinition.of(CHECK).test();
        assertEquals(CHECK, get().decode(original.encode()).choice().value());
    }
}
