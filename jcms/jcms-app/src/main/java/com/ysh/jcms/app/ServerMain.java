package com.ysh.jcms.app;

import com.ysh.jcms.app.handler.connection.abort.AbortServer;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.handler.connection.release.ReleaseServer;
import com.ysh.jcms.app.handler.test.test.TestServer;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirServer;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirServer;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirServer;
import com.ysh.jcms.app.node.CmsNode;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 18780;

        CmsNode node = new CmsNode(port);

        // Register all server handlers
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
            System.out.println("SCL file: " + com.ysh.jcms.utils.config.CmsConfigLoader.load().getServer().getResolvedSclFile());
        }
        System.out.println("Press Enter to stop...");
        System.in.read();

        node.stop();
        System.out.println("Server stopped.");
    }
}
