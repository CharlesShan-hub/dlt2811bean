package com.ysh.jcms.datatypes2.data.choice;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_data_definition_t — tagged union for DataDefinition CHOICE.
 */
public class CmsDataDefinition extends CmsStructure {

    public static class MemberStruct extends Structure {
        public byte[] name = new byte[65];
        public byte[] fc = new byte[3];
        public int hasFc;
        public Pointer type;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("name", "fc", "hasFc", "type");
        }
    }

    public static class ArrayStruct extends Structure {
        public int numberOfElement;
        public Pointer elementType;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("numberOfElement", "elementType");
        }
    }

    public static class MemberArrayStruct extends Structure {
        public Pointer elements;
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

    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_data_definition_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_data_definition_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsDataDefinition from(byte[] data) { return new CmsDataDefinition().decode(data); }
}
