package com.ysh.jcms.app.handler.file.setFile;

import com.ysh.jcms.app.handler.base.BaseDao;
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
    private long position = 1; // upload start position, advanced per chunk
    private byte[] fileData; // chunk payload
    private boolean endOfFile; // marks the final chunk

    @Override
    public CmsType toRequest() {
        return new CmsSetFileRequest().filename(remoteFile).startPosition(position).fileData(fileData).endOfFile(endOfFile);
    }
}
