package com.ysh.dlt2811bean.cli.handler.common;

import com.ysh.dlt2811bean.cli.handler.CliContext;

public abstract class AbstractSystemHandler implements CommandHandler{
    protected final CliContext ctx;

    protected AbstractSystemHandler(CliContext ctx) {
        this.ctx = ctx;
    }
}
