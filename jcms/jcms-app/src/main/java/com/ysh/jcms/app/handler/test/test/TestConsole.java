package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;

public class TestConsole extends CommandHandler<TestDao, TestClient> {

    public TestConsole() {
        super(CommandInfo.TEST, false);
    }
}