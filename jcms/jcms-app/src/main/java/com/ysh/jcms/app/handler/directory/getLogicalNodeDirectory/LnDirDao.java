package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalNodeDirectoryRequest;
import com.ysh.jcms.app.handler.base.BaseDao;
import java.util.Objects;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class LnDirDao extends BaseDao {

    /** LD name (e.g. "C1") or LN reference (e.g. "C1/LLN0") — auto-detected by PDU */
    private String reference;

    /** ACSI class to query (1=DATA-OBJECT, 2=DATA-SET, 3=BRCB, 4=URCB, 5=LCB, 6=LOG, 7=SGCB, 8=GoCB, 10=MSVCB) */
    private Integer acsiClass;

    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(reference, "reference must not be null");
        Objects.requireNonNull(acsiClass, "acsiClass must not be null");
        return new CmsGetLogicalNodeDirectoryRequest()
            .reference(reference)
            .acsiClass(acsiClass)
            .referenceAfter(referenceAfter);
    }
}
