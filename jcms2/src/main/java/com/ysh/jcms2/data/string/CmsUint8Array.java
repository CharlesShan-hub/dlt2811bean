package com.ysh.jcms2.data.string;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.ysh.jcms2.core.CmsType;

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
    private Memory ownedData;

    public CmsUint8Array() {}

    public CmsUint8Array(int maxLen) {
        this.len = 0;
        if (maxLen > 0) {
            this.ownedData = new Memory(maxLen);
            this.value = ownedData;
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

    /** 获取字节数组（从 native 拷贝到 Java）。 */
    public byte[] value() {
        if (value == null || len == 0) return new byte[0];
        return value.getByteArray(0, len);
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
        int ofs = 0;
        nativePtr.setPointer(ofs, value); ofs += 8;
        nativePtr.setInt(ofs, len);
    }

    @Override
    public void read() {
        int ofs = 0;
        this.value = nativePtr.getPointer(ofs); ofs += 8;
        this.len = nativePtr.getInt(ofs);
    }
}
