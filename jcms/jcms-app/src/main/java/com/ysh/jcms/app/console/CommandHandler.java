package com.ysh.jcms.app.console;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.app.handler.CmsContent;
import com.ysh.jcms.app.console.Param.ParamType;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
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
 * Two convenience methods are provided for the common {@link #execute} patterns:
 * <ul>
 *   <li>{@link #executeAction} — for one-way commands (no response data)</li>
 *   <li>{@link #executeQuery} — for data-query commands (outputs JSON result)</li>
 * </ul>
 *
 * <pre>
 * // Action command (no response data)
 * public class AbortConsole extends CommandHandler&lt;AbortClientDao, AbortClient&gt; {
 *     public AbortConsole() { super(CommandInfo.ABORT); }
 *     &#64;Override public void execute(CmsConsole c, Map&lt;String, String&gt; a) throws Exception {
 *         executeAction(c, a);
 *     }
 *     &#64;Override public List&lt;Param&gt; params() {
 *         return List.of(new Param("reason", "中止原因码", "0", ParamType.INT));
 *     }
 * }
 *
 * // Query command (returns JSON data)
 * public class SvrDirConsole extends CommandHandler&lt;SvrDirDao, SvrDirClient&gt; {
 *     public SvrDirConsole() { super(CommandInfo.SERVER_DIR); }
 *     &#64;Override public void execute(CmsConsole c, Map&lt;String, String&gt; a) throws Exception {
 *         executeQuery(c, a);
 *     }
 *     &#64;Override public List&lt;Param&gt; params() {
 *         return List.of(
 *             new Param("after", "起始引用...", "", "referenceAfter"),
 *             new Param("auto-pull", "自动续拉分页", "false"));
 *     }
 * }
 * </pre>
 */
public abstract class CommandHandler<D extends BaseDao, C extends BaseClientHandler<D>> {

    private final CommandInfo info;
    private final Class<C> clientClass;
    private final Class<D> daoClass;
    private final boolean isQuery;

    @SuppressWarnings("unchecked")
    protected CommandHandler(CommandInfo info) {
        this(info, true);
    }

    @SuppressWarnings("unchecked")
    protected CommandHandler(CommandInfo info, boolean isQuery) {
        this.info = info;
        this.isQuery = isQuery;
        Type superClass = getClass().getGenericSuperclass();
        ParameterizedType type = (ParameterizedType) superClass;
        this.daoClass = (Class<D>) type.getActualTypeArguments()[0];
        this.clientClass = (Class<C>) type.getActualTypeArguments()[1];
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

    /** Parameter definitions for command-line parsing. */
    public List<Param> params() {
        return Arrays.asList();
    }

    /** Execute the command with parsed arguments. */
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (isQuery) {
            executeQuery(console, args);
        } else {
            executeAction(console, args);
        }
    }

    // ── Template execute helpers ──

    /** Execute a one-way action command (no response data). */
    protected final void executeAction(CmsConsole console, Map<String, String> args) throws Exception {
        D dao = daoClass.getDeclaredConstructor().newInstance();
        bindParams(dao, args);
        console.getClient(clientClass).execute(dao);
        ConsolePrinter.success(info().name() + " sent");
    }

    /** Execute a data-query command (outputs JSON result). */
    protected final void executeQuery(CmsConsole console, Map<String, String> args) throws Exception {
        D dao = daoClass.getDeclaredConstructor().newInstance();
        bindParams(dao, args);
        CmsContent<D> c = new CmsContent<>(dao, args.get("auto-pull"));
        console.getClient(clientClass).executeResult(c);
        ConsolePrinter.outputJson(c.res());
    }

    // ── Param binding ──

    private void bindParams(D dao, Map<String, String> args) throws Exception {
        List<Param> params = params();
        if (params == null || params.isEmpty()) return;
        for (Param p : params) {
            String setter = p.setter();
            if (setter == null) continue;
            String value = args.get(p.name());
            if (value == null) continue;
            Method m = dao.getClass().getMethod(setter, p.type().javaType());
            m.invoke(dao, convert(value, p.type()));
        }
    }

    private static Object convert(String value, ParamType type) {
        switch (type) {
            case STRING: return value;
            case INT:
            case INTEGER: return Integer.parseInt(value);
            case LONG: return Long.parseLong(value);
            case BOOLEAN: return Boolean.parseBoolean(value);
            case BYTE: return Byte.parseByte(value);
            case SHORT: return Short.parseShort(value);
            case FLOAT: return Float.parseFloat(value);
            case DOUBLE: return Double.parseDouble(value);
            default: throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}