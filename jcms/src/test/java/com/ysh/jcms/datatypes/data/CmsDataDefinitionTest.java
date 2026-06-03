package com.ysh.jcms.datatypes.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDataDefinition")
class CmsDataDefinitionTest {

    @Test
    void errorRoundtrip() {
        byte[] enc = CmsDataDefinition.createError(5).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.ERROR, dec.choice());
        assertEquals(5, dec.serviceError());
    }

    @Test
    void errorZero() {
        byte[] enc = CmsDataDefinition.createError(0).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.ERROR, dec.choice());
        assertEquals(0, dec.serviceError());
    }

    @Test
    void tagOnlyBoolean() {
        byte[] enc = CmsDataDefinition.createTagOnly(CmsDataDefinition.BOOLEAN).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.BOOLEAN, dec.choice());
        assertTrue(dec.isTagOnly());
    }

    @Test
    void tagOnlyInt8() {
        byte[] enc = CmsDataDefinition.createTagOnly(CmsDataDefinition.INT8).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.INT8, dec.choice());
        assertTrue(dec.isTagOnly());
    }

    @Test
    void tagOnlyInt32() {
        byte[] enc = CmsDataDefinition.createTagOnly(CmsDataDefinition.INT32).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.INT32, dec.choice());
        assertTrue(dec.isTagOnly());
    }

    @Test
    void tagOnlyFloat64() {
        byte[] enc = CmsDataDefinition.createTagOnly(CmsDataDefinition.FLOAT64).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.FLOAT64, dec.choice());
        assertTrue(dec.isTagOnly());
    }

    @Test
    void tagOnlyUtcTime() {
        byte[] enc = CmsDataDefinition.createTagOnly(CmsDataDefinition.UTC_TIME).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.UTC_TIME, dec.choice());
        assertTrue(dec.isTagOnly());
    }

    @Test
    void tagOnlyQuality() {
        byte[] enc = CmsDataDefinition.createTagOnly(CmsDataDefinition.QUALITY).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.QUALITY, dec.choice());
        assertTrue(dec.isTagOnly());
    }

    @Test
    void tagOnlyCheck() {
        byte[] enc = CmsDataDefinition.createTagOnly(CmsDataDefinition.CHECK).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.CHECK, dec.choice());
        assertTrue(dec.isTagOnly());
    }

    @Test
    void bitStringRoundtrip() {
        byte[] enc = CmsDataDefinition.createStringType(CmsDataDefinition.BIT_STRING, 13).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.BIT_STRING, dec.choice());
        assertEquals(13, dec.stringLength());
    }

    @Test
    void octetStringRoundtrip() {
        byte[] enc = CmsDataDefinition.createStringType(CmsDataDefinition.OCTET_STRING, 100).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.OCTET_STRING, dec.choice());
        assertEquals(100, dec.stringLength());
    }

    @Test
    void visibleStringRoundtrip() {
        byte[] enc = CmsDataDefinition.createStringType(CmsDataDefinition.VISIBLE_STRING, 64).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.VISIBLE_STRING, dec.choice());
        assertEquals(64, dec.stringLength());
    }

    @Test
    void utf8StringRoundtrip() {
        byte[] enc = CmsDataDefinition.createStringType(CmsDataDefinition.UTF8_STRING, 128).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.UTF8_STRING, dec.choice());
        assertEquals(128, dec.stringLength());
    }

    @Test
    void arrayRoundtrip() {
        CmsDataDefinition elemType = CmsDataDefinition.createTagOnly(CmsDataDefinition.INT32);
        byte[] enc = CmsDataDefinition.createArray(10, elemType).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.ARRAY, dec.choice());
        assertEquals(10, dec.numberOfElement());
        assertNotNull(dec.elementType());
        assertEquals(CmsDataDefinition.INT32, dec.elementType().choice());
    }

    @Test
    void arrayNestedRoundtrip() {
        CmsDataDefinition inner = CmsDataDefinition.createStructure(Arrays.asList(
            new CmsDataDefinitionMember("value", CmsDataDefinition.createTagOnly(CmsDataDefinition.FLOAT64))
        ));
        CmsDataDefinition outer = CmsDataDefinition.createArray(5, inner);
        byte[] enc = outer.encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.ARRAY, dec.choice());
        assertEquals(5, dec.numberOfElement());
        assertEquals(CmsDataDefinition.STRUCTURE, dec.elementType().choice());
        assertEquals(1, dec.elementType().members().size());
        assertEquals("value", dec.elementType().members().get(0).name());
        assertEquals(CmsDataDefinition.FLOAT64, dec.elementType().members().get(0).type().choice());
    }

    @Test
    void structureRoundtripSimple() {
        List<CmsDataDefinitionMember> members = Arrays.asList(
            new CmsDataDefinitionMember("mag", CmsDataDefinition.createTagOnly(CmsDataDefinition.FLOAT64)),
            new CmsDataDefinitionMember("angle", CmsDataDefinition.createTagOnly(CmsDataDefinition.INT32)),
            new CmsDataDefinitionMember("quality", CmsDataDefinition.createTagOnly(CmsDataDefinition.QUALITY))
        );
        byte[] enc = CmsDataDefinition.createStructure(members).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.STRUCTURE, dec.choice());

        List<CmsDataDefinitionMember> result = dec.members();
        assertEquals(3, result.size());

        assertEquals("mag", result.get(0).name());
        assertEquals(CmsDataDefinition.FLOAT64, result.get(0).type().choice());

        assertEquals("angle", result.get(1).name());
        assertEquals(CmsDataDefinition.INT32, result.get(1).type().choice());

        assertEquals("quality", result.get(2).name());
        assertEquals(CmsDataDefinition.QUALITY, result.get(2).type().choice());
    }

    @Test
    void structureWithFc() {
        List<CmsDataDefinitionMember> members = Arrays.asList(
            new CmsDataDefinitionMember("stVal", "MX", CmsDataDefinition.createTagOnly(CmsDataDefinition.INT32))
        );
        byte[] enc = CmsDataDefinition.createStructure(members).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.STRUCTURE, dec.choice());

        List<CmsDataDefinitionMember> result = dec.members();
        assertEquals(1, result.size());
        assertEquals("stVal", result.get(0).name());
        assertTrue(result.get(0).hasFc());
        assertEquals("MX", result.get(0).fc().trim());
        assertEquals(CmsDataDefinition.INT32, result.get(0).type().choice());
    }

    @Test
    void emptyStructureRoundtrip() {
        byte[] enc = CmsDataDefinition.createStructure(Arrays.asList()).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.STRUCTURE, dec.choice());
        assertTrue(dec.members().isEmpty());
    }

    @Test
    void nestedStructureAndArray() {
        // Structure with a nested array of strings
        CmsDataDefinitionMember nestedMember = new CmsDataDefinitionMember(
            "phasors",
            CmsDataDefinition.createArray(16,
                CmsDataDefinition.createStructure(Arrays.asList(
                    new CmsDataDefinitionMember("mag", CmsDataDefinition.createTagOnly(CmsDataDefinition.FLOAT64)),
                    new CmsDataDefinitionMember("angle", CmsDataDefinition.createTagOnly(CmsDataDefinition.FLOAT64))
                ))
            )
        );
        byte[] enc = CmsDataDefinition.createStructure(Arrays.asList(nestedMember)).encode();
        CmsDataDefinition dec = CmsDataDefinition.decode(enc);
        assertEquals(CmsDataDefinition.STRUCTURE, dec.choice());

        CmsDataDefinition phasors = dec.members().get(0).type();
        assertEquals(CmsDataDefinition.ARRAY, phasors.choice());
        assertEquals(16, phasors.numberOfElement());

        CmsDataDefinition innerStruct = phasors.elementType();
        assertEquals(CmsDataDefinition.STRUCTURE, innerStruct.choice());
        assertEquals(2, innerStruct.members().size());
        assertEquals("mag", innerStruct.members().get(0).name());
        assertEquals("angle", innerStruct.members().get(1).name());
    }

    @Test
    void copy() {
        List<CmsDataDefinitionMember> members = Arrays.asList(
            new CmsDataDefinitionMember("val", CmsDataDefinition.createTagOnly(CmsDataDefinition.INT32))
        );
        CmsDataDefinition original = CmsDataDefinition.createStructure(members);
        CmsDataDefinition cloned = original.copy();

        assertEquals(original.choice(), cloned.choice());
        assertEquals(1, original.members().size());
        assertEquals(1, cloned.members().size());
        assertNotSame(original.members().get(0), cloned.members().get(0));
        assertNotSame(original.members().get(0).type(), cloned.members().get(0).type());
    }
}
