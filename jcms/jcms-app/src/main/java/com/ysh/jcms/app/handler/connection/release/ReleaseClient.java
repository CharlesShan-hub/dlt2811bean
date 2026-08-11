package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseHandler;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.connection.CmsReleaseError;
import com.ysh.jcms.pdu.connection.CmsReleaseResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.SessionState;

import java.io.IOException;

public class ReleaseClient extends BaseClientHandler<ReleaseDao> {

    @Override
    public void execute(ReleaseDao dao) throws Exception {
        send(ServiceName.RELEASE, dao);
    }

    @Override
    protected void beforeAll(ReleaseDao dao) throws IOException {
        byte[] assocId = node.client().session().associationId();
        if (assocId != null && assocId.length > 0) {
            dao.associationId(assocId);
        }
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsReleaseError err = decodeErr(frame, new CmsReleaseError());
        throw new IOException("Release rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, ReleaseDao dao) throws IOException {
        CmsReleaseResponse resp = decodeResp(frame, new CmsReleaseResponse());

        int serviceError = resp.serviceError.value();
        if (serviceError != CmsServiceError.NO_ERROR) {
            throw new IOException("Release rejected: serviceError=" + serviceError);
        }

        node.client().session().clear();
        node.client().session().state(SessionState.CONNECTED);
        BaseHandler.traceSession("Released");
    }
}
