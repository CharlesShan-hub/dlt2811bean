package com.ysh.dlt2811bean.service.svc.data.datatypes;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsGetDataDefinitionEntry extends AbstractCmsCompound<CmsGetDataDefinitionEntry> {

    public CmsVisibleString cdcType = new CmsVisibleString().max(255);
    public CmsDataDefinition definition = new CmsDataDefinition();

    public CmsGetDataDefinitionEntry() {
        super("GetDataDefinitionEntry");
        registerOptionalField("cdcType");
        registerField("definition");
    }

    public CmsGetDataDefinitionEntry cdcType(String cdcType) {
        this.cdcType.set(cdcType);
        return this;
    }

    public CmsGetDataDefinitionEntry definition(CmsDataDefinition def) {
        this.definition = def;
        return this;
    }

    @Override
    public CmsGetDataDefinitionEntry copy() {
        CmsGetDataDefinitionEntry copy = new CmsGetDataDefinitionEntry();
        copy.cdcType = cdcType.copy();
        copy.definition = definition.copy();
        return copy;
    }
}