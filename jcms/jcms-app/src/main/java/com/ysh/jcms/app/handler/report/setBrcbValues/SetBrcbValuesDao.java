package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.report.CmsSetBrcbEntry;
import com.ysh.jcms.core.pdu.report.CmsSetBrcbValuesRequest;
import java.util.Collections;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetBrcbValuesDao extends BaseDao {

    /** BRCB reference (required), e.g. "LD0/LLN0.brcbWarning" */
    private String ref;

    /** Report ID [0..1] */
    private String rptId;

    /** Report enable [0..1] */
    private Boolean rptEna;

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

    /** Purge buffer [0..1] */
    private Boolean purgeBuf;

    /** Entry ID [0..1] */
    private String entryId;

    /** Reserved time [0..1] */
    private Integer resvTms;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(ref, "ref must not be null");
        return new CmsSetBrcbValuesRequest().brcb(Collections.singletonList(
            new CmsSetBrcbEntry().reference(ref)
                .rptID(rptId)
                .rptEna(rptEna)
                .datSet(datSet)
                .optFlds(optFlds)
                .bufTm(bufTm)
                .trgOps(trgOps)
                .intgPd(intgPd)
                .gi(gi)
                .purgeBuf(purgeBuf)
                .entryID(entryId)
                .resvTms(resvTms)
        ));
    }
}