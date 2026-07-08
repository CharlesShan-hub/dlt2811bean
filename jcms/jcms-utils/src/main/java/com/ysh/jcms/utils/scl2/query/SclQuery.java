package com.ysh.jcms.utils.scl2.query;

import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.model.SclIED;

import java.util.Collections;
import java.util.List;

public class SclQuery {

    private final SclDocument document;

    public SclQuery(SclDocument document) {
        this.document = document;
    }

    public List<SclIED> ieds() {
        return document != null ? document.getIeds() : Collections.emptyList();
    }

    public SclIED ied(String name) {
        return document != null ? document.findIedByName(name) : null;
    }

    public DataTypeQuery dataTypes() {
        return null; // TODO
    }

    public IedQuery iedQuery(String name) {
        return null; // TODO
    }

    public String resolveBType(String ref) {
        return null; // TODO
    }
}
