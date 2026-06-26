package com.ysh.jcms.app.cli.handler;

import com.ysh.jcms.app.cli.CliContext;
import com.ysh.jcms.app.cli.CliPrinter;
import com.ysh.jcms.app.cli.CommandHandler;
import com.ysh.jcms.app.cli.Param;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClientDao;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateClient;
import com.ysh.jcms.app.handler.test.test.TestClient;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirClient;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirClient;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirClient;
import com.ysh.jcms.app.handler.connection.release.ReleaseClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConnectHandler implements CommandHandler {

    @Override
    public String name() { return "connect"; }

    @Override
    public String description() { return "连接到 CMS 服务器并建立关联"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("host", "服务器地址", "127.0.0.1"),
            new Param("port", "端口号", "18780"),
            new Param("sapRef", "ServerAccessPoint 引用", "E1Q1SB1/S1")
        );
    }

    @Override
    public void execute(CliContext ctx, Map<String, String> args) throws Exception {
        if (ctx.isConnected()) {
            CliPrinter.error("Already connected. Type 'disconnect' first.");
            return;
        }

        String host = args.get("host");
        int port = Integer.parseInt(args.get("port"));
        String sapRef = args.get("sapRef");

        CliPrinter.info("Connecting to " + host + ":" + port + " ...");

        CmsNode node = new CmsNode(0);
        // Register all client handlers
        node.registerClient(new NegotiateClient(node));
        node.registerClient(new AssociateClient(node));
        node.registerClient(new ReleaseClient(node));
        node.registerClient(new TestClient(node));
        node.registerClient(new SvrDirClient(node));
        node.registerClient(new LnDirClient(node));
        node.registerClient(new LdDirClient(node));

        node.connect(host, port);
        CliPrinter.info("Connected, associating with " + sapRef + " ...");

        node.getClient(AssociateClient.class)
            .execute(new AssociateClientDao()
                .sapRef(sapRef).secure(false));

        ctx.node(node);
        CliPrinter.success("Associated: " + sapRef);
    }
}
