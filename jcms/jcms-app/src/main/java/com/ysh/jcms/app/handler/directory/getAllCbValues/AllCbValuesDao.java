package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.directory.CmsGetAllCbValuesRequest;
import com.ysh.jcms.app.handler.base.BaseDao;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class AllCbValuesDao extends BaseDao {

    /** LD name (e.g. "LD0") or LN reference (e.g. "LD0/LLN0") — auto-detected by PDU */
    private String reference;

    /** ACSI class (e.g. 3=BRCB, 4=URCB, 5=LCB, 7=SGCB, 8=GOCB, 10=MSVCB) */
    private Integer acsiClass;

    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(reference, "reference must not be null");
        if (acsiClass == null || acsiClass == 0) {
            throw new IllegalArgumentException("acsiClass must be set (0 is reserved)");
        }
        return new CmsGetAllCbValuesRequest()
            .reference(reference)
            .acsiClass(acsiClass)
            .referenceAfter(referenceAfter);
    }
}
