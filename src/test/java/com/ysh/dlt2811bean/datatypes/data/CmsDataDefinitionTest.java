package com.ysh.dlt2811bean.datatypes.data;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition.StructureEntry;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDataDefinition")
class CmsDataDefinitionTest {

    private CmsDataDefinition roundtrip(CmsDataDefinition def) throws Exception {
        PerOutputStream pos = new PerOutputStream();
        def.encode(pos);
        return new CmsDataDefinition().decode(new PerInputStream(pos.toByteArray()));
    }

    // -------------------------------------------------------------------------
    // NULL-encoded basic types
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("boolean [3]")
    void booleanDef() throws Exception {
        CmsDataDefinition r = roundtrip(ofBoolean());
        assertEquals(BOOLEAN, r.getChoiceIndex());
    }

    @Test
    @DisplayName("int8 [4]")
    void int8Def() throws Exception {
        assertEquals(INT8, roundtrip(ofInt8()).getChoiceIndex());
    }

    @Test
    @DisplayName("int16 [5]")
    void int16Def() throws Exception {
        assertEquals(INT16, roundtrip(ofInt16()).getChoiceIndex());
    }

    @Test
    @DisplayName("int32 [6]")
    void int32Def() throws Exception {
        assertEquals(INT32, roundtrip(ofInt32()).getChoiceIndex());
    }

    @Test
    @DisplayName("int64 [7]")
    void int64Def() throws Exception {
        assertEquals(INT64, roundtrip(ofInt64()).getChoiceIndex());
    }

    @Test
    @DisplayName("int8u [8]")
    void int8uDef() throws Exception {
        assertEquals(INT8U, roundtrip(ofInt8U()).getChoiceIndex());
    }

    @Test
    @DisplayName("int16u [9]")
    void int16uDef() throws Exception {
        assertEquals(INT16U, roundtrip(ofInt16U()).getChoiceIndex());
    }

    @Test
    @DisplayName("int32u [10]")
    void int32uDef() throws Exception {
        assertEquals(INT32U, roundtrip(ofInt32U()).getChoiceIndex());
    }

    @Test
    @DisplayName("int64u [11]")
    void int64uDef() throws Exception {
        assertEquals(INT64U, roundtrip(ofInt64U()).getChoiceIndex());
    }

    @Test
    @DisplayName("float32 [12]")
    void float32Def() throws Exception {
        assertEquals(FLOAT32, roundtrip(ofFloat32()).getChoiceIndex());
    }

    @Test
    @DisplayName("float64 [13]")
    void float64Def() throws Exception {
        assertEquals(FLOAT64, roundtrip(ofFloat64()).getChoiceIndex());
    }

    @Test
    @DisplayName("utc-time [18]")
    void utcTimeDef() throws Exception {
        assertEquals(UTC_TIME, roundtrip(ofUtcTime()).getChoiceIndex());
    }

    @Test
    @DisplayName("binary-time [19]")
    void binaryTimeDef() throws Exception {
        assertEquals(BINARY_TIME, roundtrip(ofBinaryTime()).getChoiceIndex());
    }

    @Test
    @DisplayName("quality [20]")
    void qualityDef() throws Exception {
        assertEquals(QUALITY, roundtrip(ofQuality()).getChoiceIndex());
    }

    @Test
    @DisplayName("dbpos [21]")
    void dbposDef() throws Exception {
        assertEquals(DBPOS, roundtrip(ofDbpos()).getChoiceIndex());
    }

    @Test
    @DisplayName("tcmd [22]")
    void tcmdDef() throws Exception {
        assertEquals(TCMD, roundtrip(ofTcmd()).getChoiceIndex());
    }

    @Test
    @DisplayName("check [23]")
    void checkDef() throws Exception {
        assertEquals(CHECK, roundtrip(ofCheck()).getChoiceIndex());
    }

    // -------------------------------------------------------------------------
    // INTEGER-encoded string types (length)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("bit-string fixed length [14]")
    void bitStringFixed() throws Exception {
        CmsDataDefinition r = roundtrip(ofBitString(32));
        assertEquals(BIT_STRING, r.getChoiceIndex());
        assertEquals(32, r.getStringLength());
    }

    @Test
    @DisplayName("octet-string variable length (negative) [15]")
    void octetStringVariable() throws Exception {
        CmsDataDefinition r = roundtrip(ofOctetString(-64));
        assertEquals(OCTET_STRING, r.getChoiceIndex());
        assertEquals(-64, r.getStringLength());
    }

