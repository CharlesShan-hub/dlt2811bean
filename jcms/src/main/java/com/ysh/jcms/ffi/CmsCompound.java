package com.ysh.jcms.datatype.block;

import com.ysh.jcms.ffi.CmsFFI;
import com.ysh.jcms.ffi.CmsType;
import com.sun.jna.ptr.IntByReference;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * 手动串联型 Compound — 不依赖 JNA ByValue 和反射 FFI。
 * 子类只需实现 {@link #fields()} 返回字段顺序 + {@link #fieldEncodeBufSize()}。
 */
public abstract class CmsCompound extends CmsType {

    protected CmsCompound() {
        super(false);  // 不绑定 FFI（我们手动编码）
    }

    /** 字段列表，按 PER 编码顺序。返回 [fieldName, CmsType, ...] 三元组。 */
    protected abstract List<FieldDef> fields();

    @Override
    public byte[] encode() {
        write();
        // 每个字段依次编码
        com.sun.jna.Memory buf = new com.sun.jna.Memory(fieldEncodeBufSize());
        int total = 0;
        for (FieldDef fd : fields()) {
            CmsType sub;
            try { sub = (CmsType) fd.field.get(this); } catch (Exception e) { throw new RuntimeException(e); }
            byte[] enc = sub.encode();
            buf.write(total, enc, 0, enc.length);
            total += enc.length;
        }
        return Arrays.copyOf(buf.getByteArray(0, total), total);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CmsType> T decode(byte[] data) {
        int pos = 0;
        for (FieldDef fd : fields()) {
            CmsType sub;
            try { sub = (CmsType) fd.field.get(this); } catch (Exception e) { throw new RuntimeException(e); }
            // 需要知道编码长度或通过 decode 自动确定
            byte[] subData = Arrays.copyOfRange(data, pos, data.length);
            sub.decode(subData);
            // 自动推进：重新编码来确定实际长度
            pos += sub.encode().length;
        }
        read();
        return (T) this;
    }

    protected int fieldEncodeBufSize() { return 512; }

    public static class FieldDef {
        public final Field field;
        public final String name;
        public FieldDef(Field field, String name) {
            this.field = field;
            this.name = name;
        }
    }
}
