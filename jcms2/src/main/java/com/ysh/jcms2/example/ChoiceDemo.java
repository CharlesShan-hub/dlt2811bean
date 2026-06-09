package com.ysh.jcms2.example;

import com.ysh.jcms2.*;
import com.ysh.jcms2.choice.*;
import com.ysh.jcms2.CmsInt8;
import com.ysh.jcms2.CmsInt8U;

/**
 * 演示"叶子 + 容器"模式。
 *
 * 叶子（Leaf）：Boolean, Int8, ServiceError...
 *   没有 children，自管 native 内存，自己 write/read
 *
 * 容器（Container）：SEQUENCE
 *   字段是 CmsType 对象，通过 children() 声明
 *   基类默认逻辑：遍历 children → child.write() → 写 child.nativePtr 到 parent 内存
 *
 * 容器（CHOICE）：
 *   choice inline + 每个备选一个指针
 *   通过 alternatives() + CmsChoice 基类处理
 */
public class ChoiceDemo {

    public static void main(String[] args) {

        // ========== 叶子类型 ==========

        CmsBoolean b = new CmsBoolean(true);
        CmsServiceError err = new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        CmsInt32U ver = new CmsInt32U(100);

        System.out.println("=== 叶子类型 ===");
        System.out.println("Boolean        nativeSize=" + b.nativeSize + "  value=" + b.value());
        System.out.println("ServiceError   nativeSize=" + err.nativeSize + "  value=" + err.value());
        System.out.println("Int32U         nativeSize=" + ver.nativeSize + "  value=" + ver.value());

        b.write();   // → 写 4 字节到 nativePtr
        err.write(); // → 写 4 字节到 nativePtr
        ver.write(); // → 写 4 字节到 nativePtr

        // ========== 容器 (SEQUENCE) ==========

        System.out.println("\n=== 容器: CmsRpcMethod ===");

        CmsRpcMethod method = new CmsRpcMethod();
        method.version.value(1);
        method.timeout.value(5000);

        System.out.println("  nativeSize = " + method.nativeSize);  // 32 (4 × 8)
        System.out.println("  version    = " + method.version.value());
        System.out.println("  timeout    = " + method.timeout.value());

        System.out.println("  version.nativePtr = " + method.version.nativePtr);
        System.out.println("  timeout.nativePtr = " + method.timeout.nativePtr);

        // write → 遍历 children: version.write() + 写 version.nativePtr, timeout.write() + 写 timeout.nativePtr
        method.write();

        System.out.println("  method.nativePtr[@0] = " + method.nativePtr.getPointer(0) + "  ← version.nativePtr");
        System.out.println("  method.nativePtr[@8] = " + method.nativePtr.getPointer(8) + "  ← timeout.nativePtr");

        // ========== 容器 (CHOICE) ==========

        System.out.println("\n=== 容器: CmsErrorMethodChoice (CHOICE) ===");

        CmsErrorMethodChoice c = new CmsErrorMethodChoice();
        c.choice(1);  // 选 method
        c.method.version.value(2);
        c.method.timeout.value(3000);

        System.out.println("  nativeSize = " + c.nativeSize);  // 20 (4 + 2×8)
        System.out.println("  choice     = " + c.choice());
        System.out.println("  method.ver = " + c.method.version.value());
        System.out.println("  method.to  = " + c.method.timeout.value());

        // write → choice inline + 写每个备选的 pointer
        c.write();

        System.out.println("  c.nativePtr[@0]  = " + c.nativePtr.getInt(0) + "  ← choice");
        System.out.println("  c.nativePtr[@4]  = " + c.nativePtr.getPointer(4) + "  ← error.nativePtr");
        System.out.println("  c.nativePtr[@12] = " + c.nativePtr.getPointer(12) + "  ← method.nativePtr");

        // ========== PER 编码/解码 roundtrip ==========

        System.out.println("\n=== PER 编解码 Roundtrip ===");
        try {
            // Boolean
            CmsBoolean bEnc = new CmsBoolean(true);
            byte[] enc = bEnc.encode();
            System.out.println("Boolean encode: " + bytesToHex(enc) + " (" + enc.length + " bytes)");

            CmsBoolean bDec = new CmsBoolean();
            bDec.decode(enc);
            System.out.println("Boolean decode: " + bDec.value() + "  (ok=" + (bDec.value() == true) + ")");

            // Int8
            CmsInt8 i8 = new CmsInt8(-42);
            byte[] enc8 = i8.encode();
            System.out.println("Int8 encode: " + bytesToHex(enc8) + " (" + enc8.length + " bytes)");

            CmsInt8 i8d = new CmsInt8();
            i8d.decode(enc8);
            System.out.println("Int8 decode: " + i8d.value() + "  (ok=" + (i8d.value() == -42) + ")");

            // Int8U
            CmsInt8U u8 = new CmsInt8U(200);
            byte[] encu8 = u8.encode();
            System.out.println("Int8U encode: " + bytesToHex(encu8) + " (" + encu8.length + " bytes)");

            CmsInt8U u8d = new CmsInt8U();
            u8d.decode(encu8);
            System.out.println("Int8U decode: " + u8d.value() + "  (ok=" + (u8d.value() == 200) + ")");

        } catch (UnsatisfiedLinkError e) {
            System.out.println("  (ccms 库未加载，跳过 FFI 测试: " + e.getMessage() + ")");
        } catch (Exception e) {
            System.out.println("  FFI 错误: " + e.getMessage());
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02X ", b));
        return sb.toString().trim();
    }
}
