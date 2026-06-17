package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectName;
import com.ysh.jcms.data.fc.CmsFC;
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
 *   [16] fc         → CmsFC*
 *   [24] type       → CmsDataDefinition*
 */
public class CmsDataDefinitionStructElem extends CmsType {

    public CmsObjectName           name;
    public CmsBoolean              fc_present;
    public CmsFC fc;
    public CmsDataDefinition       type;

    public CmsDataDefinitionStructElem() {
        this.name       = new CmsObjectName();
        this.fc_present = new CmsBoolean();
        this.fc         = new CmsFC();
        this.type       = new CmsDataDefinition();
    }

    public CmsDataDefinitionStructElem name(byte[] v) { this.name.value(v); return this; }
    public CmsDataDefinitionStructElem name(String v) { this.name.value(v); return this; }
    public CmsDataDefinitionStructElem fc_present(boolean v) { this.fc_present.value(v); return this; }
    public CmsDataDefinitionStructElem fc(int v) { this.fc_present.value(true); this.fc.value(v); return this; }
    public CmsDataDefinitionStructElem type(CmsDataDefinition v) { this.type = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(name, fc_present, fc, type);
    }
}
