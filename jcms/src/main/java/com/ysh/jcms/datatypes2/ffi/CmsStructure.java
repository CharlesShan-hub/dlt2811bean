package com.ysh.jcms.datatypes2.ffi;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

import java.util.Arrays;

/**
 * 所有 Structure 映射类型的基类，提供统一的 encode/decode 能力。
 *
 * <p>子类只需实现 {@link #ffiEncode(byte[], IntByReference)} 和
 * {@link #ffiDecode(byte[])}，即可通过 {@link #encode()} / {@link #decode(byte[])}
 * 完成编码解码。
 */
public abstract class CmsStructure extends Structure {

    protected CmsStructure() {}

    /** 调用 C FFI 编码，返回编码后字节数。 */
    protected abstract int ffiEncode(byte[] buf, IntByReference outLen);

    /** 调用 C FFI 解码。 */
    protected abstract void ffiDecode(byte[] data);

    /** 编码缓冲区大小提示。子类可重写。 */
    protected int encodeBufSize() { return 4096; }

    /**
     * 编码当前结构体为字节数组。
     * <p>自动调用 {@link #write()} 同步 Java 字段到 native 内存。
     */
    public byte[] encode() {
        write();
        byte[] buf = new byte[encodeBufSize()];
        IntByReference outLen = new IntByReference(buf.length);
        ffiEncode(buf, outLen);
        return Arrays.copyOf(buf, outLen.getValue());
    }

    /**
     * 从字节数组解码到新结构体实例。
     * <p>自动调用 {@link #read()} 同步 native 内存到 Java 字段。
     */
    @SuppressWarnings("unchecked")
    public <T extends CmsStructure> T decode(byte[] data) {
        ffiDecode(data);
        read();
        return (T) this;
    }
}
