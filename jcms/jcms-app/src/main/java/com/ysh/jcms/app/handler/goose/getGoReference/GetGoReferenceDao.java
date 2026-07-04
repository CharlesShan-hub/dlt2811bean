package com.ysh.jcms.app.handler.goose.getGoReference;

import java.util.ArrayList;
import java.util.List;

public class GetGoReferenceDao {
    private String gocbReference;
    private final List<Integer> memberOffsets = new ArrayList<>();

    public GetGoReferenceDao gocbReference(String v) { this.gocbReference = v; return this; }
    public GetGoReferenceDao addMemberOffset(int offset) { memberOffsets.add(offset); return this; }

    public String gocbReference() { return gocbReference; }
    public List<Integer> memberOffsets() { return memberOffsets; }
}
