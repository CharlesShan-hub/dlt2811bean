package com.ysh.dlt2811bean.transport.protocol.goose;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.goose.CmsSetGoCBValues;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesResultEntry;
import com.ysh.dlt2811bean.transport.goose.GooseConfig;
import com.ysh.dlt2811bean.transport.goose.GoosePublisher;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SetGoCBValuesHandler extends AbstractCmsServiceHandler<CmsSetGoCBValues> {

    private final GoosePublisher goosePublisher;

    public SetGoCBValuesHandler() {
        super(ServiceName.SET_GOCB_VALUES, CmsSetGoCBValues::new);
        this.goosePublisher = null;
    }

    public SetGoCBValuesHandler(GoosePublisher goosePublisher) {
        super(ServiceName.SET_GOCB_VALUES, CmsSetGoCBValues::new);
        this.goosePublisher = goosePublisher;
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_GOCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SetGoCBValues: {}", e.getMessage(), e);
            int reqId = request != null ? ((CmsSetGoCBValues) request.getAsdu()).reqId().get() : 0;
            return buildNegativeResponse(reqId,
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsSetGoCBValues asdu = (CmsSetGoCBValues) request.getAsdu();

        if (asdu.gocb == null || asdu.gocb.size() == 0) {
            return buildNegativeResponse(asdu.reqId().get(),
                    CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        CmsArray<CmsSetGoCBValuesResultEntry> results = new CmsArray<>(CmsSetGoCBValuesResultEntry::new).capacity(100);
        for (int i = 0; i < asdu.gocb.size(); i++) {
            CmsSetGoCBValuesEntry entry = asdu.gocb.get(i);
            CmsSetGoCBValuesResultEntry result = new CmsSetGoCBValuesResultEntry();

            if (entry.goEna.isPresent() && goosePublisher != null) {
                String goCBRef = entry.reference.get();
                boolean goEna = entry.goEna.get();
                if (goEna) {
                    GooseConfig config = GooseConfig.builder()
                            .goCBRef(goCBRef)
                            .goID(entry.goID.isPresent() ? entry.goID.get() : goCBRef)
                            .build();
                    goosePublisher.start(config);
                    log.info("GOOSE publishing started via SetGoCBValues: {}", goCBRef);
                } else {
                    goosePublisher.stop(goCBRef);
                    log.info("GOOSE publishing stopped via SetGoCBValues: {}", goCBRef);
                }
            }

            results.add(result);
        }

        if (hasAnyError(results)) {
            CmsSetGoCBValues response = new CmsSetGoCBValues(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get());
            response.result = results;
            log.debug("[Server] SetGoCBValues: {} entries with errors", results.size());
            return new CmsApdu(response);
        }

        CmsSetGoCBValues response = new CmsSetGoCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        log.debug("[Server] SetGoCBValues: {} entries accepted", results.size());
        return new CmsApdu(response);
    }

    private boolean hasAnyError(CmsArray<CmsSetGoCBValuesResultEntry> results) {
        return false;
    }

    private CmsApdu buildNegativeResponse(int reqId, int errorCode) {
        CmsSetGoCBValues response = new CmsSetGoCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(reqId);
        CmsSetGoCBValuesResultEntry entry = new CmsSetGoCBValuesResultEntry();
        entry.error.set(errorCode);
        response.result = new CmsArray<>(CmsSetGoCBValuesResultEntry::new).capacity(1);
        response.result.add(entry);
        return new CmsApdu(response);
    }
}
