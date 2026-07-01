package com.ysh.jcms.app.console;

import com.ysh.jcms.app.handler.console.client.*;
import com.ysh.jcms.app.handler.console.TracePduHandler;
import com.ysh.jcms.app.handler.console.ClearHandler;
import com.ysh.jcms.app.handler.data.getDataValues.GetDataValuesClient;
import com.ysh.jcms.app.handler.data.getDataValues.GetDataValuesConsole;
import com.ysh.jcms.app.handler.data.getDataDirectory.GetDataDirectoryClient;
import com.ysh.jcms.app.handler.data.getDataDirectory.GetDataDirectoryConsole;
import com.ysh.jcms.app.handler.data.getDataDefinition.GetDataDefinitionClient;
import com.ysh.jcms.app.handler.data.getDataDefinition.GetDataDefinitionConsole;
import com.ysh.jcms.app.handler.dataset.getDataSetValues.GetDataSetValuesClient;
import com.ysh.jcms.app.handler.dataset.getDataSetValues.GetDataSetValuesConsole;
import com.ysh.jcms.app.handler.dataset.setDataSetValues.SetDataSetValuesClient;
import com.ysh.jcms.app.handler.dataset.setDataSetValues.SetDataSetValuesConsole;
import com.ysh.jcms.app.handler.dataset.getDataSetDirectory.GetDataSetDirectoryClient;
import com.ysh.jcms.app.handler.dataset.getDataSetDirectory.GetDataSetDirectoryConsole;
import com.ysh.jcms.app.handler.dataset.createDataSet.CreateDataSetClient;
import com.ysh.jcms.app.handler.dataset.createDataSet.CreateDataSetConsole;
import com.ysh.jcms.app.handler.dataset.deleteDataSet.DeleteDataSetClient;
import com.ysh.jcms.app.handler.dataset.deleteDataSet.DeleteDataSetConsole;
import com.ysh.jcms.app.handler.sg.getSgcbValues.GetSgcbValuesClient;
import com.ysh.jcms.app.handler.sg.getSgcbValues.GetSgcbValuesConsole;
import com.ysh.jcms.app.handler.sg.selectActiveSg.SelectActiveSgClient;
import com.ysh.jcms.app.handler.sg.selectActiveSg.SelectActiveSgConsole;
import com.ysh.jcms.app.handler.sg.selectEditSg.SelectEditSgClient;
import com.ysh.jcms.app.handler.sg.selectEditSg.SelectEditSgConsole;
import com.ysh.jcms.app.handler.sg.getEditSgValue.GetEditSgValueClient;
import com.ysh.jcms.app.handler.sg.getEditSgValue.GetEditSgValueConsole;
import com.ysh.jcms.app.handler.sg.setEditSgValue.SetEditSgValueClient;
import com.ysh.jcms.app.handler.sg.setEditSgValue.SetEditSgValueConsole;
import com.ysh.jcms.app.handler.sg.confirmEditSgValues.ConfirmEditSgValuesClient;
import com.ysh.jcms.app.handler.sg.confirmEditSgValues.ConfirmEditSgValuesConsole;
import com.ysh.jcms.app.console.api.CliApiServer;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.app.handler.data.setDataValues.SetDataValuesClient;
import com.ysh.jcms.app.handler.data.setDataValues.SetDataValuesConsole;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirClient;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirConsole;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirClient;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirConsole;
import com.ysh.jcms.app.handler.directory.getAllCbValues.AllCbValuesClient;
import com.ysh.jcms.app.handler.directory.getAllCbValues.AllCbValuesConsole;
import com.ysh.jcms.app.handler.directory.getAllDataDefinition.AllDataDefClient;
import com.ysh.jcms.app.handler.directory.getAllDataDefinition.AllDataDefConsole;
import com.ysh.jcms.app.handler.directory.getAllDataValues.AllDataValuesClient;
import com.ysh.jcms.app.handler.directory.getAllDataValues.AllDataValuesConsole;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirClient;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirConsole;
import com.ysh.jcms.app.handler.connection.release.ReleaseClient;
import com.ysh.jcms.app.handler.connection.release.ReleaseConsole;
import com.ysh.jcms.app.handler.connection.abort.AbortClient;
import com.ysh.jcms.app.handler.connection.abort.AbortConsole;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateConsole;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateClient;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateConsole;
import com.ysh.jcms.app.handler.test.test.TestClient;
import com.ysh.jcms.app.handler.test.test.TestConsole;

