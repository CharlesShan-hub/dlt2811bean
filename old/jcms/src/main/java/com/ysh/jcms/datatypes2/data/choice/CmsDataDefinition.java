package com.ysh.jcms.datatypes2.data.choice;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

import java.util.Arrays;
import java.util.List;

/**
 * cms_data_definition_t — tagged union for DataDefinition CHOICE.
 *
 * C: typedef struct cms_data_definition {
 *     int      choice;    // 0..23
 *     union { ... } value;
 * } cms_data_definition_t;
 */
public class CmsDataDefinition extends Structure {

    public static class MemberStruct extends Structure {
        public byte[] name = new byte[65];
        public byte[] fc = new byte[3];
        public int hasFc;
        public Pointer type;          /* struct cms_data_definition* */

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("name", "fc", "hasFc", "type");
        }
    }

    public static class ArrayStruct extends Structure {
        public int numberOfElement;
        public Pointer elementType;    /* struct cms_data_definition* */

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("numberOfElement", "elementType");
        }
    }

    public static class MemberArrayStruct extends Structure {
        public Pointer elements;       /* struct cms_data_definition_member* */
        public int count;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("elements", "count");
        }
    }

    public static class DefUnion extends Union {
        public int error;
        public ArrayStruct array;
        public MemberArrayStruct structure;
        public int stringLength;

        public DefUnion() {
            array = new ArrayStruct();
            structure = new MemberArrayStruct();
        }
    }

    public int choice;
    public DefUnion value = new DefUnion();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("choice", "value");
    }
}
