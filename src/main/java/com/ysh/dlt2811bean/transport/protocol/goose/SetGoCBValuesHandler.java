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
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import static com.ysh.dlt2811bean.transport.protocol.goose.GetGoCBValuesHandler.goEnaState;

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
    protected CmsApdu doServerHandle() {
        CmsSetGoCBValues asdu = (CmsSetGoCBValues) request.getAsdu();

        if (asdu.gocb == null || asdu.gocb.size() == 0) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        CmsArray<CmsSetGoCBValuesResultEntry> results = new CmsArray<>(CmsSetGoCBValuesResultEntry::new);
        for (int i = 0; i < asdu.gocb.size(); i++) {
            CmsSetGoCBValuesEntry entry = asdu.gocb.get(i);
            CmsSetGoCBValuesResultEntry result = new CmsSetGoCBValuesResultEntry();

            if (entry.goEna.isPresent()) {
                String goCBRef = entry.reference.get();
                boolean goEna = entry.goEna.get();
                goEnaState.put(goCBRef, goEna);
                if (goosePublisher != null) {
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

    @Override
    protected CmsApdu buildNegativeResponse(int errorCode) {
        CmsSetGoCBValues response = new CmsSetGoCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.getReqId());
        CmsSetGoCBValuesResultEntry entry = new CmsSetGoCBValuesResultEntry();
        entry.error.set(errorCode);
        response.result = new CmsArray<>(CmsSetGoCBValuesResultEntry::new).capacity(1);
        response.result.add(entry);
        return new CmsApdu(response);
    }
}
