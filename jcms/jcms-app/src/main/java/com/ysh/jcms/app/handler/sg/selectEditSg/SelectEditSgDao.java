package com.ysh.jcms.app.handler.sg.selectEditSg;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectEditSgDao {
    private String sgcbReference;
    private int settingGroupNumber;
}
