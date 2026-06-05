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
    public CmsServiceError error = new CmsServiceError();           //  0
    public CmsDataDefinitionArray array = new CmsDataDefinitionArray();       //  1
    public CmsDataDefinitionStructure structure = new CmsDataDefinitionStructure(); //  2
    public CmsInt32 string_length = new CmsInt32();                  // 14-17
}
