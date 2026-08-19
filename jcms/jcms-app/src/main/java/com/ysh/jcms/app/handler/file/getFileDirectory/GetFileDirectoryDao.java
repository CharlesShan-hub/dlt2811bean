package com.ysh.jcms.app.handler.file.getFileDirectory;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.file.CmsGetFileDirectoryRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetFileDirectoryDao extends BaseDao {

    /** Directory path on server (required) */
    private String pathName;

    /** Start time filter [0..1] (not parsed for CLI simplicity) */
    private String startTime;

    /** Stop time filter [0..1] (not parsed for CLI simplicity) */
    private String stopTime;

    /** Return files after this name [0..1] */
    private String fileAfter;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(pathName, "pathName must not be null");
        return new CmsGetFileDirectoryRequest()
            .pathName(pathName)
            .fileAfter(fileAfter);
        // startTime/stopTime would need parsing — skipped for CLI simplicity
    }
}