/**
 * Client-side CMS console.
 *
 * <p>Connects to a remote CMS server and issues commands interactively.
 */
public class CmsClientConsole extends CmsConsole {

    private volatile CliApiServer apiServer;

    public CmsClientConsole() {
        super(false);
        registerClients();
    }

    @Override
    protected void onStart() {
        // Start embedded API server (for remote execution via cms.ps1)
        String apiEnabled = System.getProperty("cms.api.enabled", "true");
        if (!"false".equalsIgnoreCase(apiEnabled)) {
            int apiPort = Integer.parseInt(System.getProperty("cms.api.port",
                String.valueOf(CmsConfigLoader.load().getClient().getConsole().getApiPort())));
            try {
                apiServer = new CliApiServer(apiPort, this);
                apiServer.start();
            } catch (Exception e) {
                ConsolePrinter.gray("ApiServer not started (port " + apiPort + "): " + e.getMessage());
            }
        }
    }

    @Override
    protected void onStop() {
        if (apiServer != null) {
            apiServer.stop();
        }
    }

    private void registerClients() {
        registerClient(new NegotiateClient(this));
        registerClient(new AssociateClient(this));
        registerClient(new ReleaseClient(this));
        registerClient(new AbortClient(this));
        registerClient(new TestClient(this));
        registerClient(new SvrDirClient(this));
        registerClient(new LnDirClient(this));
        registerClient(new LdDirClient(this));
        registerClient(new AllDataValuesClient(this));
        registerClient(new AllDataDefClient(this));
        registerClient(new AllCbValuesClient(this));
        registerClient(new GetDataValuesClient(this));
        registerClient(new SetDataValuesClient(this));
        registerClient(new GetDataDirectoryClient(this));
        registerClient(new GetDataDefinitionClient(this));
        registerClient(new GetDataSetValuesClient(this));
        registerClient(new GetDataSetDirectoryClient(this));
        registerClient(new SetDataSetValuesClient(this));
        registerClient(new CreateDataSetClient(this));
        registerClient(new DeleteDataSetClient(this));
        registerClient(new GetSgcbValuesClient(this));
        registerClient(new SelectActiveSgClient(this));
        registerClient(new SelectEditSgClient(this));
        registerClient(new GetEditSgValueClient(this));
        registerClient(new SetEditSgValueClient(this));
        registerClient(new ConfirmEditSgValuesClient(this));
    }

    @Override
    protected void registerHandlers() {
        register(new HelpHandler(this));
        register(new ConnectHandler());
        register(new DisconnectHandler());
        register(new SvrDirConsole());
        register(new LdDirConsole());
        register(new LnDirConsole());
        register(new AllDataValuesConsole());
        register(new AllDataDefConsole());
        register(new AllCbValuesConsole());
        register(new GetDataValuesConsole());
        register(new GetDataDirectoryConsole());
        register(new GetDataDefinitionConsole());
        register(new GetDataSetValuesConsole());
        register(new GetDataSetDirectoryConsole());
        register(new SetDataSetValuesConsole());
        register(new CreateDataSetConsole());
        register(new DeleteDataSetConsole());
        register(new GetSgcbValuesConsole());
        register(new SelectActiveSgConsole());
        register(new SelectEditSgConsole());
        register(new GetEditSgValueConsole());
        register(new SetEditSgValueConsole());
        register(new ConfirmEditSgValuesConsole());
        register(new SetDataValuesConsole());
        register(new TracePduHandler());
        register(new ClearHandler());
        register(new ReleaseConsole());
        register(new AssociateConsole());
        register(new AbortConsole());
        register(new NegotiateConsole());
        register(new TestConsole());
    }

    public static void main(String[] args) {
        new CmsClientConsole().run();
    }
}
