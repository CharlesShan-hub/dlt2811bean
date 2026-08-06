package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import lombok.Getter;
import com.ysh.jcms.app.handler.BaseDao;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class LdDirDao extends BaseDao {

    private String ldName;
    private String referenceAfter;
}
