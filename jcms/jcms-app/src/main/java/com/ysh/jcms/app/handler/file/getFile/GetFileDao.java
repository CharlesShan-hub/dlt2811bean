package com.ysh.jcms.app.handler.file.getFile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetFileDao {
    private String fileName;
    private String outputFile; // local path to save, null to print info only
}
