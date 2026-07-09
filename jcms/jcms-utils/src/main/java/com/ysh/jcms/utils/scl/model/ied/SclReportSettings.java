package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclReportSettings {

    private String bufTime;
    private String cbName;
    private String rptID;
    private String datSet;
    private String intgPd;
    private String optFields;
}
