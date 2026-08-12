package com.ysh.jcms.app.handler.rpc;

import com.ysh.jcms.core.data.choice.CmsDataDefinition;
import com.ysh.jcms.core.data.sequence.rpc.CmsRpcMethodEntry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory RPC interface and method registry.
 *
 * <p>
 * Stores definitions of available RPC interfaces and their methods. Server
 * implementations register their methods here at startup.
 */
public class RpcRegistry {

    private static final Map<String, InterfaceDef> interfaces = new ConcurrentHashMap<>();

    static {
        // Register built-in SystemInfo interface
        InterfaceDef sysInfo = new InterfaceDef("SystemInfo");
        sysInfo.addMethod("getServerVersion", 1, 5000, buildVersionDataDef(), buildVersionDataDef());
        interfaces.put("SystemInfo", sysInfo);
    }

    /** Get all interface names. */
    public static List<String> getInterfaceNames() {
        return new ArrayList<>(interfaces.keySet());
    }

    /** Get an interface definition by name. */
    public static InterfaceDef getInterface(String name) {
        return interfaces.get(name);
    }

    /** Get all methods for an interface. */
    public static List<String> getMethodNames(String iface) {
        InterfaceDef def = interfaces.get(iface);
        return def != null ? new ArrayList<>(def.methods.keySet()) : Collections.emptyList();
    }

    /** Get a specific method definition. */
    public static MethodDef getMethod(String iface, String method) {
        InterfaceDef def = interfaces.get(iface);
        return def != null ? def.methods.get(method) : null;
    }

    /** Get a method definition by full reference (interface.method). */
    public static MethodDef getMethodByRef(String fullRef) {
        int dot = fullRef.indexOf('.');
        if (dot < 0)
            return null;
        String iface = fullRef.substring(0, dot);
        String method = fullRef.substring(dot + 1);
        return getMethod(iface, method);
    }

    /** Build RpcMethodEntry for a given method in an interface. */
    public static CmsRpcMethodEntry buildMethodEntry(String iface, String method) {
        MethodDef def = getMethod(iface, method);
        if (def == null)
            return null;
        CmsRpcMethodEntry entry = new CmsRpcMethodEntry();
        entry.name(method);
        entry.version(def.version);
        entry.timeout(def.timeout);
        entry.request(def.requestDef);
        entry.response(def.responseDef);
        return entry;
    }

    // ---- Data structure helpers ----

    private static CmsDataDefinition buildVersionDataDef() {
        // VisibleString definition (choice=16) with max length 64
        CmsDataDefinition def = new CmsDataDefinition();
        def.choice(16); // visible-string
        def.alt_visible_string_len(64);
        return def;
    }

    // ---- Internal types ----

    public static class InterfaceDef {
        public final String name;
        public final Map<String, MethodDef> methods = new LinkedHashMap<>();

        public InterfaceDef(String name) {
            this.name = name;
        }

        public void addMethod(String name, int version, int timeout, CmsDataDefinition requestDef, CmsDataDefinition responseDef) {
            methods.put(name, new MethodDef(name, version, timeout, requestDef, responseDef));
        }
    }

    public static class MethodDef {
        public final String name;
        public final int version;
        public final int timeout;
        public final CmsDataDefinition requestDef;
        public final CmsDataDefinition responseDef;

        public MethodDef(String name, int version, int timeout, CmsDataDefinition requestDef, CmsDataDefinition responseDef) {
            this.name = name;
            this.version = version;
            this.timeout = timeout;
            this.requestDef = requestDef;
            this.responseDef = responseDef;
        }
    }
}
