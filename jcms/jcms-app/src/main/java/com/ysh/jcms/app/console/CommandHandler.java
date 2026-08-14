package com.ysh.jcms.app.console;

import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.handler.support.CmsContent;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base class for CLI command handlers.
 *
 * <p>
 * Subclasses pass a {@link CommandInfo} constant to the constructor, which
 * provides the name, description, and precondition requirement automatically.
 * Generic type parameters {@code <D extends BaseDao>} and
 * {@code <C extends BaseClientHandler<D>>} are inferred via reflection, so
 * subclasses only need to declare them in the class signature.
 *
 * <p>
 * Two convenience methods are provided for the common {@link #execute}
 * patterns:
 * <ul>
 * <li>{@link #executeAction} — for one-way commands (no response data)</li>
 * <li>{@link #executeQuery} — for data-query commands (outputs JSON
 * result)</li>
 * </ul>
 *
 * <pre>
 * // Action command (no response data)
 * public class AbortConsole extends CommandHandler&lt;AbortDao, AbortClient&gt; {
 *     public AbortConsole() {
 *         super(CommandInfo.ABORT, false);
 *         param(Param.of("reason", "0", "reason", Integer.class, false), "中止原因码");
 *     }
 * }
 *
 * // Query command (returns JSON data)
 * public class SvrDirConsole extends CommandHandler&lt;SvrDirDao, SvrDirClient&gt; {
 *     public SvrDirConsole() {
 *         super(CommandInfo.SERVER_DIR);
 *         param("after", "起始引用...", "", "referenceAfter");
 *         param("auto-pull", "自动续拉分页", "false");
 *     }
 * }
 * </pre>
 */
public abstract class CommandHandler<D extends BaseDao, C extends BaseClientHandler<D>> {

    private final CommandInfo info;
    private final Class<C> clientClass;
    private final Class<D> daoClass;
    private final boolean isQuery;
    private final List<Param> params = new ArrayList<>();

    @SuppressWarnings("unchecked")
    protected CommandHandler(CommandInfo info) {
        this(info, true);
    }

    @SuppressWarnings("unchecked")
    protected CommandHandler(CommandInfo info, boolean isQuery) {
        this.info = info;
        this.isQuery = isQuery;
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) superClass;
            this.daoClass = resolveRawClass(type.getActualTypeArguments()[0]);
            this.clientClass = resolveRawClass(type.getActualTypeArguments()[1]);
        } else {
            // Raw type: local-only handler without a DAO/Client (e.g. help, connect).
            this.daoClass = null;
            this.clientClass = null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> resolveRawClass(Type type) {
        if (type instanceof Class) {
            return (Class<T>) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getRawType();
        }
        return null;
    }

    // ── Precondition ──

    public final CommandInfo.Requirement requirement() {
        return info.requirement();
    }

    // ── Metadata ──

    public final String name() {
        return info.commandName();
    }

    public final String description() {
        return info.description();
    }

    public final CommandInfo info() {
        return info;
    }

    // ── Subclass hooks ──

    /**
     * Parameter definitions for command-line parsing. Declared via
     * {@link #param(String, String)} calls in the subclass constructor; this method
     * is final and should not be overridden.
     */
    public final List<Param> params() {
        return params;
    }

    // ── Param declaration (call in subclass constructor, one per line) ──

    protected final void param(Param p, String n) {
        params.add(p.desp(n));
    }

    /** Execute the command with parsed arguments. */
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!validateRequired(args))
            return;
        // Apply custom delimiter to all List.class params
        String delim = args.get("delimiter");
        if (delim != null) {
            for (Param p : params()) {
                if (p.type() == List.class) {
                    p.delimiter(delim);
                }
            }
        }
        if (isQuery) {
            executeQuery(console, args);
        } else {
            executeAction(console, args);
        }
    }

    // ── Template execute helpers ──

    /** Execute a one-way action command (no response data). */
    protected final void executeAction(CmsConsole console, Map<String, String> args) throws Exception {
        requireDaoClient();
        D dao = daoClass.getDeclaredConstructor().newInstance();
        bindParams(dao, args);
        console.getClient(clientClass).execute(dao);
        CmsPrinter.success(info().name() + " sent");
    }

    /** Execute a data-query command (outputs JSON result). */
    protected final void executeQuery(CmsConsole console, Map<String, String> args) throws Exception {
        requireDaoClient();
        D dao = daoClass.getDeclaredConstructor().newInstance();
        bindParams(dao, args);
        CmsContent<D> c = new CmsContent<>(dao, args.get("auto-pull"));
        console.getClient(clientClass).executeResult(c);
        CmsPrinter.outputJson(c.res());
    }

    private void requireDaoClient() {
        if (daoClass == null || clientClass == null) {
            throw new IllegalStateException(name() + ": raw CommandHandler cannot execute; extend CommandHandler<Dao, Client> instead");
        }
    }

    // ── Param binding ──

    private boolean validateRequired(Map<String, String> args) {
        for (Param p : params()) {
            if (p.required()) {
                String value = args.get(p.cliName());
                if (value == null || value.trim().isEmpty()) {
                    CmsPrinter.error("Missing required parameter: --" + p.cliName() + " (" + p.desp() + ")");
                    return false;
                }
            }
        }
        return true;
    }

    private void bindParams(D dao, Map<String, String> args) throws Exception {
        List<Param> params = params();
        if (params == null || params.isEmpty())
            return;
        for (Param p : params) {
            String daoName = p.daoName();
            if (daoName == null)
                continue;
            String value = args.get(p.cliName());
            if (value == null)
                continue;
            Method m = findMethod(dao.getClass(), daoName, p.type());
            m.invoke(dao, p.convert(value));
        }
    }

    /**
     * Find a method matching the given name and type, trying both primitive and
     * wrapper equivalents.
     */
    private static Method findMethod(Class<?> clazz, String name, Class<?> type) throws NoSuchMethodException {
        try {
            return clazz.getMethod(name, type);
        } catch (NoSuchMethodException e) {
            // Try primitive/wrapper counterpart
            Class<?> alt = primitiveToWrapper(type);
            if (alt != null) {
                try {
                    return clazz.getMethod(name, alt);
                } catch (NoSuchMethodException ignored) {
                }
            }
            throw e;
        }
    }

    private static Class<?> primitiveToWrapper(Class<?> type) {
        if (type == Boolean.class)
            return boolean.class;
        if (type == boolean.class)
            return Boolean.class;
        if (type == Integer.class)
            return int.class;
        if (type == int.class)
            return Integer.class;
        if (type == Long.class)
            return long.class;
        if (type == long.class)
            return Long.class;
        if (type == Byte.class)
            return byte.class;
        if (type == byte.class)
            return Byte.class;
        if (type == Short.class)
            return short.class;
        if (type == short.class)
            return Short.class;
        if (type == Float.class)
            return float.class;
        if (type == float.class)
            return Float.class;
        if (type == Double.class)
            return double.class;
        if (type == double.class)
            return Double.class;
        return null;
    }
}
