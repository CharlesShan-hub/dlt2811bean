package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.scl.SclDocument;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDA;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDO;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDOType;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclLNodeType;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDAI;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDOI;
import com.ysh.dlt2811bean.scl.model.SclIED.SclLN;
import com.ysh.dlt2811bean.scl.model.SclIED.SclLN0;
import com.ysh.dlt2811bean.scl.model.SclIED.SclSDI;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GetAllDataValuesHandler implements CmsServiceHandler {

    private final SclDocument sclDocument;

    public GetAllDataValuesHandler(SclDocument sclDocument) {
        this.sclDocument = sclDocument;
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_ALL_DATA_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetAllDataValues: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetAllDataValues) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) throws Exception {
        CmsGetAllDataValues asdu = (CmsGetAllDataValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = null;
        String lnRef = null;
        boolean useLdName = asdu.reference.getSelectedIndex() == 0;
        log.debug("[Server] reference selectedIndex={}, useLdName={}",
                asdu.reference.getSelectedIndex(), useLdName);
        if (useLdName) {
            ldName = asdu.reference.ldName.get();
            log.debug("[Server] reference ldName='{}'", ldName);
        } else {
            lnRef = asdu.reference.lnReference.get();
            log.debug("[Server] reference lnReference='{}'", lnRef);
        }

        String fcFilter = asdu.fc != null ? asdu.fc.get() : null;
        if (fcFilter != null && (fcFilter.isEmpty() || "XX".equals(fcFilter))) {
            fcFilter = null;
        }

        List<TargetLn> targets = resolveTargets(accessPoint, ldName, lnRef);
        if (targets == null) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<DataValue> values = collectDataValues(targets, !useLdName, fcFilter);

        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        int startIndex = 0;
        if (after != null && !after.isEmpty()) {
            boolean found = false;
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).ref.equals(after)) {
                    startIndex = i + 1;
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.warn("[Server] referenceAfter not found: {}", after);
                return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        CmsArray<CmsDataEntry> data = new CmsArray<>(CmsDataEntry::new).capacity(Math.max(1, values.size() - startIndex));
        for (int i = startIndex; i < values.size(); i++) {
            DataValue dv = values.get(i);
            CmsDataEntry entry = new CmsDataEntry()
                    .reference(dv.ref)
                    .value(new CmsVisibleString(dv.val).max(255));
            data.add(entry);
        }

        CmsGetAllDataValues response = new CmsGetAllDataValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .data(data);
        response.moreFollows.set(false);

        log.info("[Server] GetAllDataValues: {} entries{}", data.size(),
                fcFilter != null ? " (fc=" + fcFilter + ")" : "");
        return new CmsApdu(response);
    }

    private List<TargetLn> resolveTargets(SclIED.SclAccessPoint accessPoint, String ldName, String lnRef) {
        SclIED.SclServer server = accessPoint.getServer();
        if (ldName != null && !ldName.isEmpty()) {
            SclIED.SclLDevice device = findLDevice(server, ldName);
            if (device == null) {
                log.warn("[Server] LDevice not found: {}", ldName);
                return null;
            }
            List<TargetLn> result = new ArrayList<>();
            SclLN0 ln0 = device.getLn0();
            if (ln0 != null) {
                result.add(new TargetLn(ldName, ln0.getLnClass(), ln0.getInst(), ln0.getLnType(), ln0, null));
            }
            for (SclLN ln : device.getLns()) {
                result.add(new TargetLn(ldName, ln.getLnClass(), ln.getInst(), ln.getLnType(), null, ln));
            }
            return result;
        }
        if (lnRef == null || lnRef.isEmpty()) {
            log.warn("[Server] No ldName or lnReference provided");
            return null;
        }
        int slashIdx = lnRef.indexOf('/');
        if (slashIdx < 0) {
            log.warn("[Server] Invalid lnReference (no '/'): {}", lnRef);
            return null;
        }
        String targetLd = lnRef.substring(0, slashIdx);
        String targetLnName = lnRef.substring(slashIdx + 1);

        SclIED.SclLDevice device = findLDevice(server, targetLd);
        if (device == null) {
            log.warn("[Server] LDevice not found: {}", targetLd);
            return null;
        }
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(targetLnName)) {
                List<TargetLn> result = new ArrayList<>();
                result.add(new TargetLn(device.getInst(), device.getLn0().getLnClass(),
                        device.getLn0().getInst(), device.getLn0().getLnType(), device.getLn0(), null));
                return result;
            }
        }
        for (SclLN ln : device.getLns()) {
            String lnName = ln.getLnClass() + ln.getInst();
            if (lnName.equals(targetLnName)) {
                List<TargetLn> result = new ArrayList<>();
                result.add(new TargetLn(device.getInst(), ln.getLnClass(), ln.getInst(), ln.getLnType(), null, ln));
                return result;
            }
        }
        log.warn("[Server] LN not found: {} in LDevice {}", targetLnName, targetLd);
        return null;
    }

    private List<DataValue> collectDataValues(List<TargetLn> targets, boolean relative, String fcFilter) {
        SclDataTypeTemplates templates = sclDocument != null ? sclDocument.getDataTypeTemplates() : null;
        List<DataValue> result = new ArrayList<>();

        for (TargetLn t : targets) {
            String prefix = relative ? "" : (t.lnClass + t.inst + ".");
            List<SclDOI> dois;
            if (t.ln0 != null) {
                dois = t.ln0.getDois();
            } else if (t.ln != null) {
                dois = t.ln.getDois();
            } else {
                dois = null;
            }
            if (dois == null || dois.isEmpty()) {
                continue;
            }

            SclLNodeType lnt = null;
            if (templates != null && t.lnType != null && !t.lnType.isEmpty()) {
                lnt = templates.findLNodeTypeById(t.lnType);
            }

            for (SclDOI doi : dois) {
                String doiPrefix = prefix + doi.getName();
                SclDOType doType = resolveDoType(templates, lnt, doi.getName());

                collectDaiValues(doi.getDais(), doiPrefix, doType, fcFilter, result);
                for (SclSDI sdi : doi.getSdis()) {
                    String sdiPrefix = doiPrefix + "." + sdi.getName();
                    SclDOType sdiDoType = resolveSdiDoType(doType, sdi.getName(), templates);
                    collectDaiValues(sdi.getDais(), sdiPrefix, sdiDoType, fcFilter, result);
                }
            }
        }
        return result;
    }

    private void collectDaiValues(List<SclDAI> dais, String prefix, SclDOType doType,
                                   String fcFilter, List<DataValue> result) {
        if (dais == null || dais.isEmpty()) {
            return;
        }
        for (SclDAI dai : dais) {
            if (dai.getValue() == null || dai.getValue().isEmpty()) {
                continue;
            }
            String daFc = findDaFc(doType, dai.getName());
            if (fcFilter != null && !fcFilter.equals(daFc)) {
                continue;
            }
            String ref = prefix + "." + dai.getName();
            result.add(new DataValue(ref, dai.getValue()));
        }
    }

    private String findDaFc(SclDOType doType, String daName) {
        if (doType == null) return null;
        for (SclDA da : doType.getDas()) {
            if (da.getName().equals(daName)) {
                return da.getFc();
            }
        }
        return null;
    }

    private SclDOType resolveDoType(SclDataTypeTemplates templates, SclLNodeType lnt, String doName) {
        if (templates == null || lnt == null) return null;
        for (SclDO doDef : lnt.getDos()) {
            if (doDef.getName().equals(doName) && doDef.getType() != null) {
                return templates.findDoTypeById(doDef.getType());
            }
        }
        return null;
    }

    private SclDOType resolveSdiDoType(SclDOType doType, String sdiName, SclDataTypeTemplates templates) {
        if (doType == null || templates == null) return null;
        for (SclDA da : doType.getDas()) {
            if (da.getName().equals(sdiName) && "Struct".equals(da.getBType()) && da.getType() != null) {
                return templates.findDoTypeById(da.getType());
            }
        }
        for (SclDataTypeTemplates.SclSDO sdo : doType.getSdos()) {
            if (sdo.getName().equals(sdiName) && sdo.getType() != null) {
                return templates.findDoTypeById(sdo.getType());
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

    private CmsApdu buildNegativeResponse(CmsGetAllDataValues request, int errorCode) {
        CmsGetAllDataValues response = new CmsGetAllDataValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get());
        response.serviceError.set(errorCode);
        return new CmsApdu(response);
    }

    private static class TargetLn {
        final String ldInst;
        final String lnClass;
        final String inst;
        final String lnType;
        final SclLN0 ln0;
        final SclLN ln;

        TargetLn(String ldInst, String lnClass, String inst, String lnType, SclLN0 ln0, SclLN ln) {
            this.ldInst = ldInst;
            this.lnClass = lnClass;
            this.inst = inst;
            this.lnType = lnType;
            this.ln0 = ln0;
            this.ln = ln;
        }
    }

    private static class DataValue {
        final String ref;
        final String val;

        DataValue(String ref, String val) {
            this.ref = ref;
            this.val = val;
        }
    }
}
