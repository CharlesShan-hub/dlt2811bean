package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.directory.CmsGetAllCbValuesRequest;
import lombok.Getter;
import com.ysh.jcms.app.handler.BaseDao;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class AllCbValuesDao extends BaseDao {

    /** ldName (e.g. "LD0") — alternative to lnReference */
    private String ldName;
    /** lnReference (e.g. "LD0/LLN0") — alternative to ldName */
    private String lnReference;
    /** ACSI class (e.g. 3=BRCB, 4=URCB, 5=LCB, 7=SGCB, 8=GOCB, 10=MSVCB) */
    private int acsiClass;
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

    @Override
    public CmsType toRequest() {
        CmsGetAllCbValuesRequest req = new CmsGetAllCbValuesRequest().acsiClass(acsiClass);
        if (referenceAfter != null && !referenceAfter.isEmpty())
            req.referenceAfter(referenceAfter);
        if (ldName != null) {
            req.reference.altLdName(ldName);
        } else if (lnReference != null) {
            req.reference.altLnReference(lnReference);
        }
        return req;
    }
}
