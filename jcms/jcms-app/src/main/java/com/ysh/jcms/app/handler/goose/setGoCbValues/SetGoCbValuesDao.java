package com.ysh.jcms.app.handler.goose.setGoCbValues;

import com.ysh.jcms.svc.goose.CmsSetGoCbEntry;
import com.ysh.jcms.svc.goose.CmsSetGoCbValuesRequest;

public class SetGoCbValuesDao {
    private String ref;
    private Boolean goEna;
    private String goID;
    private String datSet;

    public SetGoCbValuesDao ref(String v) { this.ref = v; return this; }
    public SetGoCbValuesDao goEna(Boolean v) { this.goEna = v; return this; }
    public SetGoCbValuesDao goID(String v) { this.goID = v; return this; }
    public SetGoCbValuesDao datSet(String v) { this.datSet = v; return this; }

    public String ref() { return ref; }

    CmsSetGoCbValuesRequest toRequest(int reqId) {
        CmsSetGoCbValuesRequest req = new CmsSetGoCbValuesRequest().reqId(reqId);
        CmsSetGoCbEntry entry = new CmsSetGoCbEntry()
            .reference(ref != null ? ref : "");

        if (goEna != null) { entry.goEnaPresent(true); entry.goEna(goEna); }
        if (goID != null) entry.goId(goID);
        if (datSet != null) entry.datSet(datSet);

        req.gocb.add(entry);
        return req;
    }
}
