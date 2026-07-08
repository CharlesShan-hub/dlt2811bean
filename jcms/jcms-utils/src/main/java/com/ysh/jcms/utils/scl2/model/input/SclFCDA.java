package com.ysh.jcms.utils.scl2.model.input;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclFCDA {

    private String ldInst;
    private String prefix;
    private String lnClass;
    private String lnInst;
    private String doName;
    private String daName;
    private String fc;
    private Integer ix;

    public String buildFcdaRef() {
        return ldInst + "/" + prefix + lnClass + lnInst + "." + doName
                + (daName != null ? "." + daName : "");
    }

    public String buildLnName() {
        return prefix + lnClass + lnInst;
    }
}
