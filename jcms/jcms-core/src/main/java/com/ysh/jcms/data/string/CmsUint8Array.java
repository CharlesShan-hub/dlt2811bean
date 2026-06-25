package com.ysh.jcms.data.string;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.ysh.jcms.core.CmsType;
import java.util.Arrays;

/**
 * typedef struct { uint8_t *value; int32_t len; } cms_uint8_array_t;
 * sizeof = 16 (Pointer 8 + int32 4 + padding 4)
 *
 * 通用字节数组容器。ObjectName、ObjectReference、SubReference、EntryID
 * 等类型都是它的别名，各自有 encode/decode。
 *
 * CmsUint8Array 只管理 { Pointer, len } 的 struct 布局，
 * encode/decode 由具体的别名类（如 CmsObjectName）提供。
 */
public class CmsUint8Array extends CmsType {

    public static final int SIZEOF = 16;  // value(8) + len(4) + padding(4)

    /** 指向数据的 native 指针。 */
    public Pointer value;

    /** 数据长度（字节）。 */
    public int len;

    /** 由 write() 分配的 data 内存，用于生命周期管理。 */
    protected Memory ownedData;

    /** 子类可覆盖此方法指定默认缓冲区大小（如 BitString 需要更⼤空间）。 */
    protected int defaultBufSize() { return 1; }

    public CmsUint8Array() {
        int sz = defaultBufSize();
        this.ownedData = new Memory(sz);
        this.value = ownedData;
        this.len = 0;
        if (sz > 0) ownedData.setByte(0, (byte)0);  /* 初始化为空字符串 */
        write();
    }

    public CmsUint8Array(int maxLen) {
        this.len = 0;
        if (maxLen > 0) {
            this.ownedData = new Memory(maxLen);
            this.value = ownedData;
        }
        write();
    }

    /** 预分配固定大小的缓冲区，并可选填入初始数据。 */
    public CmsUint8Array(int bufSize, byte[] defaultData) {
        this.ownedData = new Memory(bufSize);
        this.value = ownedData;
        if (defaultData != null && defaultData.length > 0) {
            int copyLen = Math.min(defaultData.length, bufSize);
            ownedData.write(0, defaultData, 0, copyLen);
            this.len = copyLen;
        } else {
            this.len = 0;
        }
        write();
    }

    /** 用已有字节数组初始化（拷贝到 native 内存）。 */
    public CmsUint8Array(byte[] data) {
        value(data);
    }

    /** 用字符串初始化（UTF-8 编码）。 */
    public CmsUint8Array(String s) {
        value(s);
    }

    // ==================== 读写数据 ====================

    /** 子类可覆盖此方法以改变 len 到实际字节数的映射（如 BitString 存比特数）。 */
    protected int valueByteLen() { return len; }

    /** 获取字节数组（从 native 拷贝到 Java）。 */
    public byte[] value() {
        if (value == null || len == 0) return new byte[0];
        return value.getByteArray(0, valueByteLen());
    }

    /** 设置字节数组（拷贝到 native）。 */
    public CmsUint8Array value(byte[] data) {
        if (data == null || data.length == 0) {
            this.value = null;
            this.len = 0;
            this.ownedData = null;
        } else {
            int size = data.length;
            this.ownedData = new Memory(size + 1);
            this.ownedData.write(0, data, 0, size);
            this.ownedData.setByte(size, (byte) 0);  // null terminate
            this.value = ownedData;
            this.len = size;
        }
        write();
        return this;
    }

    /** 设置字符串（UTF-8 编码）。 */
    public CmsUint8Array value(String s) {
        return value(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    // ==================== 布局 ====================

    @Override
    protected int calcNativeSize() { return SIZEOF; }

    @Override
    public void write() {
        if (value == null) {
            // 自动分配默认缓冲区，确保 C decode 有地方写
            ownedData = new Memory(256);
            value = ownedData;
            len = 0;
        }
        int ofs = 0;
        nativePtr.setPointer(ofs, value); ofs += 8;
        nativePtr.setInt(ofs, len);
    }

    @Override
    public void read() {
        int ofs = 0;
        Pointer v = nativePtr.getPointer(ofs); ofs += 8;
        int n = nativePtr.getInt(ofs);
        // if C decoder left garbage for absent fields, treat as empty
        if (v == null || n <= 0) {
            this.value = null;
            this.len = 0;
            return;
        }
        this.value = v;
        this.len = n;
    }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CmsUint8Array)) return false;
        // compare by logical data content; value() handles empty case
        return Arrays.equals(value(), ((CmsUint8Array) o).value());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value());
    }
}

