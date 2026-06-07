package com.ysh.jcms.datatype.choice;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsDataDefinitionArray extends Structure {
    public int numberOfElement;
    public Pointer elementType;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("numberOfElement", "elementType");
    }

    public static class ByValue extends CmsDataDefinitionArray implements Structure.ByValue {}
}
