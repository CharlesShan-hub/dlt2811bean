package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

public class ConfirmEditSgValuesDao {
    private String sgcbReference;

    public String sgcbReference() {
        return sgcbReference;
    }
    public ConfirmEditSgValuesDao sgcbReference(String v) {
        this.sgcbReference = v;
        return this;
    }
}
