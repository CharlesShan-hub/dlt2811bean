package com.ysh.jcms.app.handler.file.setFile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetFileDao {
    private String remoteFile;   // remote path on server
    private String localFile;    // local file to upload
}
