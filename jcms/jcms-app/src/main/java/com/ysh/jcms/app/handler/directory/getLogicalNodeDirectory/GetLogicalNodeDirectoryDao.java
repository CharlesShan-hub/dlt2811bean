package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetLogicalNodeDirectoryDao {

    /** ldName (e.g. "C1") — alternative to lnReference */
    private String ldName;
    /** lnReference (e.g. "C1/LLN0") — alternative to ldName */
    private String lnReference;
    /** ACSI class to query, default DATA_OBJECT(1) */
    private int acsiClass = 1;
    /** Optional pagination: return items after this reference */
    private String referenceAfter;
}
