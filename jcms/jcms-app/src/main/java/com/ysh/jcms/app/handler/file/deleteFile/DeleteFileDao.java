package com.ysh.jcms.app.handler.file.deleteFile;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.file.CmsDeleteFileRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class DeleteFileDao extends BaseDao {
    private String fileName;

    @Override
    public CmsType toRequest() {
        return new CmsDeleteFileRequest().filename(fileName);
    }
}
