package com.ysh.jcms.app.handler.file.deleteFile;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class DeleteFileDao extends BaseDao {
    private String fileName;
}
