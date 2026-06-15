package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectName;
import com.ysh.jcms.data.fc.CmsFunctionalConstraint;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * DataDefinitionStructElem ::= SEQUENCE {
 *     name   ObjectName,
 *     fc     FunctionalConstraint OPTIONAL,
 *     type   DataDefinition
 * }  —  7.7
 *
 * All-pointer container:
 *   [0]  name       → CmsObjectName*
 *   [8]  fc_present → CmsBoolean*
 *   [16] fc         → CmsFunctionalConstraint*
 *   [24] type       → CmsDataDefinition*
 */
public class CmsDataDefinitionStructElem extends CmsType {

    public CmsObjectName           name;
    public CmsBoolean              fc_present;
    public CmsFunctionalConstraint fc;
    public CmsDataDefinition       type;

    public CmsDataDefinitionStructElem() {
        this.name       = new CmsObjectName();
        this.fc_present = new CmsBoolean();
        this.fc         = new CmsFunctionalConstraint();
        this.type       = new CmsDataDefinition();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(name, fc_present, fc, type);
    }
}
