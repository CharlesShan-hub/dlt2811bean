package com.ysh.jcms2.choice;

import com.ysh.jcms2.*;
import java.util.Arrays;
import java.util.List;

/**
 * CHOICE { error [0] ServiceError, method [1] SEQUENCE { ... } }
 *
 * native 布局 (4 + 2 × 8 = 20 字节):
 *   [choice: int32] [ptr→error] [ptr→method]
 *
 * C 侧:
 *   int32_t               choice;
 *   cms_service_error_t  *error;
 *   CmsRpcMethod         *method;
 */
public class CmsErrorMethodChoice extends CmsChoice {

    public CmsServiceError error;
    public CmsRpcMethod    method;

    public CmsErrorMethodChoice() {
        this.error = new CmsServiceError();
        this.method = new CmsRpcMethod();
    }

    @Override
    public List<? extends CmsType> alternatives() {
        return Arrays.asList(error, method);
    }
}
