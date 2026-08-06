package com.ysh.jcms.app.handler.file.getFileDirectory;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetFileDirectoryDao extends BaseDao {
    private String pathName;
    private String startTime;
    private String stopTime;
    private String fileAfter;
}
