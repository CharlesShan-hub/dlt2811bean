package com.ysh.jcms2.example;

import com.ysh.jcms2.*;

/**
 * 最简演示 — 叶子类型（自管 native）+ 容器类型（children 指针）
 */
public class Demo {

    public static void main(String[] args) {
        // ---- 叶子 ----
        CmsBoolean b = new CmsBoolean(true);
        System.out.println("Boolean: " + b.value() + "  nativeSize=" + b.nativeSize);

        CmsInt8 i8 = new CmsInt8(-42);
        System.out.println("Int8: " + i8.value() + "  nativeSize=" + i8.nativeSize);

        CmsInt8U u8 = new CmsInt8U(200);
        System.out.println("Int8U: " + u8.value() + "  nativeSize=" + u8.nativeSize);

        // ---- 容器 SEQUENCE ----
        CmsRpcMethod m = new CmsRpcMethod();
        m.version.value(1);
        m.timeout.value(5000);
        System.out.println("RpcMethod: nativeSize=" + m.nativeSize
            + "  version=" + m.version.value() + "  timeout=" + m.timeout.value());

        // ---- 容器 CHOICE ----
        // 见 ChoiceDemo.java
    }
}
