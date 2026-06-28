package com.ysh.jcms.app.console;

import com.ysh.jcms.app.node.CmsNode;

public class ConsoleContext {

    private CmsNode node;

    public CmsNode node() { return node; }
    public void node(CmsNode node) { this.node = node; }

    public boolean isConnected() {
        return node != null && node.isClientConnected()
            && node.getClient().getSession() != null
            && node.getClient().getSession().getState() == com.ysh.jcms.utils.transport.session.SessionState.ASSOCIATED;
    }
}
