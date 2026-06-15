package com.ysh.dlt2811bean.service.svc.directory.datatypes;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsDataDefinitionEntry extends AbstractCmsCompound<CmsDataDefinitionEntry> {

    public CmsSubReference reference = new CmsSubReference();
    public CmsVisibleString cdcType = new CmsVisibleString().max(255);
    public CmsDataDefinition definition = new CmsDataDefinition();

    public CmsDataDefinitionEntry() {
        super("DataDefinitionEntry");
        registerField("reference");
        registerOptionalField("cdcType");
        registerField("definition");
    }

    public CmsDataDefinitionEntry reference(String ref) {
        this.reference = new CmsSubReference(ref);
        return this;
    }

    public CmsDataDefinitionEntry cdcType(String cdcType) {
        this.cdcType.set(cdcType);
        return this;
    }

    public CmsDataDefinitionEntry definition(CmsDataDefinition def) {
        this.definition = def;
        return this;
    }

    @Override
    public CmsDataDefinitionEntry copy() {
        CmsDataDefinitionEntry copy = new CmsDataDefinitionEntry();
        copy.reference = reference.copy();
        copy.cdcType = cdcType.copy();
        copy.definition = definition.copy();
        return copy;
    }
}