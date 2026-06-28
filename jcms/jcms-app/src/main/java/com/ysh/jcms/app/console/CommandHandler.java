package com.ysh.jcms.app.console;

import java.util.List;
import java.util.Map;

public interface CommandHandler {

    String name();

    String description();

    List<Param> params();

    void execute(CmsConsole console, Map<String, String> args) throws Exception;
}
