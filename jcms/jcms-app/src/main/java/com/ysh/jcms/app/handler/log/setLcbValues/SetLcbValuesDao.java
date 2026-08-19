package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.log.CmsSetLcbEntry;
import com.ysh.jcms.core.pdu.log.CmsSetLcbValuesRequest;
import java.util.Collections;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetLcbValuesDao extends BaseDao {

    /** LCB reference (required) */
    private String ref;

    /** Log enable [0..1] */
    private Boolean logEna;

    /** Data set reference [0..1] */
    private String datSet;

    /** Trigger conditions bitmap [0..1] */
    private Integer trgOps;

    /** Integrity period [0..1] */
    private Integer intgPd;

    /** Log reference [0..1] */
    private String logRef;

    /** Log option flags bitmap [0..1] */
    private Integer optFlds;

    /** Buffered time [0..1] */
    private Integer bufTm;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(ref, "ref must not be null");
        return new CmsSetLcbValuesRequest().lcb(Collections.singletonList(
            new CmsSetLcbEntry().reference(ref)
                .logEna(logEna)
                .datSet(datSet)
                .trgOps(trgOps)
                .intgPd(intgPd)
                .logRef(logRef)
                .optFlds(optFlds)
                .bufTm(bufTm)
        ));
    }
}
