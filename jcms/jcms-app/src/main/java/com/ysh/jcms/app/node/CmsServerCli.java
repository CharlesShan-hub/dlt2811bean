package com.ysh.jcms.app.node;

import com.ysh.jcms.app.handler.connection.abort.AbortServer;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.handler.connection.release.ReleaseServer;
import com.ysh.jcms.app.handler.test.test.TestServer;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirServer;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirServer;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirServer;
import com.ysh.jcms.utils.config.CmsConfigLoader;

/**
 * CMS Server CLI entry point.
 *
 * <p>Starts a CMS server on the specified port (default 18780),
 * registers all server handlers, and waits for Enter to stop.
 *
 * <p>Usage: CmsServerCli [port]
 */
public class CmsServerCli {

    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 18780;

        CmsNode node = new CmsNode(port);

        node.registerServer(new AssociateServer());
        node.registerServer(new ReleaseServer());
        node.registerServer(new AbortServer());
        node.registerServer(new TestServer());
        node.registerServer(new SvrDirServer());
        node.registerServer(new LdDirServer());
        node.registerServer(new LnDirServer());

        node.start();

        System.out.println("CMS Server running on port " + port);
        System.out.println("SCL loaded: " + node.getSclManager().isLoaded());
        if (node.getSclManager().isLoaded()) {
            System.out.println("SCL file: " + CmsConfigLoader.load().getServer().getResolvedTestSclFile());
        }
        System.out.println("Press Enter to stop...");
        System.in.read();

        node.stop();
        System.out.println("Server stopped.");
    }
}
