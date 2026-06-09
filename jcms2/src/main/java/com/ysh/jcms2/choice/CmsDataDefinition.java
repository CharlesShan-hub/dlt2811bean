package com.ysh.jcms2.choice;

import com.ysh.jcms2.*;
import java.util.Arrays;
import java.util.List;

/**
 * DataDefinition ::= CHOICE { 24 种备选 }
 *
 * 简化版 — 只列出常用的几个，其余为空。
 */
public class CmsDataDefinition extends CmsChoice {

    public CmsServiceError error;          //  0
    // ... 其余 23 个

    public CmsDataDefinition() {
        this.error = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> alternatives() {
        return Arrays.asList(error);
    }
}
