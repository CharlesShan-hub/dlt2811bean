package com.ysh.jcms.datatype.choice;

import com.ysh.jcms.datatype.common.CmsServiceError;
import static com.ysh.jcms.datatype.choice.CmsDataType.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDataDefinition")
class CmsDataDefinitionTest {

    @Test
    void type0Error() {
        CmsDataDefinition original = new CmsDataDefinition();
        original.choice().value(ERROR);
        original.value.error.value(CmsServiceError.NO_ERROR);

        CmsDataDefinition decoded = new CmsDataDefinition().decode(original.encode());
        assertEquals(ERROR, decoded.choice().value());
        assertEquals(CmsServiceError.NO_ERROR, decoded.value.error.value());
    }

    @Test
    void type0ErrorWithValue() {
        CmsDataDefinition original = CmsDataDefinition.ofError(CmsServiceError.INSTANCE_IN_USE);

        CmsDataDefinition decoded = new CmsDataDefinition().decode(original.encode());
        assertEquals(ERROR, decoded.choice().value());
        assertEquals(CmsServiceError.INSTANCE_IN_USE, decoded.value.error.value());
    }

    @Test
    void type3Boolean() {
        CmsDataDefinition original = CmsDataDefinition.of(BOOLEAN);
        assertEquals(BOOLEAN, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void type6Int32() {
        CmsDataDefinition original = CmsDataDefinition.of(INT32);
        assertEquals(INT32, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void type10Int32U() {
        CmsDataDefinition original = CmsDataDefinition.of(INT32U);
        assertEquals(INT32U, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void type12Float32() {
        CmsDataDefinition original = CmsDataDefinition.of(FLOAT32);
        assertEquals(FLOAT32, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void type13Float64() {
        CmsDataDefinition original = CmsDataDefinition.of(FLOAT64);
        assertEquals(FLOAT64, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void type14VisibleString() {
        CmsDataDefinition original = CmsDataDefinition.of(VISIBLE_STRING);
        assertEquals(VISIBLE_STRING, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void type14VisibleStringWithLength() {
        CmsDataDefinition original = CmsDataDefinition.of(VISIBLE_STRING, 64);

        CmsDataDefinition decoded = new CmsDataDefinition().decode(original.encode());
        assertEquals(VISIBLE_STRING, decoded.choice().value());
        assertEquals(64, decoded.value.string_length.value());
    }

    @Test
    void type15OctetString() {
        CmsDataDefinition original = CmsDataDefinition.of(OCTET_STRING);
        assertEquals(OCTET_STRING, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void type18UtcTime() {
        CmsDataDefinition original = CmsDataDefinition.of(UTC_TIME);
        assertEquals(UTC_TIME, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void type20Quality() {
        CmsDataDefinition original = CmsDataDefinition.of(QUALITY);
        assertEquals(QUALITY, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void type23Check() {
        CmsDataDefinition original = CmsDataDefinition.of(CHECK);
        assertEquals(CHECK, new CmsDataDefinition().decode(original.encode()).choice().value());
    }

    @Test
    void defaultChoiceIsZero() {
        CmsDataDefinition d = new CmsDataDefinition();
        assertEquals(0, d.choice().value());
    }
}
