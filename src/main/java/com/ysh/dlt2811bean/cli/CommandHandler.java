package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public interface CommandHandler {
    String getName();
    String getDescription();
    List<Param> getParams();
    void execute(CmsClient client, Map<String, String> values) throws Exception;
}