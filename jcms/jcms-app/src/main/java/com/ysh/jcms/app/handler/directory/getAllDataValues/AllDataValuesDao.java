package com.ysh.jcms.app.handler.directory.getAllDataValues;

import lombok.Getter;
import com.ysh.jcms.app.handler.BaseDao;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class AllDataValuesDao extends BaseDao {

    /** ldName (e.g. "LD0") — alternative to lnReference */
    private String ldName;
    /** lnReference (e.g. "LD0/LLN0") — alternative to ldName */
    private String lnReference;
    /** Optional FunctionalConstraint filter */
    private Integer fc;
    /** Optional pagination: return items after this reference */
    private String referenceAfter;
}
