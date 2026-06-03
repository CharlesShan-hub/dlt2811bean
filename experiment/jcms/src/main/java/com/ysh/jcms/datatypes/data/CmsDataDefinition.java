package com.ysh.jcms.datatypes.data;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CmsDataDefinition — tagged union for DataDefinition CHOICE (24 alternatives).
 *
 * <p>Check {@link #choice} before accessing the corresponding value field.
 * Same convention as C: you read {@code def->choice} then the appropriate field.
 *
 * <p>Thread-safety: not guaranteed.
 */
@Getter
@Accessors(fluent = true)
public class CmsDataDefinition extends AbstractCmsDataUnit<CmsDataDefinition> {

    // ==================== Native struct/union mapping ====================

    /** Sub-struct for array (choice 1): {@code { int32_t numberOfElement; DataDefinition *elementType; }}. */
    public static class ArrayStruct extends Structure {
        public int numberOfElement;
        public Pointer elementType;   // struct cms_data_definition*

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("numberOfElement", "elementType");
        }
    }

    /** Sub-struct for structure (choice 2): {@code { Member *elements; int count; }}. */
    public static class MemberArrayStruct extends Structure {
        public Pointer elements;      // struct cms_data_definition_member*
        public int count;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("elements", "count");
        }
    }

    /** The union — only 4 distinct field shapes. */
    public static class DefUnion extends Union {
        public int error;                      // 0
        public ArrayStruct array;              // 1
        public MemberArrayStruct structure;    // 2
        public int string_length;              // 14-17  (and all others are just choice tag)

        public DefUnion() {
            array = new ArrayStruct();
            structure = new MemberArrayStruct();
        }
    }

    /** Top-level native struct: cms_data_definition_t. */
    public static class NativeStruct extends Structure {
        public int choice;
        public DefUnion value;

        public NativeStruct() {
            value = new DefUnion();
        }

        public NativeStruct(Pointer p) {
            super(p);
            value = new DefUnion();
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("choice", "value");
        }
    }

    // ===================== Java-level fields =====================

    // array (1)
    public int numberOfElement;
    public CmsDataDefinition elementType;

    // structure (2)
    public List<CmsDataDefinitionMember> members;

    // string types (14-17)
    public int stringLength;

    // ===================== Construction =====================

    public CmsDataDefinition() {
        super("DataDefinition");
    }

    // ---- factory methods ----

    public static CmsDataDefinition createError(int errorCode) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.choice = ERROR;
        d.serviceError = errorCode;
        return d;
    }

    public static CmsDataDefinition createArray(int numberOfElement, CmsDataDefinition elementType) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.choice = ARRAY;
        d.numberOfElement = numberOfElement;
        d.elementType = elementType;
        return d;
    }

    public static CmsDataDefinition createStructure(List<CmsDataDefinitionMember> members) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.choice = STRUCTURE;
        d.members = new ArrayList<>(members);
        return d;
    }

    /** For choices 3-13, 18-23 that carry no payload (just the choice tag). */
    public static CmsDataDefinition createTagOnly(int choice) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.choice = choice;
        return d;
    }

    /** For string types (14-17) with a length constraint. */
    public static CmsDataDefinition createStringType(int choice, int stringLength) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.choice = choice;
        d.stringLength = stringLength;
        return d;
    }

    // ===================== Encode =====================

    public byte[] encode() {
        NativeStruct ns = new NativeStruct();
        List<Pointer> allocs = new ArrayList<>();

        try {
            syncToNative(ns, allocs);
            ns.write();

            byte[] buf = new byte[65536];
            IntByReference outLen = new IntByReference(buf.length);
            CmsFFIDatatypes.INSTANCE.cms_data_definition_encode(ns, buf, outLen);
            return Arrays.copyOf(buf, outLen.getValue());
        } finally {
            // Memory objects will be GC'd
        }
    }

    private void syncToNative(NativeStruct ns, List<Pointer> allocs) {
        ns.choice = choice;

        switch (choice) {
            case ERROR:
                ns.value.setType("error");
                ns.value.error = serviceError;
                break;

            case ARRAY:
                ns.value.setType("array");
                ns.value.array.numberOfElement = numberOfElement;
                if (elementType != null) {
                    NativeStruct childNs = new NativeStruct();
                    elementType.syncToNative(childNs, allocs);
                    childNs.write();
                    long size = childNs.size();
                    Memory mem = new Memory(size);
                    mem.write(0, childNs.getPointer().getByteArray(0, (int) size), 0, (int) size);
                    allocs.add(mem);
                    ns.value.array.elementType = mem;
                } else {
                    ns.value.array.elementType = null;
                }
                break;

            case STRUCTURE:
                ns.value.setType("structure");
                syncMemberArray(ns.value.structure, allocs);
                break;

            // choices 3-13, 18-23: no payload
            case BOOLEAN: case INT8: case INT16: case INT32: case INT64:
            case INT8U: case INT16U: case INT32U: case INT64U:
            case FLOAT32: case FLOAT64:
            case UTC_TIME: case BINARY_TIME: case QUALITY:
            case DBPOS: case TCMD: case CHECK:
                break;

            // string types 14-17: encode string_length
            case BIT_STRING: case OCTET_STRING:
            case VISIBLE_STRING: case UTF8_STRING:
                ns.value.setType("string_length");
                ns.value.string_length = stringLength;
                break;

            default:
                throw new IllegalArgumentException("Unknown choice: " + choice);
        }
    }

    private void syncMemberArray(MemberArrayStruct arr, List<Pointer> allocs) {
        int count = members != null ? members.size() : 0;
        arr.count = count;
        if (count == 0) {
            arr.elements = null;
            return;
        }

        long elemSize = new CmsDataDefinitionMember.MemberStruct().size();
        Memory block = new Memory(elemSize * count);
        allocs.add(block);

        for (int i = 0; i < count; i++) {
            Pointer elemPtr = block.share(i * elemSize);
            CmsDataDefinitionMember.MemberStruct ms = new CmsDataDefinitionMember.MemberStruct(elemPtr);
            CmsDataDefinitionMember member = members.get(i);

            byte[] nameBytes = new byte[65];
            if (member.name != null) {
                byte[] src = member.name.getBytes();
                System.arraycopy(src, 0, nameBytes, 0, Math.min(src.length, 64));
            }
            ms.name = nameBytes;

            byte[] fcBytes = new byte[3];
            if (member.hasFc && member.fc != null) {
                byte[] src = member.fc.getBytes();
                System.arraycopy(src, 0, fcBytes, 0, Math.min(src.length, 2));
            }
            ms.fc = fcBytes;
            ms.has_fc = member.hasFc ? 1 : 0;

            if (member.type != null) {
                NativeStruct childNs = new NativeStruct();
                member.type.syncToNative(childNs, allocs);
                childNs.write();
                long childSize = childNs.size();
                Memory childMem = new Memory(childSize);
                childMem.write(0, childNs.getPointer().getByteArray(0, (int) childSize), 0, (int) childSize);
                allocs.add(childMem);
                ms.type = childMem;
            } else {
                ms.type = null;
            }

            ms.write();
        }
        arr.elements = block;
    }

    // ===================== Decode =====================

    public static CmsDataDefinition decode(byte[] data) {
        NativeStruct ns = new NativeStruct();
        CmsFFIDatatypes.INSTANCE.cms_data_definition_decode(data, data.length, ns);
        ns.read();

        CmsDataDefinition result;
        try {
            result = syncFromNative(ns);
        } finally {
            CmsFFIDatatypes.INSTANCE.cms_data_definition_free(ns);
        }
        return result;
    }

    private static String unionFieldName(int choice) {
        switch (choice) {
            case ERROR:          return "error";
            case ARRAY:          return "array";
            case STRUCTURE:      return "structure";
            case BIT_STRING: case OCTET_STRING:
            case VISIBLE_STRING: case UTF8_STRING:
                return "string_length";
            default:
                return "string_length"; // any field works for empty choices
        }
    }

    private static CmsDataDefinition syncFromNative(NativeStruct ns) {
        int ch = ns.choice;

        ns.value.setType(unionFieldName(ch));
        ns.value.read();

        switch (ch) {
            case ARRAY:
                ns.value.array.read();
                break;
            case STRUCTURE:
                ns.value.structure.read();
                break;
        }

        switch (ch) {
            case ERROR:
                return createError(ns.value.error);

            case ARRAY: {
                int numElem = ns.value.array.numberOfElement;
                CmsDataDefinition child = null;
                if (ns.value.array.elementType != null) {
                    NativeStruct childNs = new NativeStruct(ns.value.array.elementType);
                    childNs.read();
                    child = syncFromNative(childNs);
                }
                return createArray(numElem, child);
            }

            case STRUCTURE: {
                int count = ns.value.structure.count;
                List<CmsDataDefinitionMember> memberList = new ArrayList<>(count);
                if (count > 0 && ns.value.structure.elements != null) {
                    long elemSize = new CmsDataDefinitionMember.MemberStruct().size();
                    for (int i = 0; i < count; i++) {
                        Pointer elemPtr = ns.value.structure.elements.share(i * elemSize);
                        CmsDataDefinitionMember.MemberStruct ms = new CmsDataDefinitionMember.MemberStruct(elemPtr);
                        ms.read();

                        CmsDataDefinitionMember m = new CmsDataDefinitionMember();
                        m.name = new String(ms.name).trim();
                        m.hasFc = ms.has_fc != 0;
                        if (m.hasFc) {
                            m.fc = new String(ms.fc).trim();
                        }
                        if (ms.type != null) {
                            NativeStruct childNs = new NativeStruct(ms.type);
                            childNs.read();
                            m.type = syncFromNative(childNs);
                        }
                        memberList.add(m);
                    }
                }
                return createStructure(memberList);
            }

            case BIT_STRING: case OCTET_STRING:
            case VISIBLE_STRING: case UTF8_STRING:
                return createStringType(ch, ns.value.string_length);

            // choices 3-13, 18-23: no payload
            default:
                return createTagOnly(ch);
        }
    }

    // ===================== Copy =====================

    public CmsDataDefinition copy() {
        CmsDataDefinition c = new CmsDataDefinition();
        c.choice = choice;
        c.serviceError = serviceError;
        c.numberOfElement = numberOfElement;
        c.elementType = elementType != null ? elementType.copy() : null;
        c.stringLength = stringLength;

        if (members != null) {
            c.members = new ArrayList<>(members.size());
            for (CmsDataDefinitionMember m : members) {
                c.members.add(m.copy());
            }
        }
        return c;
    }

    // ===================== Helpers =====================

    @Override
    public boolean isTagOnly() {
        switch (choice) {
            case BOOLEAN: case INT8: case INT16: case INT32: case INT64:
            case INT8U: case INT16U: case INT32U: case INT64U:
            case FLOAT32: case FLOAT64:
            case UTC_TIME: case BINARY_TIME: case QUALITY:
            case DBPOS: case TCMD: case CHECK:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        switch (choice) {
            case ERROR: return "DataDefinition(error=" + serviceError + ")";
            case ARRAY: return "DataDefinition(array[" + numberOfElement + "]=" + elementType + ")";
            case STRUCTURE: return "DataDefinition(structure=" + members + ")";
            case BIT_STRING: return "DataDefinition(bit-string[" + stringLength + "])";
            case OCTET_STRING: return "DataDefinition(octet-string[" + stringLength + "])";
            case VISIBLE_STRING: return "DataDefinition(visible-string[" + stringLength + "])";
            case UTF8_STRING: return "DataDefinition(unicode-string[" + stringLength + "])";
            default: {
                String[] names = {"boolean", "int8", "int16", "int32", "int64",
                    "int8u", "int16u", "int32u", "int64u",
                    "float32", "float64",
                    "utc-time", "binary-time", "quality",
                    "dbpos", "tcmd", "check"};
                int idx = choice - BOOLEAN;
                if (idx >= 0 && idx < names.length) {
                    return "DataDefinition(" + names[idx] + ")";
                }
                return "DataDefinition(choice=" + choice + ")";
            }
        }
    }
}