    @Test
    @DisplayName("visible-string unbounded (0) [16]")
    void visibleStringUnbounded() throws Exception {
        CmsDataDefinition r = roundtrip(ofVisibleString(0));
        assertEquals(VISIBLE_STRING, r.getChoiceIndex());
        assertEquals(0, r.getStringLength());
    }

    @Test
    @DisplayName("unicode-string positive length [17]")
    void unicodeStringPositive() throws Exception {
        CmsDataDefinition r = roundtrip(ofUnicodeString(20));
        assertEquals(UNICODE_STRING, r.getChoiceIndex());
        assertEquals(20, r.getStringLength());
    }

    // -------------------------------------------------------------------------
    // Array type
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("array fixed count [1]")
    void arrayFixed() throws Exception {
        CmsDataDefinition def = ofArray(10, ofInt32());
        CmsDataDefinition r = roundtrip(def);

        assertEquals(ARRAY, r.getChoiceIndex());
        assertEquals(10, r.getArrayNumberOfElement());
        assertEquals(INT32, r.getArrayElementType().getChoiceIndex());
    }

    @Test
    @DisplayName("array variable count (negative) [1]")
    void arrayVariable() throws Exception {
        CmsDataDefinition def = ofArray(-100, ofFloat32());
        CmsDataDefinition r = roundtrip(def);

        assertEquals(ARRAY, r.getChoiceIndex());
        assertEquals(-100, r.getArrayNumberOfElement());
        assertEquals(FLOAT32, r.getArrayElementType().getChoiceIndex());
    }

    @Test
    @DisplayName("array unbounded (0) [1]")
    void arrayUnbounded() throws Exception {
        CmsDataDefinition def = ofArray(0, ofBoolean());
        CmsDataDefinition r = roundtrip(def);

        assertEquals(ARRAY, r.getChoiceIndex());
        assertEquals(0, r.getArrayNumberOfElement());
        assertEquals(BOOLEAN, r.getArrayElementType().getChoiceIndex());
    }

    @Test
    @DisplayName("array of array (nested) [1]")
    void arrayNested() throws Exception {
        CmsDataDefinition inner = ofArray(5, ofInt16());
        CmsDataDefinition def = ofArray(3, inner);
        CmsDataDefinition r = roundtrip(def);

        assertEquals(ARRAY, r.getChoiceIndex());
        assertEquals(3, r.getArrayNumberOfElement());
        CmsDataDefinition elem = r.getArrayElementType();
        assertEquals(ARRAY, elem.getChoiceIndex());
        assertEquals(5, elem.getArrayNumberOfElement());
        assertEquals(INT16, elem.getArrayElementType().getChoiceIndex());
    }

    // -------------------------------------------------------------------------
    // Structure type
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("structure without fc [2]")
    void structureNoFc() throws Exception {
        CmsDataDefinition def = ofStructure(Arrays.asList(
                new StructureEntry("stVal", ofDbpos()),
                new StructureEntry("q",     ofQuality()),
                new StructureEntry("t",     ofUtcTime())
        ));
        CmsDataDefinition r = roundtrip(def);

        assertEquals(STRUCTURE, r.getChoiceIndex());
        assertEquals(3, r.getStructureEntries().size());

        StructureEntry e0 = r.getStructureEntries().get(0);
        assertEquals("stVal", e0.name.get());
        assertNull(e0.fc);
        assertEquals(DBPOS, e0.type.getChoiceIndex());

        StructureEntry e1 = r.getStructureEntries().get(1);
        assertEquals("q", e1.name.get());
        assertNull(e1.fc);
        assertEquals(QUALITY, e1.type.getChoiceIndex());

        StructureEntry e2 = r.getStructureEntries().get(2);
        assertEquals("t", e2.name.get());
        assertNull(e2.fc);
        assertEquals(UTC_TIME, e2.type.getChoiceIndex());
    }

    @Test
    @DisplayName("structure with fc [2]")
    void structureWithFc() throws Exception {
        CmsDataDefinition def = ofStructure(Arrays.asList(
                new StructureEntry("stVal", "ST", ofDbpos()),
                new StructureEntry("q",     "ST", ofQuality()),
                new StructureEntry("t",     "ST", ofUtcTime())
        ));
        CmsDataDefinition r = roundtrip(def);

        assertEquals(STRUCTURE, r.getChoiceIndex());
        assertEquals(3, r.getStructureEntries().size());

        for (StructureEntry e : r.getStructureEntries()) {
            assertNotNull(e.fc);
            assertEquals("ST", e.fc.get());
        }
        assertEquals("stVal", r.getStructureEntries().get(0).name.get());
        assertEquals("q",     r.getStructureEntries().get(1).name.get());
        assertEquals("t",     r.getStructureEntries().get(2).name.get());
    }

