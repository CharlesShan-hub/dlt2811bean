package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGooseElementNumberDao extends BaseDao {
    private String gocbReference;
    private final List<GetGooseElementNumberClient.MemberSpec> members = new ArrayList<>();

    public GetGooseElementNumberDao addMember(String reference, int fc) {
        members.add(new GetGooseElementNumberClient.MemberSpec(reference, fc));
        return this;
    }
}
