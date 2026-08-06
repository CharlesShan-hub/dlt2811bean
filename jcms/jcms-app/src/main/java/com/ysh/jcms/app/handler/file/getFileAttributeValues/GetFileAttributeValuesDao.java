package com.ysh.jcms.app.handler.file.getFileAttributeValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.file.CmsGetFileAttributeValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetFileAttributeValuesDao extends BaseDao {
    private String fileName;

    @Override
    public CmsType toRequest() {
        return new CmsGetFileAttributeValuesRequest().filename(fileName);
    }
}
