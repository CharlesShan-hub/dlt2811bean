package com.ysh.jcms.datatype.choice;

import com.sun.jna.Union;
import com.ysh.jcms.datatype.basic.CmsInt32;
import com.ysh.jcms.datatype.common.CmsServiceError;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsDataDefinitionUnion extends Union {
    public CmsServiceError.ByValue error = new CmsServiceError.ByValue();                 //  0
    public CmsDataDefinitionArray.ByValue array = new CmsDataDefinitionArray.ByValue();       //  1
    public CmsDataDefinitionStructure.ByValue structure = new CmsDataDefinitionStructure.ByValue(); //  2
    public CmsInt32.ByValue string_length = new CmsInt32.ByValue();                                // 14-17

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("error", "array", "structure", "string_length");
    }

    /** 根据 choice 值返回当前活跃的字段。 */
    public Object get(int c) {
        switch (c) {
            case 0:  return error;
            case 1:  return array;
            case 2:  return structure;
            default: return string_length;
        }
    }
}
