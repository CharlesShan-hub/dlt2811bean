package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDAI;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDOI;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsSetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsSetDataValuesEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetDataValuesHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(SetDataValuesHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_DATA_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SetDataValues: {}", e.getMessage(), e);
            return null;
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsSetDataValues asdu = (CmsSetDataValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return null;
        }

        SclIED.SclServer server = accessPoint.getServer();

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new).capacity(asdu.data.size());
        boolean allSuccess = true;

        for (CmsSetDataValuesEntry entry : asdu.data) {
            String ref = entry.reference.get();
            int error = validateAndSetValue(server, ref, entry);
            results.add(new CmsServiceError(error));
            if (error != CmsServiceError.NO_ERROR) {
                allSuccess = false;
            }
        }

        log.debug("[Server] SetDataValues: {} entries, allSuccess={}", asdu.data.size(), allSuccess);

        if (allSuccess) {
            CmsSetDataValues response = new CmsSetDataValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get());
            return new CmsApdu(response);
        } else {
            CmsSetDataValues response = new CmsSetDataValues(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get())
                    .result(results);
            return new CmsApdu(response);
        }
    }

    private int validateAndSetValue(SclIED.SclServer server, String ref, CmsSetDataValuesEntry entry) {
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);

        SclIED.SclLDevice device = findLDevice(server, ldName);
        if (device == null) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        String[] parts = rest.split("\\.");
        if (parts.length < 2) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        String lnName = parts[0];
        String doName = parts[1];

        SclDOI doi = findDoiInDevice(device, lnName, doName);
        if (doi == null) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        String daName = parts[parts.length - 1];
        SclDAI dai = findDaiByName(doi.getDais(), daName);
        if (dai == null) {
            if (parts.length == 2) {
                return CmsServiceError.NO_ERROR;
            }
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        String newValue = entry.value != null ? entry.value.getInnerValue().toString() : null;
        if (newValue != null) {
            int idx = newValue.lastIndexOf(") ");
            if (idx >= 0) newValue = newValue.substring(idx + 2);
            dai.setValue(newValue);
            log.debug("[Server] Set {} = {}", ref, newValue);
        }
        return CmsServiceError.NO_ERROR;
    }

    private SclDAI findDaiByName(java.util.List<SclDAI> dais, String name) {
        for (SclDAI dai : dais) {
            if (dai.getName().equals(name)) {
                return dai;
            }
        }
        return null;
    }

    private SclDOI findDoiInDevice(SclIED.SclLDevice device, String lnName, String doName) {
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                for (SclDOI doi : device.getLn0().getDois()) {
                    if (doi.getName().equals(doName)) return doi;
                }
                return null;
            }
        }
        for (SclIED.SclLN ln : device.getLns()) {
            String curLnName = ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                for (SclDOI doi : ln.getDois()) {
                    if (doi.getName().equals(doName)) return doi;
                }
                return null;
            }
        }
        return null;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice ld : server.getLDevices()) {
            if (ld.getInst().equals(ldName)) {
                return ld;
            }
        }
        return null;
    }
}
