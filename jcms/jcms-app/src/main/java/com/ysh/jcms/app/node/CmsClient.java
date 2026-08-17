package com.ysh.jcms.app.node;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.handler.connection.abort.AbortClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.release.ReleaseClient;
import com.ysh.jcms.app.handler.control.cancel.CancelClient;
import com.ysh.jcms.app.handler.control.operate.OperateClient;
import com.ysh.jcms.app.handler.control.select.SelectClient;
import com.ysh.jcms.app.handler.control.timeActivatedOperate.TimeActivatedOperateClient;
import com.ysh.jcms.app.handler.data.getDataDefinition.GetDataDefinitionClient;
import com.ysh.jcms.app.handler.data.getDataDirectory.GetDataDirectoryClient;
import com.ysh.jcms.app.handler.data.getDataValues.GetDataValuesClient;
import com.ysh.jcms.app.handler.data.setDataValues.SetDataValuesClient;
import com.ysh.jcms.app.handler.dataset.createDataSet.CreateDataSetClient;
import com.ysh.jcms.app.handler.dataset.deleteDataSet.DeleteDataSetClient;
import com.ysh.jcms.app.handler.dataset.getDataSetDirectory.GetDataSetDirectoryClient;
import com.ysh.jcms.app.handler.dataset.getDataSetValues.GetDataSetValuesClient;
import com.ysh.jcms.app.handler.dataset.setDataSetValues.SetDataSetValuesClient;
import com.ysh.jcms.app.handler.directory.getAllCbValues.AllCbValuesClient;
import com.ysh.jcms.app.handler.directory.getAllDataDefinition.AllDataDefClient;
import com.ysh.jcms.app.handler.directory.getAllDataValues.AllDataValuesClient;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirClient;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirClient;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirClient;
import com.ysh.jcms.app.handler.file.deleteFile.DeleteFileClient;
import com.ysh.jcms.app.handler.file.getFile.GetFileClient;
import com.ysh.jcms.app.handler.file.getFileAttributeValues.GetFileAttributeValuesClient;
import com.ysh.jcms.app.handler.file.getFileDirectory.GetFileDirectoryClient;
import com.ysh.jcms.app.handler.file.setFile.SetFileClient;
import com.ysh.jcms.app.handler.goose.getGoCbValues.GetGoCbValuesClient;
import com.ysh.jcms.app.handler.goose.getGoReference.GetGoReferenceClient;
import com.ysh.jcms.app.handler.goose.getGooseElementNumber.GetGooseElementNumberClient;
import com.ysh.jcms.app.handler.goose.setGoCbValues.SetGoCbValuesClient;
import com.ysh.jcms.app.handler.log.getLcbValues.GetLcbValuesClient;
import com.ysh.jcms.app.handler.log.getLogStatusValues.GetLogStatusValuesClient;
import com.ysh.jcms.app.handler.log.queryLogAfter.QueryLogAfterClient;
import com.ysh.jcms.app.handler.log.queryLogByTime.QueryLogByTimeClient;
import com.ysh.jcms.app.handler.log.setLcbValues.SetLcbValuesClient;
import com.ysh.jcms.app.handler.msv.getMsvcbValues.GetMsvcbValuesClient;
import com.ysh.jcms.app.handler.msv.setMsvcbValues.SetMsvcbValuesClient;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateClient;
import com.ysh.jcms.app.handler.report.getBrcbValues.GetBrcbValuesClient;
import com.ysh.jcms.app.handler.report.getUrcbValues.GetUrcbValuesClient;
import com.ysh.jcms.app.handler.report.report.ReportClient;
import com.ysh.jcms.app.handler.report.setBrcbValues.SetBrcbValuesClient;
import com.ysh.jcms.app.handler.report.setUrcbValues.SetUrcbValuesClient;
import com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition.GetRpcInterfaceDefinitionClient;
import com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory.GetRpcInterfaceDirectoryClient;
import com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition.GetRpcMethodDefinitionClient;
import com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory.GetRpcMethodDirectoryClient;
import com.ysh.jcms.app.handler.rpc.rpcCall.RpcCallClient;
import com.ysh.jcms.app.handler.sg.confirmEditSgValues.ConfirmEditSgValuesClient;
import com.ysh.jcms.app.handler.sg.getEditSgValue.GetEditSgValueClient;
import com.ysh.jcms.app.handler.sg.getSgcbValues.GetSgcbValuesClient;
import com.ysh.jcms.app.handler.sg.selectActiveSg.SelectActiveSgClient;
import com.ysh.jcms.app.handler.sg.selectEditSg.SelectEditSgClient;
import com.ysh.jcms.app.handler.sg.setEditSgValue.SetEditSgValueClient;
import com.ysh.jcms.app.handler.test.test.TestClient;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * A CMS client node with all client handlers pre-registered.
 *
 * <p>
 * Provides a pure API interface for programmatic use — no JLine, no CLI, no
 * HTTP server. External projects can use this class directly:
 *
 * <pre>
 * CmsClient client = new CmsClient();
 * client.connect("127.0.0.1", 8102);
 * client.associate("C_B5041X/S1");
 * AllDataValuesDao r = client.execute(new AllDataValuesDao().lnRef("LD0/LLN0").fc("ST"));
 * </pre>
 */
