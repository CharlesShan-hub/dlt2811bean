package com.ysh.jcms.utils.scl.model.input;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclExtRef {

    // local side
    private String desc;
    private String ldInst;
    private String prefix;
    private String lnClass;
    private String lnInst;
    private String doName;
    private String daName;

    // source side
    private String iedName;
    private String serviceType;
    private String srcLDInst;
    private String srcPrefix;
    private String srcLnClass;
    private String srcLnInst;
    private String srcCBName;

    // ExtRef attributes per XSD
    private String intAddr;
    private String pServT;
    private String pLN;
    private String pDO;
    private String pDA;
}
