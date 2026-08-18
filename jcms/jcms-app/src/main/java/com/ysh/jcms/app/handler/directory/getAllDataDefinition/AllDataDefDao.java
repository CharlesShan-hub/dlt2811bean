package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.directory.CmsGetAllDataDefinitionRequest;
import com.ysh.jcms.app.handler.base.BaseDao;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class AllDataDefDao extends BaseDao {

    /** LD name (e.g. "LD0") or LN reference (e.g. "LD0/LLN0") — auto-detected by PDU */
    private String reference;

    /** Optional FunctionalConstraint filter (2-char code, e.g. "ST", "MX") */
    private String fc;

    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(reference, "reference must not be null");
        return new CmsGetAllDataDefinitionRequest()
            .reference(reference)
            .fc(fc)
            .referenceAfter(referenceAfter);
    }
}
