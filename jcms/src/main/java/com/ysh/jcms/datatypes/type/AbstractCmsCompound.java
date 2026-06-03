package com.ysh.jcms.datatypes.type;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCmsCompound<T extends AbstractCmsCompound<T>> extends AbstractCmsType<T> implements CmsCompound<T> {

    protected Structure nativeStruct;

    protected AbstractCmsCompound(String typeName) {
        super(typeName);
    }

    protected void setNativeStruct(Structure s) {
        this.nativeStruct = s;
    }

    protected Structure getNativeStruct() {
        return nativeStruct;
    }

    protected void write() {
        if (nativeStruct != null) nativeStruct.write();
    }

    protected void read() {
        if (nativeStruct != null) nativeStruct.read();
    }

    protected Pointer getPointer() {
        return nativeStruct != null ? nativeStruct.getPointer() : null;
    }

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
                    if ("nativeStruct".equals(name)) continue;
                    field.setAccessible(true);
                    Object srcVal = field.get(this);
                    if (srcVal instanceof byte[]) {
                        field.set(clone, ((byte[]) srcVal).clone());
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

    @Override
    public String toString() {
        List<String> lines = new ArrayList<>();
        Class<?> clazz = getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) continue;
                String name = field.getName();
                if ("nativeStruct".equals(name)) continue;
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
