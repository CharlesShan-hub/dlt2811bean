package com.ysh.jcms.app.handler.file.deleteFile;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.file.CmsDeleteFileRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class DeleteFileDao extends BaseDao {

    /** File name on server to delete */
    private String fileName;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(fileName, "fileName must not be null");
        return new CmsDeleteFileRequest().filename(fileName);
    }
}
