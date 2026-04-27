package com.ysh.dlt2811bean.datatypes.data;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsDataUnit;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerBoolean;
import com.ysh.dlt2811bean.per.types.PerChoice;
import com.ysh.dlt2811bean.per.types.PerInteger;
import com.ysh.dlt2811bean.per.types.PerNull;

/**
 * DL/T 2811 DataDefinition type (§7.7.2) — describes the type structure of a data object.
 *
 * <pre>
 * DataDefinition ::= CHOICE {
 *     error          [0]  IMPLICIT ServiceError,
 *     array          [1]  IMPLICIT SEQUENCE {
 *         numberOfElement  [1] IMPLICIT INT32,
 *         elementType      [2] DataDefinition
 *     },
 *     structure      [2]  IMPLICIT SEQUENCE OF SEQUENCE {
 *         name             [0] IMPLICIT ObjectName,
 *         fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *         type             [2] DataDefinition
 *     },
 *     boolean        [3]  IMPLICIT NULL,
 *     int8           [4]  IMPLICIT NULL,
 *     int16          [5]  IMPLICIT NULL,
 *     int32          [6]  IMPLICIT NULL,
 *     int64          [7]  IMPLICIT NULL,
 *     int8u          [8]  IMPLICIT NULL,
 *     int16u         [9]  IMPLICIT NULL,
 *     int32u         [10] IMPLICIT NULL,
 *     int64u         [11] IMPLICIT NULL,
 *     float32        [12] IMPLICIT NULL,
 *     float64        [13] IMPLICIT NULL,
 *     bit-string     [14] IMPLICIT INTEGER,
 *     octet-string   [15] IMPLICIT INTEGER,
 *     visible-string [16] IMPLICIT INTEGER,
 *     unicode-string [17] IMPLICIT INTEGER,
 *     utc-time       [18] IMPLICIT NULL,
 *     binary-time    [19] IMPLICIT NULL,
 *     quality        [20] IMPLICIT NULL,
 *     dbpos          [21] IMPLICIT NULL,
 *     tcmd           [22] IMPLICIT NULL,
 *     check          [23] IMPLICIT NULL
 * }
 * </pre>
 *
 * <p>Unlike {@link CmsData} which carries actual values, DataDefinition describes the
 * <em>type</em> of a data object. Basic types encode as NULL (index only). String types
 * encode as INTEGER (length). Array and structure types are recursive.
 *
 * <p>For string types, the length follows the convention of §7.7.2:
 * positive = fixed count, negative = variable (max = abs), 0 = unbounded.
 *
 * <pre>
 * // Construct a boolean definition
 * CmsDataDefinition def = CmsDataDefinition.ofBoolean();
 *
 * // Construct a visible-string definition with max length 64
 * CmsDataDefinition def = CmsDataDefinition.ofVisibleString(-64);
 *
 * // Construct an array definition
 * CmsDataDefinition def = CmsDataDefinition.ofArray(-10, CmsDataDefinition.ofInt32());
 *
 * // Encode / Decode
 * def.encode(pos);
 * CmsDataDefinition r = new CmsDataDefinition().decode(pis);
 * </pre>
 */
public class CmsDataDefinition extends AbstractCmsDataUnit<CmsDataDefinition, Integer> {

    /** For string types: length (positive=fixed, negative=variable max, 0=unbounded). */
    private int stringLength = 0;

    /** For array type. */
    private int arrayNumberOfElement = 0;
    private CmsDataDefinition arrayElementType = null;

    /** For structure type: list of entries, each with name, optional fc, and type. */
    private java.util.List<StructureEntry> structureEntries = null;

    public CmsDataDefinition() {
        super("DataDefinition", -1);
    }

    // -------------------------------------------------------------------------
    // Structure entry
    // -------------------------------------------------------------------------

    public static class StructureEntry {
        public final CmsObjectName name;
        public final CmsFC fc;        // null if absent
        public final CmsDataDefinition type;

