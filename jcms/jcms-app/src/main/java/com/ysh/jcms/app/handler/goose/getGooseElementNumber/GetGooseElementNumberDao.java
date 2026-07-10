package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import java.util.ArrayList;
import java.util.List;

public class GetGooseElementNumberDao {
    private String gocbReference;
    private final List<GetGooseElementNumberClient.MemberSpec> members = new ArrayList<>();

    public GetGooseElementNumberDao gocbReference(String v) {
        this.gocbReference = v;
        return this;
    }
    public GetGooseElementNumberDao addMember(String reference, int fc) {
        members.add(new GetGooseElementNumberClient.MemberSpec(reference, fc));
        return this;
    }

    public String gocbReference() {
        return gocbReference;
    }
    public List<GetGooseElementNumberClient.MemberSpec> members() {
        return members;
    }
}
