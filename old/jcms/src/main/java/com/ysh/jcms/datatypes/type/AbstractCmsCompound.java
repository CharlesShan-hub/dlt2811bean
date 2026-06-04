package com.ysh.jcms.datatypes.type;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCmsCompound<T extends AbstractCmsCompound<T>>
        extends Structure implements CmsCompound<T> {

    protected final String typeName;
    protected boolean optional = false;
    protected boolean present = true;

    protected AbstractCmsCompound(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean isOptional() { return optional; }

    @Override
    public void setOptional(boolean optional) { this.optional = optional; }

    @Override
    public boolean isPresent() { return present; }

    @Override
    public void setPresent(boolean present) { this.present = present; }

    // ==================== Subclass hooks ====================

    /** Subclasses must declare @Override getFieldOrder() (no more NativeStruct). */
    @Override
    protected abstract List<String> getFieldOrder();

    /**
     * Sync Java-friendly fields → native layout before FFI encode.
     * Default no-op — override only if you have Java-friendly wrapper fields.
     */
    protected void syncToNative() {}

    /**
     * Sync native layout → Java-friendly fields after FFI decode + read().
     * Default no-op — override only if you have Java-friendly wrapper fields.
     */
    protected void syncFromNative() {}

    /** FFI encode: call the C function with {@code this} as the struct parameter. */
    protected abstract int ffiEncode(byte[] buf, IntByReference outLen);

    /** FFI decode: call the C function, then read() and syncFromNative(). */
    protected abstract void ffiDecode(byte[] data);

    /**
     * Java PER encode fallback — writes PER-encoded data to {@code pos}.
     * Invoked when FFI library is unavailable. Default throws.
     */
    protected void perEncode(PerOutputStream pos) {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no Java PER encode fallback");
    }

    /**
     * Java PER decode fallback — reads PER-encoded data from {@code pis}.
     * Invoked when FFI library is unavailable. Default throws.
     */
    protected void perDecode(PerInputStream pis) {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no Java PER decode fallback");
    }

    /** Buffer size hint for FFI encode. Override if larger buffer needed. */
    protected int encodeBufSize() {
        return 4096;
    }

    // ==================== Encode / Decode ====================

    @Override
    public byte[] encode() {
        if (CmsFFIDatatypes.isAvailable()) {
            syncToNative();
            write();                    // JNA: Java fields → native memory
            byte[] buf = new byte[encodeBufSize()];
            IntByReference outLen = new IntByReference(buf.length);
            ffiEncode(buf, outLen);
            byte[] result = new byte[outLen.getValue()];
            System.arraycopy(buf, 0, result, 0, result.length);
            return result;
        }
        PerOutputStream pos = new PerOutputStream();
        perEncode(pos);
        return pos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public T decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            ffiDecode(data);
            read();                     // JNA: native memory → Java fields
            syncFromNative();
        } else {
            perDecode(new PerInputStream(data));
        }
        return (T) this;
    }

    // ==================== copy ====================

    @Override
    @SuppressWarnings("unchecked")
    public T copy() {
        try {
            T clone = (T) getClass().getDeclaredConstructor().newInstance();
            Class<?> clazz = getClass();
            while (clazz != null && clazz != Object.class) {
                for (Field field : clazz.getDeclaredFields()) {
                    int mod = field.getModifiers();
                    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) continue;
                    String name = field.getName();
                    if ("typeName".equals(name)) continue;
                    field.setAccessible(true);
                    Object srcVal = field.get(this);
                    if (srcVal instanceof byte[]) {
                        field.set(clone, ((byte[]) srcVal).clone());
                    } else if (srcVal instanceof Structure) {
                        // skip embedded structures — they are handled by Structure itself
                    } else {
                        field.set(clone, srcVal);
                    }
                }
                clazz = clazz.getSuperclass();
            }
            return clone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy " + typeName, e);
        }
    }

    // ==================== toString ====================

    @Override
    public String toString() {
        List<String> lines = new ArrayList<>();
        Class<?> clazz = getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) continue;
                String name = field.getName();
                if ("typeName".equals(name)) continue;
                try {
                    field.setAccessible(true);
                    Object val = field.get(this);
                    String valStr = formatValue(val);
                    lines.add(name + ": " + valStr);
                } catch (Exception e) {
                    lines.add(name + ": <error>");
                }
            }
            clazz = clazz.getSuperclass();
        }
        if (lines.isEmpty()) {
            return "(" + getClass().getSimpleName() + ") {}";
        }
        StringBuilder sb = new StringBuilder("(").append(getClass().getSimpleName()).append(") {\n");
        for (String line : lines) {
            sb.append("    ").append(line).append(",\n");
        }
        sb.setLength(sb.length() - 2);
        sb.append("\n}");
        return sb.toString();
    }

    private static String formatValue(Object val) {
        if (val == null) return "null";
        if (val instanceof byte[]) {
            byte[] bytes = (byte[]) val;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < Math.min(bytes.length, 16); i++) {
                if (i > 0) sb.append(" ");
                sb.append(String.format("%02X", bytes[i]));
            }
            if (bytes.length > 16) sb.append(" ...");
            sb.append("]");
            return sb.toString();
        }
        if (val instanceof Structure) return val.toString();
        return val.toString();
    }
}
