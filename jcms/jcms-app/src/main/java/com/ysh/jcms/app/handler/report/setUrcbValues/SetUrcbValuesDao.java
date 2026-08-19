package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.report.CmsSetUrcbEntry;
import com.ysh.jcms.core.pdu.report.CmsSetUrcbValuesRequest;
import java.util.Collections;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetUrcbValuesDao extends BaseDao {

    /** URCB reference (required), e.g. "LD0/LLN0.urcbAin" */
    private String ref;

    /** Report ID [0..1] */
    private String rptId;

    /** Report enable [0..1] */
    private Boolean rptEna;

    /** Reserve [0..1] */
    private Boolean resv;

    /** Data set reference [0..1] */
    private String datSet;

    /** Report option flags bitmap [0..1] */
    private Integer optFlds;

    /** Buffered time [0..1] */
    private Integer bufTm;

    /** Trigger conditions bitmap [0..1] */
    private Integer trgOps;

    /** Integrity period [0..1] */
    private Integer intgPd;

    /** General interrogation [0..1] */
    private Boolean gi;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(ref, "ref must not be null");
        return new CmsSetUrcbValuesRequest().urcb(Collections.singletonList(
            new CmsSetUrcbEntry().reference(ref)
                .rptID(rptId)
                .rptEna(rptEna)
                .resv(resv)
                .datSet(datSet)
                .optFlds(optFlds)
                .bufTm(bufTm)
                .trgOps(trgOps)
                .intgPd(intgPd)
                .gi(gi)
        ));
    }
}
}
