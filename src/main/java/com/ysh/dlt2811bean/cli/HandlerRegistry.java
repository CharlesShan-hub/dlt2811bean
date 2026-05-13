package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;

public class HandlerRegistry {

    private static final Logger log = LoggerFactory.getLogger(HandlerRegistry.class);

    private static final String BASE_PACKAGE = "com.ysh.dlt2811bean.cli.handler";

    private static final String[] SUBPACKAGES = {
        "association", "command", "data", "dataset", "directory",
        "goose", "negotiation", "report", "setting", "sv", "test"
    };

    public static void autoRegister(CliContext ctx, Map<String, CommandHandler> handlers) {
        for (String subPkg : SUBPACKAGES) {
            scanPackage(ctx, BASE_PACKAGE + "." + subPkg, handlers);
        }
    }

    private static void scanPackage(CliContext ctx, String packageName, Map<String, CommandHandler> handlers) {
        String path = packageName.replace('.', '/');
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = cl.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if ("file".equals(resource.getProtocol())) {
                    scanDirectory(ctx, packageName, new File(resource.toURI()), handlers);
                } else if ("jar".equals(resource.getProtocol())) {
                    scanJar(ctx, packageName, resource, handlers);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to scan package {}: {}", packageName, e.getMessage());
        }
    }

    private static void scanDirectory(CliContext ctx, String packageName, File dir, Map<String, CommandHandler> handlers) {
        File[] files = dir.listFiles((d, name) -> name.endsWith(".class") && !name.contains("$"));
        if (files == null) return;
        for (File file : files) {
            String className = packageName + "." + file.getName().replace(".class", "");
            registerIfValid(ctx, className, handlers);
        }
    }

    private static void scanJar(CliContext ctx, String packageName, URL resource, Map<String, CommandHandler> handlers) throws IOException {
        JarURLConnection conn = (JarURLConnection) resource.openConnection();
        String prefix = packageName.replace('.', '/') + "/";
        Enumeration<JarEntry> entries = conn.getJarFile().entries();
        while (entries.hasMoreElements()) {
            String entryName = entries.nextElement().getName();
            if (entryName.startsWith(prefix) && entryName.endsWith(".class") && !entryName.contains("$")) {
                String className = entryName.replace('/', '.').replace(".class", "");
                registerIfValid(ctx, className, handlers);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void registerIfValid(CliContext ctx, String className, Map<String, CommandHandler> handlers) {
        try {
            Class<?> clazz = Class.forName(className);
            if (!CommandHandler.class.isAssignableFrom(clazz)) return;
            if (Modifier.isAbstract(clazz.getModifiers())) return;

            Class<CommandHandler> handlerClass = (Class<CommandHandler>) clazz;
            CommandHandler instance = handlerClass.getDeclaredConstructor(CliContext.class).newInstance(ctx);
            handlers.put(instance.getName(), instance);
            log.debug("Auto-registered handler: {}", instance.getName());
        } catch (NoSuchMethodException e) {
            // no CliContext constructor — skip (e.g. abstract base classes)
        } catch (Exception e) {
            log.debug("Skipping {}: {}", className, e.getMessage());
        }
    }
}
