package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.util.CmsRequestHelper;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.directory.CmsGetAllDataValuesRequest;
import lombok.Getter;
import com.ysh.jcms.app.handler.BaseDao;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class AllDataValuesDao extends BaseDao {

    /** ldName (e.g. "LD0") — alternative to lnReference */
    private String ldName;
    /** lnReference (e.g. "LD0/LLN0") — alternative to ldName */
    private String lnReference;
    /** Optional FunctionalConstraint filter */
    private Integer fc;
    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    /**
     * Convenience setter that detects {@code lnReference} (contains "/") vs
     * {@code ldName} (no "/").
     */
    public void ln(String value) {
        if (value.contains("/")) {
            this.lnReference = value;
        } else {
            this.ldName = value;
        }
    }

    /**
     * Convenience setter for FC from a 2-char code string (e.g. "ST", "MX").
     */
    public void fc(String value) {
        this.fc = com.ysh.jcms.core.data.scalar.CmsFC.fromString(value);
    }

    @Override
    public CmsType toRequest() {
        CmsGetAllDataValuesRequest req = new CmsGetAllDataValuesRequest();
        CmsRequestHelper.setIfNotEmpty(req::referenceAfter, referenceAfter);
        if (ldName != null) {
            req.reference.altLdName(ldName);
        } else if (lnReference != null) {
            req.reference.altLnReference(lnReference);
        }
        if (fc != null) {
            req.fc(fc);
        }
        return req;
    }
}