        public StructureEntry(CmsObjectName name, CmsFC fc, CmsDataDefinition type) {
            this.name = name;
            this.fc = fc;
            this.type = type;
        }

        public StructureEntry(String name, CmsDataDefinition type) {
            this(new CmsObjectName(name), null, type);
        }

        public StructureEntry(String name, String fc, CmsDataDefinition type) {
            this(new CmsObjectName(name), new CmsFC(fc), type);
        }
    }

    // -------------------------------------------------------------------------
    // Static factory methods
    // -------------------------------------------------------------------------

    public static CmsDataDefinition ofBoolean()      { return ofNull(BOOLEAN); }
    public static CmsDataDefinition ofInt8()         { return ofNull(INT8); }
    public static CmsDataDefinition ofInt16()        { return ofNull(INT16); }
    public static CmsDataDefinition ofInt32()        { return ofNull(INT32); }
    public static CmsDataDefinition ofInt64()        { return ofNull(INT64); }
    public static CmsDataDefinition ofInt8U()        { return ofNull(INT8U); }
    public static CmsDataDefinition ofInt16U()       { return ofNull(INT16U); }
    public static CmsDataDefinition ofInt32U()       { return ofNull(INT32U); }
    public static CmsDataDefinition ofInt64U()       { return ofNull(INT64U); }
    public static CmsDataDefinition ofFloat32()      { return ofNull(FLOAT32); }
    public static CmsDataDefinition ofFloat64()      { return ofNull(FLOAT64); }
    public static CmsDataDefinition ofUtcTime()      { return ofNull(UTC_TIME); }
    public static CmsDataDefinition ofBinaryTime()   { return ofNull(BINARY_TIME); }
    public static CmsDataDefinition ofQuality()      { return ofNull(QUALITY); }
    public static CmsDataDefinition ofDbpos()        { return ofNull(DBPOS); }
    public static CmsDataDefinition ofTcmd()         { return ofNull(TCMD); }
    public static CmsDataDefinition ofCheck()        { return ofNull(CHECK); }

    public static CmsDataDefinition ofBitString(int length)     { return ofString(BIT_STRING, length); }
    public static CmsDataDefinition ofOctetString(int length)   { return ofString(OCTET_STRING, length); }
    public static CmsDataDefinition ofVisibleString(int length) { return ofString(VISIBLE_STRING, length); }
    public static CmsDataDefinition ofUnicodeString(int length) { return ofString(UNICODE_STRING, length); }

