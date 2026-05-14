package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDAI;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDOI;
import com.ysh.dlt2811bean.scl.model.SclIED.SclSDI;
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

        CmsStructure values = new CmsStructure();
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

            values.add(resolveValue(accessPoint.getServer(), serverSession.getSclDataTypeTemplates(), memberRef));
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

    private CmsType resolveValue(SclIED.SclServer server, SclDataTypeTemplates templates, String ref) {
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

        // DO-level reference (e.g. C1/MMXU1.Volts) — return first DAI with a value
        if (parts.length == 2) {
            for (SclDAI dai : doi.getDais()) {
                if (dai.getValue() != null && !dai.getValue().isEmpty()) {
                    String bType = SclTypeResolver.resolveBType(server, templates, ldName, lnName, parts[1], dai.getName());
                    return SclTypeResolver.createTypedValue(bType, dai.getValue());
                }
            }
            return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String daName = parts[parts.length - 1];

        // DA-level reference (e.g. C1/TVTR1.Vol.instMag)
        if (parts.length == 3) {
            for (SclDAI dai : doi.getDais()) {
                if (dai.getName().equals(daName) && dai.getValue() != null && !dai.getValue().isEmpty()) {
                    String bType = SclTypeResolver.resolveBType(server, templates, ldName, lnName, parts[1], daName);
                    return SclTypeResolver.createTypedValue(bType, dai.getValue());
                }
            }
            return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // SDI.BDA-level reference (e.g. C1/MMXU1.Volts.sVC.offset)
        if (parts.length == 4) {
            String sdiName = parts[2];
            String bdaName = parts[3];
            SclSDI sdi = findSdi(doi, sdiName);
            if (sdi == null) {
                return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            for (SclDAI dai : sdi.getDais()) {
                if (dai.getName().equals(bdaName) && dai.getValue() != null && !dai.getValue().isEmpty()) {
                    String bType = SclTypeResolver.resolveSdiBType(server, templates, ldName, lnName, parts[1], sdiName, bdaName);
                    return SclTypeResolver.createTypedValue(bType, dai.getValue());
                }
            }
            return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        return new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
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
            String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
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

    private SclSDI findSdi(SclDOI doi, String sdiName) {
        if (doi.getSdis() == null) return null;
        for (SclSDI sdi : doi.getSdis()) {
            if (sdi.getName().equals(sdiName)) {
                return sdi;
            }
        }
        return null;
    }

    private SclIED.SclDataSet findDataSet(SclIED.SclLDevice device, String ref) {
        int dotIdx = ref.indexOf('.');
        if (dotIdx < 0) {
            if (device.getLn0() == null) return null;
            for (SclIED.SclDataSet ds : device.getLn0().getDataSets()) {
                if (ds.getName().equals(ref)) {
                    return ds;
                }
            }
            return null;
        }
        String lnName = ref.substring(0, dotIdx);
        String dsName = ref.substring(dotIdx + 1);
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                for (SclIED.SclDataSet ds : device.getLn0().getDataSets()) {
                    if (ds.getName().equals(dsName)) {
                        return ds;
                    }
                }
                return null;
            }
        }
        for (SclIED.SclLN ln : device.getLns()) {
            String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                for (SclIED.SclDataSet ds : ln.getDataSets()) {
                    if (ds.getName().equals(dsName)) {
                        return ds;
                    }
                }
                return null;
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
