package com.ysh.jcms.app.handler.sg.selectActiveSg;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectActiveSgDao {
    private String sgcbReference;
    private int settingGroupNumber;
}