    public static CmsDataDefinition ofArray(int numberOfElement, CmsDataDefinition elementType) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.set(ARRAY);
        d.arrayNumberOfElement = numberOfElement;
        d.arrayElementType = elementType;
        return d;
    }

    public static CmsDataDefinition ofStructure(java.util.List<StructureEntry> entries) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.set(STRUCTURE);
        d.structureEntries = new java.util.ArrayList<>(entries);
        return d;
    }

    private static CmsDataDefinition ofNull(int index) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.set(index);
        return d;
    }

    private static CmsDataDefinition ofString(int index, int length) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.set(index);
        d.stringLength = length;
        return d;
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public int getChoiceIndex() { return get(); }
    public int getStringLength() { return stringLength; }
    public int getArrayNumberOfElement() { return arrayNumberOfElement; }
    public CmsDataDefinition getArrayElementType() { return arrayElementType; }
    public java.util.List<StructureEntry> getStructureEntries() { return structureEntries; }

    // -------------------------------------------------------------------------
    // Encode / Decode
    // -------------------------------------------------------------------------

    @Override
    public void encode(PerOutputStream pos) {
        int idx = get();
        if (idx < 0) {
            throw new IllegalStateException("DataDefinition: choiceIndex not set");
        }
        PerChoice.encode(pos, idx);

        switch (idx) {
            case ARRAY:
                CmsInt32.write(pos, arrayNumberOfElement);
                arrayElementType.encode(pos);
                break;
            case STRUCTURE:
                int count = structureEntries.size();
                PerInteger.encode(pos, count, 0, Integer.MAX_VALUE);
                for (StructureEntry entry : structureEntries) {
                    entry.name.encode(pos);
                    boolean hasFc = entry.fc != null;
                    PerBoolean.encode(pos, hasFc);
                    if (hasFc) {
                        entry.fc.encode(pos);
                    }
                    entry.type.encode(pos);
                }
                break;
            case BIT_STRING:
            case OCTET_STRING:
            case VISIBLE_STRING:
            case UNICODE_STRING:
                PerInteger.encode(pos, stringLength, Integer.MIN_VALUE, Integer.MAX_VALUE);
                break;
            default:
                PerNull.encode(pos);
                break;
        }
    }

    @Override
    public CmsDataDefinition decode(PerInputStream pis) throws Exception {
        int idx = PerChoice.decode(pis);
        set(idx);

        switch (idx) {
            case ARRAY:
                arrayNumberOfElement = (int) PerInteger.decode(pis, Integer.MIN_VALUE, Integer.MAX_VALUE);
                arrayElementType = new CmsDataDefinition().decode(pis);
                break;
            case STRUCTURE:
                int count = (int) PerInteger.decode(pis, 0, Integer.MAX_VALUE);
                structureEntries = new java.util.ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    CmsObjectName name = new CmsObjectName().decode(pis);
                    boolean hasFc = PerBoolean.decode(pis);
                    CmsFC fc = hasFc ? new CmsFC().decode(pis) : null;
                    CmsDataDefinition type = new CmsDataDefinition().decode(pis);
                    structureEntries.add(new StructureEntry(name, fc, type));
                }
                break;
            case BIT_STRING:
            case OCTET_STRING:
            case VISIBLE_STRING:
            case UNICODE_STRING:
                stringLength = (int) PerInteger.decode(pis, Integer.MIN_VALUE, Integer.MAX_VALUE);
                break;
            default:
                PerNull.decode(pis);
                break;
        }
        return this;
    }

    @Override
    public CmsDataDefinition copy() {
        int idx = get();
        switch (idx) {
            case ARRAY:
                return ofArray(arrayNumberOfElement, arrayElementType.copy());
            case STRUCTURE: {
                java.util.List<StructureEntry> copied = new java.util.ArrayList<>();
                for (StructureEntry e : structureEntries) {
                    CmsObjectName nameCopy = e.name.copy();
                    CmsFC fcCopy = e.fc != null ? e.fc.copy() : null;
                    copied.add(new StructureEntry(nameCopy, fcCopy, e.type.copy()));
                }
                return ofStructure(copied);
            }
            case BIT_STRING:
            case OCTET_STRING:
            case VISIBLE_STRING:
            case UNICODE_STRING:
                return ofString(idx, stringLength);
            default:
                return ofNull(idx);
        }
    }

    @Override
    public String toString() {
        int idx = get();
        switch (idx) {
            case -1:           return "DataDefinition[unset]";
            case ARRAY:        return "DataDefinition[array, n=" + arrayNumberOfElement + ", elem=" + arrayElementType + "]";
            case STRUCTURE: {
                StringBuilder sb = new StringBuilder("DataDefinition[structure, entries=[");
                for (int i = 0; i < structureEntries.size(); i++) {
                    if (i > 0) sb.append(", ");
                    StructureEntry e = structureEntries.get(i);
                    sb.append(e.name.get());
                    if (e.fc != null) sb.append("(").append(e.fc.get()).append(")");
                    sb.append(":").append(e.type);
                }
                sb.append("]]");
                return sb.toString();
            }
            case BIT_STRING:     return "DataDefinition[bit-string, len=" + stringLength + "]";
            case OCTET_STRING:   return "DataDefinition[octet-string, len=" + stringLength + "]";
            case VISIBLE_STRING: return "DataDefinition[visible-string, len=" + stringLength + "]";
            case UNICODE_STRING: return "DataDefinition[unicode-string, len=" + stringLength + "]";
            default:             return "DataDefinition[" + idx + "]";
        }
    }
}
