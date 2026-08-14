package com.ysh.jcms.app.handler.goose.setGoCbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.goose.CmsSetGoCbEntry;
import com.ysh.jcms.core.pdu.goose.CmsSetGoCbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetGoCbValuesDao extends BaseDao {
    private String ref;
    private Boolean goEna;
    private String goID;
    private String datSet;

    @Override
    public CmsType toRequest() {
        CmsSetGoCbValuesRequest req = new CmsSetGoCbValuesRequest();
        CmsSetGoCbEntry entry = new CmsSetGoCbEntry().reference(ref != null ? ref : "");

        if (goEna != null) {
            entry.goEna(goEna);
        }
        if (goID != null)
            entry.goID(goID);
        if (datSet != null)
            entry.datSet(datSet);

        req.gocb.add(entry);
        return req;
    }
}
