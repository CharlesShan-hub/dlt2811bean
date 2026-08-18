package com.ysh.jcms.app.console;

import com.ysh.jcms.app.handler.console.client.*;
import com.ysh.jcms.app.handler.console.LogHandler;
import com.ysh.jcms.app.handler.console.ClearHandler;
import com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory.GetRpcInterfaceDirectoryConsole;
import com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory.GetRpcMethodDirectoryConsole;
import com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition.GetRpcInterfaceDefinitionConsole;
import com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition.GetRpcMethodDefinitionConsole;
import com.ysh.jcms.app.handler.rpc.rpcCall.RpcCallConsole;
import com.ysh.jcms.app.handler.data.getDataValues.GetDataValuesConsole;
import com.ysh.jcms.app.handler.data.getDataDirectory.GetDataDirectoryConsole;
import com.ysh.jcms.app.handler.data.getDataDefinition.GetDataDefinitionConsole;
import com.ysh.jcms.app.handler.dataset.getDataSetValues.GetDataSetValuesConsole;
import com.ysh.jcms.app.handler.dataset.setDataSetValues.SetDataSetValuesConsole;
import com.ysh.jcms.app.handler.dataset.getDataSetDirectory.GetDataSetDirectoryConsole;
import com.ysh.jcms.app.handler.dataset.createDataSet.CreateDataSetConsole;
import com.ysh.jcms.app.handler.dataset.deleteDataSet.DeleteDataSetConsole;
import com.ysh.jcms.app.handler.sg.getSgcbValues.GetSgcbValuesConsole;
import com.ysh.jcms.app.handler.sg.selectActiveSg.SelectActiveSgConsole;
import com.ysh.jcms.app.handler.sg.selectEditSg.SelectEditSgConsole;
import com.ysh.jcms.app.handler.sg.getEditSgValue.GetEditSgValueConsole;
import com.ysh.jcms.app.handler.sg.setEditSgValue.SetEditSgValueConsole;
import com.ysh.jcms.app.handler.sg.confirmEditSgValues.ConfirmEditSgValuesConsole;
import com.ysh.jcms.app.handler.report.getBrcbValues.GetBrcbValuesConsole;
import com.ysh.jcms.app.handler.report.setBrcbValues.SetBrcbValuesConsole;
import com.ysh.jcms.app.handler.report.getUrcbValues.GetUrcbValuesConsole;
import com.ysh.jcms.app.handler.report.setUrcbValues.SetUrcbValuesConsole;
import com.ysh.jcms.app.handler.log.getLcbValues.GetLcbValuesConsole;
import com.ysh.jcms.app.handler.log.setLcbValues.SetLcbValuesConsole;
import com.ysh.jcms.app.handler.log.queryLogByTime.QueryLogByTimeConsole;
import com.ysh.jcms.app.handler.log.queryLogAfter.QueryLogAfterConsole;
import com.ysh.jcms.app.handler.log.getLogStatusValues.GetLogStatusValuesConsole;
import com.ysh.jcms.app.handler.file.deleteFile.DeleteFileConsole;
import com.ysh.jcms.app.handler.file.getFile.GetFileConsole;
import com.ysh.jcms.app.handler.file.getFileAttributeValues.GetFileAttributeValuesConsole;
import com.ysh.jcms.app.handler.file.getFileDirectory.GetFileDirectoryConsole;
import com.ysh.jcms.app.handler.file.setFile.SetFileConsole;
import com.ysh.jcms.app.handler.goose.getGoCbValues.GetGoCbValuesConsole;
import com.ysh.jcms.app.handler.goose.setGoCbValues.SetGoCbValuesConsole;
import com.ysh.jcms.app.handler.goose.getGoReference.GetGoReferenceConsole;
import com.ysh.jcms.app.handler.goose.getGooseElementNumber.GetGooseElementNumberConsole;
import com.ysh.jcms.app.handler.msv.getMsvcbValues.GetMsvcbValuesConsole;
import com.ysh.jcms.app.handler.msv.setMsvcbValues.SetMsvcbValuesConsole;
import com.ysh.jcms.app.handler.control.cancel.CancelConsole;
import com.ysh.jcms.app.handler.control.operate.OperateConsole;
import com.ysh.jcms.app.handler.control.select.SelectConsole;
import com.ysh.jcms.app.handler.control.timeActivatedOperate.TimeActivatedOperateConsole;
import com.ysh.jcms.app.handler.data.setDataValues.SetDataValuesConsole;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirConsole;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirConsole;
import com.ysh.jcms.app.handler.directory.getAllCbValues.AllCbValuesConsole;
import com.ysh.jcms.app.handler.directory.getAllDataDefinition.AllDataDefConsole;
import com.ysh.jcms.app.handler.directory.getAllDataValues.AllDataValuesConsole;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirConsole;
import com.ysh.jcms.app.handler.connection.release.ReleaseConsole;
import com.ysh.jcms.app.handler.connection.abort.AbortConsole;
import com.ysh.jcms.app.handler.connection.associate.AssociateConsole;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateConsole;
import com.ysh.jcms.app.handler.test.test.TestConsole;
import com.ysh.jcms.app.node.CmsClient;

/**
 * Client-side CMS console.  Only defines which handlers to register;
 * all other behaviour is inherited from {@link CmsConsole} default methods.
 */
public class CmsClientConsole extends CmsClient implements CmsConsole {

    @Override
    public void registerHandlers() {
        register(new HelpHandler(this));
        register(new ConnectHandler());
        register(new DisconnectHandler());
        register(new ApDirHandler());
        register(new ApCfgHandler());
        register(new NegCfgHandler());
        register(new ConformanceHandler());
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
        register(new LogHandler());
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
