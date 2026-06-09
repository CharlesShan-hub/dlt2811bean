package com.ysh.jcms2;

import com.ysh.jcms2.choice.CmsDataDefinition;
import java.util.Arrays;
import java.util.List;

/**
 * SEQUENCE {
 *     version  [0] INT32U,
 *     timeout  [1] INT32U,
 *     request  [2] DataDefinition,
 *     response [3] DataDefinition
 * }
 *
 * 容器类型 — 字段是 CmsType 对象，通过 children() 声明。
 * parent 的 native 内存存指针指向每个 child。
 *
 *  nativeSize = 4 × 8 = 32 字节
 *  ┌───────ptr→version───────┬───────ptr→timeout───────┬───────ptr→request──────┬───────ptr→response──────┐
 *  └─────────────────────────┴─────────────────────────┴────────────────────────┴─────────────────────────┘
 *    version.nativePtr        timeout.nativePtr         request.nativePtr        response.nativePtr
 *    (4 字节 int32)           (4 字节 int32)            (CmsDataDefinition)      (CmsDataDefinition)
 */
public class CmsRpcMethod extends CmsType {

    public CmsInt32U version;
    public CmsInt32U timeout;
    public CmsDataDefinition request;
    public CmsDataDefinition response;

    public CmsRpcMethod() {
        this.version = new CmsInt32U();
        this.timeout = new CmsInt32U();
        this.request = new CmsDataDefinition();
        this.response = new CmsDataDefinition();
        // nativeSize 由基类自动计算 = children().size() * 8
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(version, timeout, request, response);
    }
}
