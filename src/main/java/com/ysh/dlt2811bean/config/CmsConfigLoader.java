package com.ysh.dlt2811bean.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CmsConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(CmsConfigLoader.class);
    private static final String[] SEARCH_PATHS = {
        "application.yaml",
        "config/application.yaml",
        "conf/application.yaml",
    };

    private static CmsConfig loadedConfig;

    public static synchronized CmsConfig load() {
        if (loadedConfig != null) {
            return loadedConfig;
        }

        CmsConfig config = new CmsConfig();
        CmsConfig fileConfig = loadFromFileSystem();

        if (fileConfig == null) {
            fileConfig = loadFromClasspath();
        }

        if (fileConfig != null) {
            config.merge(fileConfig);
        } else {
            log.info("No application.yaml found, using default config");
        }

        loadedConfig = config;
        return config;
    }

    public static synchronized void reload() {
        loadedConfig = null;
        load();
    }

    private static CmsConfig loadFromFileSystem() {
        for (String path : SEARCH_PATHS) {
            Path p = Paths.get(path);
            if (Files.exists(p)) {
                try (InputStream in = new FileInputStream(p.toFile())) {
                    CmsConfig config = parseYaml(in);
                    if (config != null) {
                        log.info("Loaded config from {}", p.toAbsolutePath());
                        return config;
                    }
                } catch (Exception e) {
                    log.warn("Failed to load config from {}: {}", p.toAbsolutePath(), e.getMessage());
                }
            }
        }
        return null;
    }

    private static CmsConfig loadFromClasspath() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = CmsConfigLoader.class.getClassLoader();
        }
        try (InputStream in = cl.getResourceAsStream("application.yaml")) {
            if (in != null) {
                CmsConfig config = parseYaml(in);
                if (config != null) {
                    log.info("Loaded config from classpath: application.yaml");
                    return config;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load config from classpath: {}", e.getMessage());
        }
        return null;
    }

    private static CmsConfig parseYaml(InputStream in) {
        LoaderOptions options = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(CmsConfig.class, options));
        return yaml.loadAs(in, CmsConfig.class);
    }
}
