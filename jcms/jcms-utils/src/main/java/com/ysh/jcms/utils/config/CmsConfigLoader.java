package com.ysh.jcms.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CmsConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(CmsConfigLoader.class);
    private static final String PROP_PREFIX = "cms.";
    private static final String[] SEARCH_PATHS = {"application.yaml", "config/application.yaml", "conf/application.yaml",};

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

        applySystemProperties(config);

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
        Constructor constructor = new Constructor(CmsConfig.class, options);
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setBeanAccess(BeanAccess.FIELD);
        constructor.setPropertyUtils(propertyUtils);
        Yaml yaml = new Yaml(constructor);
        CmsConfig config = yaml.loadAs(in, CmsConfig.class);
        // 验证 YAML 加载结果
        if (config != null && config.server() != null) {
            log.info("[parseYaml] sclFiles={}, testSclFiles={}", config.server().sclFiles(), config.server().testSclFiles());
        }
        return config;
    }

    private static void applySystemProperties(CmsConfig config) {
        String port = System.getProperty(PROP_PREFIX + "server.port");
        if (port != null && !port.isEmpty()) {
            try {
                config.server().port(Integer.parseInt(port));
                log.info("Override server.port={} from system property", port);
            } catch (NumberFormatException e) {
                log.warn("Invalid system property cms.server.port: {}", port);
            }
        }
        String sclFile = System.getProperty(PROP_PREFIX + "server.testSclFile");
        if (sclFile != null && !sclFile.isEmpty()) {
            config.server().testSclFile(sclFile);
            log.info("Override server.testSclFile={} from system property", sclFile);
        }
    }
}
