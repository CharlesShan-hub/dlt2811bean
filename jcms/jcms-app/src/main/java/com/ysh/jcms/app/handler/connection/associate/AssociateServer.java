package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.ServiceException;
import com.ysh.jcms.app.node.InnerServer;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.connection.CmsAuthenticationParameter;
import com.ysh.jcms.pdu.connection.CmsAssociateError;
import com.ysh.jcms.pdu.connection.CmsAssociateRequest;
import com.ysh.jcms.pdu.connection.CmsAssociateResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.service.SclAccessPointService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.AssociationIdGenerator;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;

public class AssociateServer extends BaseServerHandler<CmsAssociateRequest, CmsAssociateError> {

    private final AssociateSecurity security = new AssociateSecurity();

    public AssociateServer() {
        super(ServiceName.ASSOCIATE, CmsAssociateRequest.class, CmsAssociateError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsAssociateRequest req, int reqId) {
        String sapRef = req.isPresent("serverAccessPointReference") ? req.serverAccessPointReference.value() : null;
        log.info("Associate request from {}: reqId={}, sapRef={}", session.getSessionId(), reqId, sapRef);

        if (session.isAssociated())
            return onDecodeError(reqId, CmsServiceError.INSTANCE_IN_USE);

        // 未指定访问点时选默认
        resolveAndBindScl(session, sapRef, reqId);

        if (req.isPresent("authenticationParameter") && req.authenticationParameter.signatureCertificate.value().length > 0) {
            security.ensureInitialized();
            int authError = security.validate(req, sapRef);
            if (authError != CmsServiceError.NO_ERROR)
                return onDecodeError(reqId, authError);
        }

        byte[] assocId = AssociationIdGenerator.generate();
        CmsAssociateResponse resp = new CmsAssociateResponse().associationId(assocId).serviceError(CmsServiceError.NO_ERROR);

        // 返回服务端证书 + 签名（标准 B.3.2 要求双向认证）
        CmsAuthenticationParameter serverAuth = security.buildAuthParam(sapRef);
        if (serverAuth != null)
            resp.authenticationParameter(serverAuth);

        byte[] respBytes = resp.encode();
        session.setAssociationId(assocId);
        session.setState(SessionState.ASSOCIATED);
        log.info("Association established: session={}", session.getSessionId());
        return buildSuccess(respBytes, reqId);
    }

    /**
     * 解析并绑定 SCL 访问点到会话。解析失败时抛 {@link ServiceException} 拒绝关联。
     */
    private void resolveAndBindScl(Session session, String sapRef, int reqId) {
        if (!(session instanceof InnerServer.ServerSession))
            return;
        InnerServer.ServerSession ss = (InnerServer.ServerSession) session;
        SclDocument scl = ss.getSclDocument();
        if (scl == null) {
            log.warn("Associate rejected: no SCL model loaded on server");
            throw new ServiceException(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclAccessPointService.ResolvedAp ap = sapRef == null
                ? SclAccessPointService.resolveDefault(scl)
                : SclAccessPointService.resolve(scl, sapRef);
        if (ap == null) {
            log.warn("Associate rejected: cannot resolve access point from sapRef={}", sapRef);
            throw new ServiceException(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        ss.setSclAccessPoint(ap.ap);
        ss.setSclIed(ap.ied);
        ss.setSclDataTypeTemplates(scl.dataTypeTemplates());
        log.info("Resolved SCL access point: IED={}, AP={}", ap.ied.name(), ap.ap.name());
    }
}
