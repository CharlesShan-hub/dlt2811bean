package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclAssociation {
    /** 关联的 IED 名称 (iedName) */
    private String iedName;
    /** 逻辑设备实例 (ldInst) */
    private String ldInst;
    /** 前缀 (prefix) */
    private String prefix;
    /** LN 类 (lnClass) */
    private String lnClass;
    /** LN 实例号 (lnInst) */
    private String lnInst;
    /** 关联类型 (kind): pre-established / predefined */
    private String kind;
    /** 关联标识 (associationID) */
    private String associationID;
}
