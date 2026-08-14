package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.core.data.bitarray.CmsLcbOptFlds;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.log.CmsSetLcbEntry;
import com.ysh.jcms.core.pdu.log.CmsSetLcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetLcbValuesDao extends BaseDao {
    private String ref;
    private Boolean logEna;
    private String datSet;
    private Integer trgOps;
    private Integer intgPd;
    private String logRef;
    private Integer optFlds;
    private Integer bufTm;

    @Override
    public CmsType toRequest() {
        CmsSetLcbValuesRequest req = new CmsSetLcbValuesRequest();
        CmsSetLcbEntry entry = new CmsSetLcbEntry().reference(ref != null ? ref : "");

        if (logEna != null) {
            entry.logEna(logEna);
        }
        if (datSet != null)
            entry.datSet(datSet);
        if (trgOps != null) {
            entry.trgOps(new CmsTriggerConditions().integrity((trgOps & 1) != 0).data_change((trgOps & 2) != 0)
                    .quality_change((trgOps & 4) != 0).data_update((trgOps & 8) != 0).general_interrogation((trgOps & 16) != 0));
        }
        if (intgPd != null) {
            entry.intgPd(intgPd);
        }
        if (logRef != null)
            entry.logRef(logRef);
        if (optFlds != null) {
            entry.optFlds(new CmsLcbOptFlds().bit0((optFlds & 1) != 0));
        }
        if (bufTm != null) {
            entry.bufTm(bufTm);
        }

        req.lcb.add(entry);
        return req;
    }
}
