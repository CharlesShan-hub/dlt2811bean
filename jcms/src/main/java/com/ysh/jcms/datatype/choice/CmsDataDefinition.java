package com.ysh.jcms.datatype.choice;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsInt32;
import com.ysh.jcms.datatype.common.CmsServiceError;
import com.ysh.jcms.ffi.CmsField;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsDataDefinition extends CmsField {
    public CmsDataType.ByValue choice = new CmsDataType.ByValue();
    public CmsDataDefinitionUnion value = new CmsDataDefinitionUnion();

    public static CmsDataDefinition ofError(int errorCode) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.choice().value(CmsDataType.ERROR);
        d.value.setType(CmsServiceError.ByValue.class);
        d.value.error.value(errorCode);
        return d;
    }

    public static CmsDataDefinition of(int c, int stringLength) {
        CmsDataDefinition d = new CmsDataDefinition();
        d.choice().value(c);
        d.value.setType(CmsInt32.ByValue.class);
        d.value.string_length.value(stringLength);
        return d;
    }

    public static CmsDataDefinition of(int c) {
        return of(c, 0);
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("choice", "value");
    }

    @Override
    protected int encodeBufSize() { return 4096; }

    @Override
    public CmsDataDefinition test() {
        super.test();
        Object active = value.get(choice().value());
        if (active instanceof CmsField) ((CmsField) active).test();
        return this;
    }

    @Override
    public byte[] encode() {
        value.setType(unionClass(choice().value()));
        return super.encode();
    }

    @Override
    public CmsDataDefinition decode(byte[] data) {
        super.decode(data);
        value.setType(unionClass(choice().value()));
        value.read();
        return this;
    }

    private static Class<?> unionClass(int c) {
        switch (c) {
            case 0:  return CmsServiceError.ByValue.class;
            case 1:  return CmsDataDefinitionArray.ByValue.class;
            case 2:  return CmsDataDefinitionStructure.ByValue.class;
            default: return CmsInt32.ByValue.class;
        }
    }
}
