package com.ysh.jcms2.core;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import java.util.Collections;
import java.util.List;

/**
 * jcms2 基类。
 *
 * 两种模式：
 *
 * 1. 叶子类型（Boolean, Int8 等）：
 *    - 没有 children
 *    - 自管 native 内存，自实现 write()/read()/encode()/decode()
 *
 * 2. 容器类型（SEQUENCE, CHOICE 等）：
 *    - children() 返回子字段列表
 *    - 基类自动 write/read：把 child 的 nativePtr 按顺序写入 parent 的 native 内存
 *    - encode()/decode() 由 C 侧负责（需要 C 函数支持）
 */
public abstract class CmsType {

    public Pointer nativePtr;
    public int nativeSize;

    protected CmsType() {
        this.nativeSize = calcNativeSize();
        this.nativePtr = new Memory(nativeSize);
        zero();
    }

    // ==================== 子类可重写的 ====================

    /** 返回子字段列表（容器类型重写，叶子类型返回空列表）。 */
    public List<? extends CmsType> children() {
        return Collections.emptyList();
    }

    /** 计算 native 内存大小。叶子类型重写，容器类型默认 children.size() * 8。 */
    protected int calcNativeSize() {
        return children().size() * 8;
    }

    /**
     * 同步 Java 字段 → native 内存。
     * - 叶子类型：自己写字段到 nativePtr
     * - 容器类型：遍历 children → child.write() → 写 child.nativePtr
     */
    public void write() {
        List<? extends CmsType> kids = children();
        for (int i = 0; i < kids.size(); i++) {
            CmsType child = kids.get(i);
            child.write();
            nativePtr.setPointer(i * 8L, child.nativePtr);
        }
    }

    /**
     * 同步 native 内存 → Java 字段。
     * - 叶子类型：从 nativePtr 读字段
     * - 容器类型：读 child 指针 → 设置 child.nativePtr → child.read()
     */
    public void read() {
        List<? extends CmsType> kids = children();
        for (int i = 0; i < kids.size(); i++) {
            CmsType child = kids.get(i);
            Pointer ptr = nativePtr.getPointer(i * 8L);
            if (ptr != null) {
                child.nativePtr = ptr;
                child.read();
            }
        }
    }

    /**
     * PER 编码：将当前结构编码为字节数组。
     * - 叶子类型：write() 后再调 C FFI
     * - 容器类型：先 write()（递归写所有 children），再调 C FFI
     */
    public byte[] encode() {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no FFI encode");
    }

    /**
     * PER 解码：从字节数组解码到当前结构。
     * - 叶子类型：调 C FFI 后再 read()
     * - 容器类型：调 C FFI 后再 read()（递归读所有 children）
     */
    public void decode(byte[] data) {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no FFI decode");
    }

    // ==================== 公共工具 ====================

    public void zero() {
        if (nativePtr != null) {
            nativePtr.clear((long) nativeSize);
        }
    }
}
