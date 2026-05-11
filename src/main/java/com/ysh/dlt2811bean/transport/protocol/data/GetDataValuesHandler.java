package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDAI;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDOI;
import com.ysh.dlt2811bean.scl.model.SclIED.SclSDI;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

@SuppressWarnings("rawtypes")
public class GetDataValuesHandler extends AbstractCmsServiceHandler<CmsGetDataValues> {

    public GetDataValuesHandler() {
        super(ServiceName.GET_DATA_VALUES, CmsGetDataValues::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetDataValues asdu = (CmsGetDataValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclIED.SclServer server = accessPoint.getServer();

        CmsStructure values = new CmsStructure().capacity(100);
        for (CmsGetDataValuesEntry entry : asdu.data) {
            String ref = entry.reference.get();
            values.add(resolveValue(server, ref));
        }

        CmsGetDataValues response = new CmsGetDataValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .value(values);
        response.moreFollows.set(false);

        log.debug("[Server] GetDataValues: {} entries", values.size());
        return new CmsApdu(response);
    }

    private CmsType resolveValue(SclIED.SclServer server, String ref) {
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);

        SclIED.SclLDevice device = findLDevice(server, ldName);
        if (device == null) {
            return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String[] parts = rest.split("\\.");
        if (parts.length < 1) {
            return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String lnName = parts[0];
        SclDOI doi = findDoiInDevice(device, lnName, parts.length > 1 ? parts[1] : null);
        if (doi == null) {
            return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (parts.length == 2) {
            return resolveDoiValue(doi);
        }

        String daName = parts[parts.length - 1];
        if (parts.length == 3) {
            return resolveDaiValue(doi.getDais(), daName);
        }

        if (parts.length == 4) {
            String sdiName = parts[2];
            SclSDI sdi = findSdi(doi, sdiName);
            if (sdi == null) {
                return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            return resolveDaiValue(sdi.getDais(), daName);
        }

        return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
    }

    private CmsType resolveDoiValue(SclDOI doi) {
        for (SclDAI dai : doi.getDais()) {
            if (dai.getValue() != null && !dai.getValue().isEmpty()) {
                return new CmsVisibleString(dai.getValue()).max(255);
            }
        }
        return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
    }

    private CmsType resolveDaiValue(java.util.List<SclDAI> dais, String daName) {
        for (SclDAI dai : dais) {
            if (dai.getName().equals(daName) && dai.getValue() != null && !dai.getValue().isEmpty()) {
                return new CmsVisibleString(dai.getValue()).max(255);
            }
        }
        return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
    }

    private SclSDI findSdi(SclDOI doi, String sdiName) {
        for (SclSDI sdi : doi.getSdis()) {
            if (sdi.getName().equals(sdiName)) {
                return sdi;
            }
        }
        return null;
    }

    private SclDOI findDoiInDevice(SclIED.SclLDevice device, String lnName, String doName) {
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                if (doName == null) return null;
                for (SclDOI doi : device.getLn0().getDois()) {
                    if (doi.getName().equals(doName)) return doi;
                }
                return null;
            }
        }
        for (SclIED.SclLN ln : device.getLns()) {
            String curLnName = ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                if (doName == null) return null;
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
