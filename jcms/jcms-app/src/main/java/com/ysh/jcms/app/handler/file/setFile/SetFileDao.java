package com.ysh.jcms.app.handler.file.setFile;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetFileDao extends BaseDao {
    private String remoteFile; // remote path on server
    private String localFile; // local file to upload
}
