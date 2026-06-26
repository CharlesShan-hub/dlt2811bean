package com.ysh.jcms.app.cli;

import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.app.node.ContentManager;

/**
 * Holds the current CLI session state.
 * The {@link ContentManager} is accessed via {@code node.getContentManager()}
 * when a connection is active.
 */
public class CliContext {

    private CmsNode node;

    public CmsNode node() { return node; }

    public void node(CmsNode node) { this.node = node; }

    public boolean isConnected() {
        return node != null && node.isClientConnected();
    }
}
