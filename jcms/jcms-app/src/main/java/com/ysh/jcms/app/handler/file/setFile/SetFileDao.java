package com.ysh.jcms.app.handler.file.setFile;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.file.CmsSetFileRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetFileDao extends BaseDao {
    private String remoteFile; // remote path on server
    private String localFile; // local file to upload

    @Override
    public CmsType toRequest() {
        return new CmsSetFileRequest().filename(remoteFile).startPosition(1);
    }
}
