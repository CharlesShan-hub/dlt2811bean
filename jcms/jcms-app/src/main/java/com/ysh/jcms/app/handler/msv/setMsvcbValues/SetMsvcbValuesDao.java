package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetMsvcbValuesDao extends BaseDao {
    private String ref;
    private String svEna;
    private String msvId;
    private String datSet;
}
