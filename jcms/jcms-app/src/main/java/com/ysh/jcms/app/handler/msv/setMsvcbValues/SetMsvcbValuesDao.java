package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.msv.CmsSetMsvcbEntry;
import com.ysh.jcms.core.pdu.msv.CmsSetMsvcbValuesRequest;
import java.util.Collections;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetMsvcbValuesDao extends BaseDao {

    /** MSVCB reference (required), e.g. "LD0/SV1.msvcb01" */
    private String ref;

    /** SV enable [0..1] */
    private Boolean svEna;

    /** MSV ID [0..1] */
    private String msvId;

    /** Data set reference [0..1] */
    private String datSet;

    /** Sample mode [0..1] (0=per-nominal-period, 1=per-second, 2=per-sample) */
    private Integer smpMod;

    /** Sample rate [0..1] */
    private Integer smpRate;

    /** Option flags bitmap [0..1] */
    private Integer optFlds;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(ref, "ref must not be null");
        return new CmsSetMsvcbValuesRequest().msvcb(Collections.singletonList(
            new CmsSetMsvcbEntry().reference(ref)
                .svEna(svEna)
                .msvID(msvId)
                .datSet(datSet)
                .smpMod(smpMod)
                .smpRate(smpRate)
                .optFlds(optFlds)
        ));
    }
}
