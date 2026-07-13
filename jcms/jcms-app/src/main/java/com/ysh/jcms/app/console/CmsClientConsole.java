package com.ysh.jcms.app.console;

import com.ysh.jcms.app.handler.console.client.*;
import com.ysh.jcms.app.handler.console.TracePduHandler;
import com.ysh.jcms.app.handler.console.ClearHandler;
import com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory.GetRpcInterfaceDirectoryClient;
import com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory.GetRpcInterfaceDirectoryConsole;
import com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory.GetRpcMethodDirectoryClient;
import com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory.GetRpcMethodDirectoryConsole;
import com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition.GetRpcInterfaceDefinitionClient;
import com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition.GetRpcInterfaceDefinitionConsole;
import com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition.GetRpcMethodDefinitionClient;
import com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition.GetRpcMethodDefinitionConsole;
import com.ysh.jcms.app.handler.rpc.rpcCall.RpcCallClient;
import com.ysh.jcms.app.handler.rpc.rpcCall.RpcCallConsole;
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
import com.ysh.jcms.app.handler.report.getBrcbValues.GetBrcbValuesClient;
import com.ysh.jcms.app.handler.report.getBrcbValues.GetBrcbValuesConsole;
import com.ysh.jcms.app.handler.report.setBrcbValues.SetBrcbValuesClient;
import com.ysh.jcms.app.handler.report.setBrcbValues.SetBrcbValuesConsole;
import com.ysh.jcms.app.handler.report.getUrcbValues.GetUrcbValuesClient;
import com.ysh.jcms.app.handler.report.getUrcbValues.GetUrcbValuesConsole;
import com.ysh.jcms.app.handler.report.setUrcbValues.SetUrcbValuesClient;
import com.ysh.jcms.app.handler.report.setUrcbValues.SetUrcbValuesConsole;
import com.ysh.jcms.app.handler.report.report.ReportClient;
import com.ysh.jcms.app.handler.log.getLcbValues.GetLcbValuesClient;
import com.ysh.jcms.app.handler.log.getLcbValues.GetLcbValuesConsole;
import com.ysh.jcms.app.handler.log.setLcbValues.SetLcbValuesClient;
import com.ysh.jcms.app.handler.log.setLcbValues.SetLcbValuesConsole;
import com.ysh.jcms.app.handler.log.queryLogByTime.QueryLogByTimeClient;
import com.ysh.jcms.app.handler.log.queryLogByTime.QueryLogByTimeConsole;
import com.ysh.jcms.app.handler.log.queryLogAfter.QueryLogAfterClient;
import com.ysh.jcms.app.handler.log.queryLogAfter.QueryLogAfterConsole;
import com.ysh.jcms.app.handler.log.getLogStatusValues.GetLogStatusValuesClient;
import com.ysh.jcms.app.handler.log.getLogStatusValues.GetLogStatusValuesConsole;
import com.ysh.jcms.app.handler.file.deleteFile.DeleteFileClient;
import com.ysh.jcms.app.handler.file.deleteFile.DeleteFileConsole;
import com.ysh.jcms.app.handler.file.getFile.GetFileClient;
import com.ysh.jcms.app.handler.file.getFile.GetFileConsole;
import com.ysh.jcms.app.handler.file.getFileAttributeValues.GetFileAttributeValuesClient;
import com.ysh.jcms.app.handler.file.getFileAttributeValues.GetFileAttributeValuesConsole;
import com.ysh.jcms.app.handler.file.getFileDirectory.GetFileDirectoryClient;
import com.ysh.jcms.app.handler.file.getFileDirectory.GetFileDirectoryConsole;
import com.ysh.jcms.app.handler.file.setFile.SetFileClient;
import com.ysh.jcms.app.handler.file.setFile.SetFileConsole;
import com.ysh.jcms.app.handler.goose.getGoCbValues.GetGoCbValuesClient;
import com.ysh.jcms.app.handler.goose.getGoCbValues.GetGoCbValuesConsole;
import com.ysh.jcms.app.handler.goose.setGoCbValues.SetGoCbValuesClient;
import com.ysh.jcms.app.handler.goose.setGoCbValues.SetGoCbValuesConsole;
import com.ysh.jcms.app.handler.goose.getGoReference.GetGoReferenceClient;
import com.ysh.jcms.app.handler.goose.getGoReference.GetGoReferenceConsole;
import com.ysh.jcms.app.handler.goose.getGooseElementNumber.GetGooseElementNumberClient;
import com.ysh.jcms.app.handler.goose.getGooseElementNumber.GetGooseElementNumberConsole;
import com.ysh.jcms.app.handler.msv.getMsvcbValues.GetMsvcbValuesClient;
import com.ysh.jcms.app.handler.msv.getMsvcbValues.GetMsvcbValuesConsole;
import com.ysh.jcms.app.handler.msv.setMsvcbValues.SetMsvcbValuesClient;
import com.ysh.jcms.app.handler.msv.setMsvcbValues.SetMsvcbValuesConsole;
import com.ysh.jcms.app.handler.control.cancel.CancelClient;
import com.ysh.jcms.app.handler.control.cancel.CancelConsole;
import com.ysh.jcms.app.handler.control.operate.OperateClient;
import com.ysh.jcms.app.handler.control.operate.OperateConsole;
import com.ysh.jcms.app.handler.control.select.SelectClient;
import com.ysh.jcms.app.handler.control.select.SelectConsole;
import com.ysh.jcms.app.handler.control.timeActivatedOperate.TimeActivatedOperateClient;
import com.ysh.jcms.app.handler.control.timeActivatedOperate.TimeActivatedOperateConsole;
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
 * <p>
 * Connects to a remote CMS server and issues commands interactively.
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
            int apiPort = Integer.parseInt(
                    System.getProperty("cms.api.port", String.valueOf(CmsConfigLoader.load().getClient().getConsole().getApiPort())));
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
        registerClient(new NegotiateClient());
        registerClient(new AssociateClient());
        registerClient(new ReleaseClient());
        registerClient(new AbortClient());
        registerClient(new TestClient());
        registerClient(new SvrDirClient());
        registerClient(new LnDirClient());
        registerClient(new LdDirClient());
        registerClient(new AllDataValuesClient());
        registerClient(new AllDataDefClient());
        registerClient(new AllCbValuesClient());
        registerClient(new GetDataValuesClient());
        registerClient(new SetDataValuesClient());
        registerClient(new GetDataDirectoryClient());
        registerClient(new GetDataDefinitionClient());
        registerClient(new GetDataSetValuesClient());
        registerClient(new GetDataSetDirectoryClient());
        registerClient(new SetDataSetValuesClient());
        registerClient(new CreateDataSetClient());
        registerClient(new DeleteDataSetClient());
        registerClient(new GetSgcbValuesClient());
        registerClient(new SelectActiveSgClient());
        registerClient(new SelectEditSgClient());
        registerClient(new GetEditSgValueClient());
        registerClient(new SetEditSgValueClient());
        registerClient(new ConfirmEditSgValuesClient());
        registerClient(new GetBrcbValuesClient());
        registerClient(new SetBrcbValuesClient());
        registerClient(new GetUrcbValuesClient());
        registerClient(new SetUrcbValuesClient());
        registerClient(new ReportClient());
        registerClient(new GetLcbValuesClient());
        registerClient(new SetLcbValuesClient());
        registerClient(new QueryLogByTimeClient());
        registerClient(new QueryLogAfterClient());
        registerClient(new GetLogStatusValuesClient());
        registerClient(new GetGoCbValuesClient());
        registerClient(new SetGoCbValuesClient());
        registerClient(new GetGoReferenceClient());
        registerClient(new GetGooseElementNumberClient());
        registerClient(new GetMsvcbValuesClient());
        registerClient(new SetMsvcbValuesClient());
        registerClient(new SelectClient());
        registerClient(new OperateClient());
        registerClient(new CancelClient());
        registerClient(new TimeActivatedOperateClient());
        registerClient(new GetFileDirectoryClient());
        registerClient(new GetFileAttributeValuesClient());
        registerClient(new GetFileClient());
        registerClient(new SetFileClient());
        registerClient(new DeleteFileClient());
        registerClient(new GetRpcInterfaceDirectoryClient());
        registerClient(new GetRpcMethodDirectoryClient());
        registerClient(new GetRpcInterfaceDefinitionClient());
        registerClient(new GetRpcMethodDefinitionClient());
        registerClient(new RpcCallClient());

        // Set up push handler for incoming REPORT frames from server
        getClient().setReportHandler(frame -> {
            ReportClient rc = getClient(ReportClient.class);
            if (rc != null) {
                rc.handleReport(frame);
            }
        });
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
        register(new GetBrcbValuesConsole());
        register(new SetBrcbValuesConsole());
        register(new GetUrcbValuesConsole());
        register(new SetUrcbValuesConsole());
        register(new SetDataValuesConsole());
        register(new GetLcbValuesConsole());
        register(new SetLcbValuesConsole());
        register(new QueryLogByTimeConsole());
        register(new QueryLogAfterConsole());
        register(new GetLogStatusValuesConsole());
        register(new GetGoCbValuesConsole());
        register(new SetGoCbValuesConsole());
        register(new GetGoReferenceConsole());
        register(new GetGooseElementNumberConsole());
        register(new GetMsvcbValuesConsole());
        register(new SetMsvcbValuesConsole());
        register(new SelectConsole());
        register(new OperateConsole());
        register(new CancelConsole());
        register(new TimeActivatedOperateConsole());
        register(new GetFileDirectoryConsole());
        register(new GetFileAttributeValuesConsole());
        register(new GetFileConsole());
        register(new SetFileConsole());
        register(new DeleteFileConsole());
        register(new TracePduHandler());
        register(new ClearHandler());
        register(new ReleaseConsole());
        register(new AssociateConsole());
        register(new AbortConsole());
        register(new NegotiateConsole());
        register(new TestConsole());
        register(new GetRpcInterfaceDirectoryConsole());
        register(new GetRpcMethodDirectoryConsole());
        register(new GetRpcInterfaceDefinitionConsole());
        register(new GetRpcMethodDefinitionConsole());
        register(new RpcCallConsole());
    }

    public static void main(String[] args) {
        new CmsClientConsole().run();
    }
}