    @Test
    @DisplayName("structure mixed fc presence [2]")
    void structureMixedFc() throws Exception {
        CmsDataDefinition def = ofStructure(Arrays.asList(
                new StructureEntry("mag",  "MX", ofFloat32()),
                new StructureEntry("units",        ofVisibleString(20))
        ));
        CmsDataDefinition r = roundtrip(def);

        assertEquals(STRUCTURE, r.getChoiceIndex());
        StructureEntry e0 = r.getStructureEntries().get(0);
        assertNotNull(e0.fc);
        assertEquals("MX", e0.fc.get());
        assertEquals(FLOAT32, e0.type.getChoiceIndex());

        StructureEntry e1 = r.getStructureEntries().get(1);
        assertNull(e1.fc);
        assertEquals(VISIBLE_STRING, e1.type.getChoiceIndex());
        assertEquals(20, e1.type.getStringLength());
    }

    @Test
    @DisplayName("structure empty [2]")
    void structureEmpty() throws Exception {
        CmsDataDefinition def = ofStructure(java.util.Collections.emptyList());
        CmsDataDefinition r = roundtrip(def);
        assertEquals(STRUCTURE, r.getChoiceIndex());
        assertTrue(r.getStructureEntries().isEmpty());
    }

    // -------------------------------------------------------------------------
    // copy()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("copy basic type is independent")
    void copyBasic() {
        CmsDataDefinition orig = ofInt32();
        CmsDataDefinition copy = orig.copy();
        assertEquals(INT32, copy.getChoiceIndex());
        assertNotSame(orig, copy);
    }

    @Test
    @DisplayName("copy string type preserves length")
    void copyString() {
        CmsDataDefinition orig = ofVisibleString(-64);
        CmsDataDefinition copy = orig.copy();
        assertEquals(VISIBLE_STRING, copy.getChoiceIndex());
        assertEquals(-64, copy.getStringLength());
        assertNotSame(orig, copy);
    }

    @Test
    @DisplayName("copy array is deep")
    void copyArray() {
        CmsDataDefinition orig = ofArray(10, ofInt32());
        CmsDataDefinition copy = orig.copy();
        assertEquals(ARRAY, copy.getChoiceIndex());
        assertEquals(10, copy.getArrayNumberOfElement());
        assertNotSame(orig.getArrayElementType(), copy.getArrayElementType());
    }

    @Test
    @DisplayName("copy structure is deep")
    void copyStructure() {
        CmsDataDefinition orig = ofStructure(Arrays.asList(
                new StructureEntry("val", "MX", ofFloat32())
        ));
        CmsDataDefinition copy = orig.copy();
        assertEquals(STRUCTURE, copy.getChoiceIndex());
        assertNotSame(orig.getStructureEntries(), copy.getStructureEntries());
        assertNotSame(orig.getStructureEntries().get(0).type,
                      copy.getStructureEntries().get(0).type);
    }

    // -------------------------------------------------------------------------
    // toString()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toString unset")
    void toStringUnset() {
        assertEquals("DataDefinition[unset]", new CmsDataDefinition().toString());
    }

    @Test
    @DisplayName("toString basic type")
    void toStringBasic() {
        String s = ofInt32().toString();
        assertTrue(s.contains("6") || s.toLowerCase().contains("int32"),
                "expected index 6 or 'int32' in: " + s);
    }

    @Test
    @DisplayName("toString visible-string")
    void toStringVisibleString() {
        String s = ofVisibleString(-64).toString();
        assertTrue(s.contains("visible-string"), "expected 'visible-string' in: " + s);
        assertTrue(s.contains("-64"), "expected length -64 in: " + s);
    }

    @Test
    @DisplayName("toString array")
    void toStringArray() {
        String s = ofArray(-10, ofInt32()).toString();
        assertTrue(s.contains("array"), "expected 'array' in: " + s);
        assertTrue(s.contains("-10"), "expected -10 in: " + s);
    }

    @Test
    @DisplayName("toString structure")
    void toStringStructure() {
        CmsDataDefinition def = ofStructure(Arrays.asList(
                new StructureEntry("stVal", ofDbpos())
        ));
        String s = def.toString();
        assertTrue(s.contains("structure"), "expected 'structure' in: " + s);
        assertTrue(s.contains("stVal"), "expected 'stVal' in: " + s);
    }

    // -------------------------------------------------------------------------
    // encode without choiceIndex throws exception
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("encode unset throws exception")
    void encodeUnset() {
        assertThrows(IllegalStateException.class, () ->
                new CmsDataDefinition().encode(new PerOutputStream()));
    }
}