public class CmsClient extends CmsNode {

    private final Map<Class<?>, BaseClientHandler<?>> daoToHandler = new HashMap<>();

    public CmsClient() {
        super(false);
        registerClients();
    }

    /* ====== handler registration ====== */

    @Override
    public void registerClient(BaseClientHandler handler) {
        super.registerClient(handler);
        Class<?> daoType = resolveDaoType(handler);
        if (daoType != null) {
            daoToHandler.put(daoType, handler);
        }
    }

    private void registerClients() {
        // Connection
        registerClient(new NegotiateClient());
        registerClient(new AssociateClient());
        registerClient(new ReleaseClient());
        registerClient(new AbortClient());
        registerClient(new TestClient());
        // Directory
        registerClient(new SvrDirClient());
        registerClient(new LnDirClient());
        registerClient(new LdDirClient());
        registerClient(new AllDataValuesClient());
        registerClient(new AllDataDefClient());
        registerClient(new AllCbValuesClient());
        // Data
        registerClient(new GetDataValuesClient());
        registerClient(new SetDataValuesClient());
        registerClient(new GetDataDirectoryClient());
        registerClient(new GetDataDefinitionClient());
        // DataSet
        registerClient(new GetDataSetValuesClient());
        registerClient(new GetDataSetDirectoryClient());
        registerClient(new SetDataSetValuesClient());
        registerClient(new CreateDataSetClient());
        registerClient(new DeleteDataSetClient());
        // Setting Group
        registerClient(new GetSgcbValuesClient());
        registerClient(new SelectActiveSgClient());
        registerClient(new SelectEditSgClient());
        registerClient(new GetEditSgValueClient());
        registerClient(new SetEditSgValueClient());
        registerClient(new ConfirmEditSgValuesClient());
        // Report
        registerClient(new GetBrcbValuesClient());
        registerClient(new SetBrcbValuesClient());
        registerClient(new GetUrcbValuesClient());
        registerClient(new SetUrcbValuesClient());
        registerClient(new ReportClient());
        // Log
        registerClient(new GetLcbValuesClient());
        registerClient(new SetLcbValuesClient());
        registerClient(new QueryLogByTimeClient());
        registerClient(new QueryLogAfterClient());
        registerClient(new GetLogStatusValuesClient());
        // GOOSE
        registerClient(new GetGoCbValuesClient());
        registerClient(new SetGoCbValuesClient());
        registerClient(new GetGoReferenceClient());
        registerClient(new GetGooseElementNumberClient());
        // MSV
        registerClient(new GetMsvcbValuesClient());
        registerClient(new SetMsvcbValuesClient());
        // Control
        registerClient(new SelectClient());
        registerClient(new OperateClient());
        registerClient(new CancelClient());
        registerClient(new TimeActivatedOperateClient());
        // File
        registerClient(new GetFileDirectoryClient());
        registerClient(new GetFileAttributeValuesClient());
        registerClient(new GetFileClient());
        registerClient(new SetFileClient());
        registerClient(new DeleteFileClient());
        // RPC
        registerClient(new GetRpcInterfaceDirectoryClient());
        registerClient(new GetRpcMethodDirectoryClient());
        registerClient(new GetRpcInterfaceDefinitionClient());
        registerClient(new GetRpcMethodDefinitionClient());
        registerClient(new RpcCallClient());

        // Set up push handler for incoming REPORT frames
        client().reportHandler(frame -> {
            ReportClient rc = getClient(ReportClient.class);
            if (rc != null) {
                rc.handleReport(frame);
            }
        });
    }

    /**
     * Resolve the DAO type from a ClientHandler's generic parameter by walking the
     * class hierarchy.
     */
    private static Class<?> resolveDaoType(BaseClientHandler<?> handler) {
        Class<?> clazz = handler.getClass();
        while (clazz != null && clazz != Object.class) {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                Type raw = pt.getRawType();
                if (raw == BaseClientHandler.class || raw.getTypeName().equals(BaseClientHandler.class.getName())) {
                    Type arg = pt.getActualTypeArguments()[0];
                    if (arg instanceof Class) {
                        return (Class<?>) arg;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /* ====== execute ====== */

    /**
     * Execute a DAO request against the registered client handler.
     * <p>
     * Finds the matching {@link BaseClientHandler} for the given DAO's type and
     * executes it. The DAO is populated with the response data after execution.
     *
     * @param <D> the DAO type
     * @param dao the request DAO
     * @return the same DAO instance, populated with response data
     * @throws Exception if execution fails
     */
    @SuppressWarnings("unchecked")
    public <D extends BaseDao> D execute(D dao) throws Exception {
        Class<?> daoClass = dao.getClass();
        BaseClientHandler<D> handler = (BaseClientHandler<D>) daoToHandler.get(daoClass);
        if (handler == null) {
            // Fallback: try to find by walking superclasses
            for (Map.Entry<Class<?>, BaseClientHandler<?>> entry : daoToHandler.entrySet()) {
                if (entry.getKey().isAssignableFrom(daoClass)) {
                    handler = (BaseClientHandler<D>) entry.getValue();
                    break;
                }
            }
        }
        if (handler == null) {
            throw new IllegalArgumentException("No client handler registered for DAO type: " + daoClass.getSimpleName());
        }
        handler.execute(dao);
        return dao;
    }
}