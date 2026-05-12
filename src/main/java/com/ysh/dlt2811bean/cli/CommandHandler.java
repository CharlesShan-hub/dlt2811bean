package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public interface CommandHandler {
    String getName();
    String getDescription();
    List<Param> getParams();
    default CmsConfig config() { return CmsConfigLoader.load(); }
    default List<Param> updateConfigAndGetParams() {
        return getParams();
    }
    void execute(CmsClient client, Map<String, String> values) throws Exception;
}