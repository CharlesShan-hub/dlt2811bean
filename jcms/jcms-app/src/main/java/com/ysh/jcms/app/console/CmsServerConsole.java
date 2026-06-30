package com.ysh.jcms.app.console;

import com.ysh.jcms.app.handler.data.getDataValues.GetDataValuesServer;
import com.ysh.jcms.app.handler.data.getDataDirectory.GetDataDirectoryServer;
import com.ysh.jcms.app.handler.data.getDataDefinition.GetDataDefinitionServer;
import com.ysh.jcms.app.handler.dataset.getDataSetValues.GetDataSetValuesServer;
import com.ysh.jcms.app.handler.dataset.setDataSetValues.SetDataSetValuesServer;
import com.ysh.jcms.app.handler.dataset.getDataSetDirectory.GetDataSetDirectoryServer;
import com.ysh.jcms.app.handler.dataset.createDataSet.CreateDataSetServer;
import com.ysh.jcms.app.handler.dataset.deleteDataSet.DeleteDataSetServer;
import com.ysh.jcms.app.handler.data.setDataValues.SetDataValuesServer;
import com.ysh.jcms.app.handler.sg.getSgcbValues.GetSgcbValuesServer;
import com.ysh.jcms.app.handler.sg.selectActiveSg.SelectActiveSgServer;
import com.ysh.jcms.app.handler.sg.selectEditSg.SelectEditSgServer;
import com.ysh.jcms.app.handler.connection.abort.AbortServer;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.handler.connection.release.ReleaseServer;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateServer;
import com.ysh.jcms.app.handler.test.test.TestServer;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirServer;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirServer;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirServer;
import com.ysh.jcms.app.handler.directory.getAllCbValues.AllCbValuesServer;
import com.ysh.jcms.app.handler.directory.getAllDataDefinition.AllDataDefServer;
import com.ysh.jcms.app.handler.directory.getAllDataValues.AllDataValuesServer;
import com.ysh.jcms.app.handler.console.server.ListHandler;
import com.ysh.jcms.app.handler.console.TracePduHandler;

/**
 * Server-side CMS console.
 *
 * <p>Starts the CMS server, registers all server handlers,
 * and waits for keyboard input to shut down.
 */
public class CmsServerConsole extends CmsConsole {

    public CmsServerConsole() {
        super(true);
    }

    @Override
    protected void registerHandlers() {
        registerServer(new AssociateServer());
        registerServer(new ReleaseServer());
        registerServer(new AbortServer());
        registerServer(new NegotiateServer());
        registerServer(new TestServer());
        registerServer(new SvrDirServer());
        registerServer(new LdDirServer());
        registerServer(new LnDirServer());
        registerServer(new AllDataValuesServer());
        registerServer(new AllDataDefServer());
        registerServer(new AllCbValuesServer());
        registerServer(new GetDataValuesServer());
        registerServer(new SetDataValuesServer());
        registerServer(new GetDataDirectoryServer());
        registerServer(new GetDataDefinitionServer());
        registerServer(new GetDataSetValuesServer());
        registerServer(new GetDataSetDirectoryServer());
        registerServer(new SetDataSetValuesServer());
        registerServer(new CreateDataSetServer());
        registerServer(new DeleteDataSetServer());
        registerServer(new GetSgcbValuesServer());
        registerServer(new SelectActiveSgServer());
        registerServer(new SelectEditSgServer());
        register(new ListHandler());
        register(new TracePduHandler());
    }

    @Override
    protected void onStart() {
        try {
            start(false);
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            return;
        }
        System.out.println("CMS Server running on port " + getServer().getPort());
        if (getServer().hasTls()) {
            System.out.println("TLS port: " + getServer().getSslPort());
        }
        System.out.println("SCL loaded: " + getSclManager().isLoaded());
        if (getSclManager().isLoaded()) {
            System.out.println("SCL file: " + getSclManager().getSource());
        }
        System.out.println("Type 'exit' to stop...");
    }

    @Override
    protected void onStop() {
        stop();
        System.out.println("Server stopped.");
    }

    public static void main(String[] args) {
        new CmsServerConsole().run();
    }
}
