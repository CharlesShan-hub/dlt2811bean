package com.ysh.jcms.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CmsConfigInjector {

    private static final Logger log = LoggerFactory.getLogger(CmsConfigInjector.class);

    public static void inject(Object target) {
        CmsConfig config = CmsConfigLoader.load();
        Class<?> clazz = target.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            CmsValue annotation = field.getAnnotation(CmsValue.class);
            if (annotation == null)
                continue;

            String path = annotation.value();
            Object value = resolvePath(config, path);

            if (value == null)
                continue;

            try {
                field.setAccessible(true);
                Object converted = convertValue(value, field.getType());
                if (converted != null) {
                    field.set(target, converted);
                }
            } catch (Exception e) {
                log.warn("Failed to inject @CmsValue({}) into {}.{}: {}", path, clazz.getSimpleName(), field.getName(), e.getMessage());
            }
        }
    }

    private static Object resolvePath(Object root, String path) {
        String[] parts = path.split("\\.");
        Object current = root;

        for (String part : parts) {
            if (current == null)
                return null;
            current = getProperty(current, part);
        }

        return current;
    }

    private static Object getProperty(Object obj, String name) {
        // 1) fluent: method name = field name
        try {
            Method method = obj.getClass().getMethod(name);
            return method.invoke(obj);
        } catch (Exception ignored) {
        }
        // 2) Java Bean: getXxx
        try {
            String getter = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
            Method method = obj.getClass().getMethod(getter);
            return method.invoke(obj);
        } catch (Exception ignored) {
        }
        // 3) boolean: isXxx
        try {
            String booleanGetter = "is" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
            Method method = obj.getClass().getMethod(booleanGetter);
            return method.invoke(obj);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static Object convertValue(Object value, Class<?> targetType) {
        if (targetType.isInstance(value))
            return value;

        if (targetType == int.class || targetType == Integer.class) {
            if (value instanceof Number)
                return ((Number) value).intValue();
            return Integer.parseInt(value.toString());
        }
        if (targetType == long.class || targetType == Long.class) {
            if (value instanceof Number)
                return ((Number) value).longValue();
            return Long.parseLong(value.toString());
        }
        if (targetType == boolean.class || targetType == Boolean.class) {
            if (value instanceof Boolean)
                return value;
            return Boolean.parseBoolean(value.toString());
        }
        if (targetType == short.class || targetType == Short.class) {
            if (value instanceof Number)
                return ((Number) value).shortValue();
            return Short.parseShort(value.toString());
        }
        if (targetType == byte.class || targetType == Byte.class) {
            if (value instanceof Number)
                return ((Number) value).byteValue();
            return Byte.parseByte(value.toString());
        }
        if (targetType == float.class || targetType == Float.class) {
            if (value instanceof Number)
                return ((Number) value).floatValue();
            return Float.parseFloat(value.toString());
        }
        if (targetType == double.class || targetType == Double.class) {
            if (value instanceof Number)
                return ((Number) value).doubleValue();
            return Double.parseDouble(value.toString());
        }
        if (targetType == String.class) {
            return value.toString();
        }
        // Enum: first try standard Enum.valueOf() with uppercase, then from(String)
        if (targetType.isEnum()) {
            return convertToEnum(value.toString(), (Class<? extends Enum>) targetType);
        }

        return null;
    }

    private static <E extends Enum<E>> Object convertToEnum(String raw, Class<E> enumType) {
        // 1) static from(String) factory (project convention, e.g. SclConformanceMode.from)
        for (String factory : new String[]{"from", "of", "parse", "valueOf"}) {
            try {
                Method m = enumType.getMethod(factory, String.class);
                if (java.lang.reflect.Modifier.isStatic(m.getModifiers())) {
                    return m.invoke(null, raw);
                }
            } catch (Exception ignored) {
            }
        }
        // 2) uppercase Enum.valueOf (standard JDK fallback)
        try {
            return Enum.valueOf(enumType, raw.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}