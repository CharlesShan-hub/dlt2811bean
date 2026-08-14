package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.core.util.CmsDataUtil;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.data.CmsDataRefValueEntry;
import com.ysh.jcms.core.pdu.data.CmsSetDataValuesError;
import com.ysh.jcms.core.pdu.data.CmsSetDataValuesRequest;
import com.ysh.jcms.core.pdu.data.CmsSetDataValuesResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataWriterResolver;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.navigate.TypeChain;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class SetDataValuesServer extends BaseServerHandler<CmsSetDataValuesRequest, CmsSetDataValuesError> {

    public SetDataValuesServer() {
        super(CmsServiceInfo.SET_DATA_VALUES, CmsSetDataValuesRequest.class, CmsSetDataValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSetDataValuesRequest req = (CmsSetDataValuesRequest) decoded;
        req.data.add(new CmsDataRefValueEntry());
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSetDataValuesRequest req, int reqId) {
        log.info("LOG7 onDecodeSuccess: session={}, reqId={}, entries={}", session.sessionId(), reqId, req.data.size());

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        int successCount = 0;
        for (CmsDataRefValueEntry entry : req.data) {
            String ref = str(entry.reference);
            if (ref == null)
                continue;

            String valueStr = CmsDataUtil.toValueString(entry.value);
            if (valueStr == null)
                continue;
            log.info("LOG8 onDecodeSuccess: ref={}, valueStr={}, dataChoice={}", ref, valueStr, entry.value.choice());

            Navigator nav = Navigator.go(doc, ied, ref);
            if (!nav.isValid()) {
                log.warn("LOG9 onDecodeSuccess: nav invalid for ref={}", ref);
                continue;
            }

            // 查找 SCL 定义的 bType
            String bType = null;
            if (nav.ref().isDaLevel() && nav.document() != null && nav.document().dataTypeTemplates() != null) {
                StringBuilder typeRef = new StringBuilder(nav.ref().doName());
                for (String sdi : nav.ref().sdiChain())
                    typeRef.append(".").append(sdi);
                typeRef.append(".").append(nav.ref().daName());
                bType = TypeChain.of(nav.document().dataTypeTemplates()).resolveBType(nav.ln().lnType(), typeRef.toString());
            }
            log.info("LOG9 onDecodeSuccess: ref={}, sclBType={}", ref, bType);

            int result = DataWriterResolver.setValue(nav, valueStr);
            log.info("LOG9 onDecodeSuccess: ref={}, setValueResult={} ({})", ref, result,
                    result == CmsServiceError.NO_ERROR ? "SUCCESS" : "FAILED");
            if (result == CmsServiceError.NO_ERROR) {
                successCount++;
            }
        }
        log.info("LOG10 onDecodeSuccess: {}/{} entries set successfully", successCount, req.data.size());
        if (successCount < req.data.size()) {
            CmsSetDataValuesError err = new CmsSetDataValuesError();
            for (int i = 0; i < req.data.size(); i++) {
                int code = i < successCount ? CmsServiceError.NO_ERROR : CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT;
                err.result.add(code);
            }
            try {
                return buildError(err.encode(), reqId);
            } catch (Exception ex) {
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }
        return ok(new CmsSetDataValuesResponse(), reqId);
    }
}
