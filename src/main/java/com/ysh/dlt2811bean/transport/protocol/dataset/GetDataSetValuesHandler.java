package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetValues;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetDataSetValuesHandler extends AbstractCmsServiceHandler<CmsGetDataSetValues> {

    public GetDataSetValuesHandler() {
        super(ServiceName.GET_DATA_SET_VALUES, CmsGetDataSetValues::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetDataSetValues asdu = (CmsGetDataSetValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String dsRef = asdu.datasetReference.get();
        if (dsRef == null || dsRef.isEmpty()) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = dsRef.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = dsRef.substring(0, slashIdx);
        String rest = dsRef.substring(slashIdx + 1);

        SclIED.SclLDevice device = findLDevice(accessPoint.getServer(), ldName);
        if (device == null) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclIED.SclDataSet dataSet = findDataSet(device, rest);
        if (dataSet == null) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String afterRef = asdu.referenceAfter.get();
        boolean skipUntilAfter = afterRef != null && !afterRef.isEmpty();

        CmsStructure values = new CmsStructure().capacity(100);
        boolean foundAfter = false;

        for (SclIED.SclFCDA fcda : dataSet.getFcdaList()) {
            String memberRef = buildFcdaRef(fcda);

            if (skipUntilAfter) {
                if (!foundAfter) {
                    if (memberRef.equals(afterRef)) {
                        foundAfter = true;
                    }
                    continue;
                }
            }

            values.add(resolveValue(accessPoint.getServer(), memberRef));
        }

        if (skipUntilAfter && !foundAfter) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        CmsGetDataSetValues response = new CmsGetDataSetValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.value = values;
        response.moreFollows.set(false);

        log.debug("[Server] GetDataSetValues: {}, {} members", dsRef, values.size());
        return new CmsApdu(response);
    }

    private String buildFcdaRef(SclIED.SclFCDA fcda) {
        StringBuilder sb = new StringBuilder();
        sb.append(fcda.getLdInst()).append("/");
        if (fcda.getPrefix() != null && !fcda.getPrefix().isEmpty()) {
            sb.append(fcda.getPrefix());
        }
        sb.append(fcda.getLnClass());
        if (fcda.getLnInst() != null && !fcda.getLnInst().isEmpty()) {
            sb.append(fcda.getLnInst());
        }
        sb.append(".").append(fcda.getDoName());
        if (fcda.getDaName() != null && !fcda.getDaName().isEmpty()) {
            sb.append(".").append(fcda.getDaName());
        }
        return sb.toString();
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
            return new CmsVisibleString("").max(255);
        }

        return new CmsVisibleString(ref).max(255);
    }

    private SclIED.SclDataSet findDataSet(SclIED.SclLDevice device, String ref) {
        if (device.getLn0() == null) return null;
        int dotIdx = ref.indexOf('.');
        if (dotIdx < 0) return null;
        String dsName = ref.substring(dotIdx + 1);
        for (SclIED.SclDataSet ds : device.getLn0().getDataSets()) {
            if (ds.getName().equals(dsName)) {
                return ds;
            }
        }
        return null;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice device : server.getLDevices()) {
            if (device.getInst().equals(ldName)) {
                return device;
            }
        }
        return null;
    }
}
