package com.ysh.jcms.datatype.choice;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.common.CmsObjectName;
import com.ysh.jcms.datatype.fc.CmsFunctionalConstraint;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsDataDefinitionMember extends Structure {
    public CmsObjectName name = new CmsObjectName();
    public CmsFunctionalConstraint fc = new CmsFunctionalConstraint();
    public CmsBoolean has_fc = new CmsBoolean();
    public Pointer type;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("name", "fc", "has_fc", "type");
    }
}