package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDefinition;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDefinitionEntry;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.List;

public class GetDataDefinitionHandler extends AbstractCmsServiceHandler<CmsGetDataDefinition> {

    public GetDataDefinitionHandler() {
        super(ServiceName.GET_DATA_DEFINITION, CmsGetDataDefinition::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetDataDefinition asdu = (CmsGetDataDefinition) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsGetDataDefinitionEntry> definitions = new CmsArray<>(CmsGetDataDefinitionEntry::new);

        for (int i = 0; i < asdu.data.size(); i++) {
            CmsGetDataValuesEntry entry = asdu.data.get(i);
            String ref = entry.reference.get();
            String fc = entry.fc.get();

            if (ref == null || ref.isEmpty()) {
                definitions.add(buildErrorEntry());
                continue;
            }

            CmsGetDataDefinitionEntry defEntry = buildDefinition(accessPoint.getServer(), ref, fc);
            definitions.add(defEntry);
        }

        CmsGetDataDefinition response = new CmsGetDataDefinition(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.definition = definitions;
        response.moreFollows.set(false);

        log.debug("[Server] GetDataDefinition: {} entries", definitions.size());
        return new CmsApdu(response);
    }

    private CmsGetDataDefinitionEntry buildErrorEntry() {
        CmsGetDataDefinitionEntry defEntry = new CmsGetDataDefinitionEntry();
        CmsDataDefinition def = new CmsDataDefinition();
        def.set(CmsDataDefinition.ERROR);
        return defEntry.definition(def);
    }

    private CmsGetDataDefinitionEntry buildDefinition(SclIED.SclServer server, String ref, String fc) {
        CmsGetDataDefinitionEntry defEntry = new CmsGetDataDefinitionEntry();

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return defEntry.definition(buildErrorDef());
        }

        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");

        SclIED.SclLDevice device = findLDevice(server, ldName);
        if (device == null || parts.length < 1) {
            return defEntry.definition(buildErrorDef());
        }

        String lnName = parts[0];
        boolean hasDo = parts.length > 1;

        if (!hasDo) {
            return defEntry.definition(buildErrorDef());
        }

        String doName = parts[1];
        SclIED.SclDOI doi = findDoiInDevice(device, lnName, doName);
        if (doi == null) {
            return defEntry.definition(buildErrorDef());
        }

        boolean isDataAttribute = parts.length > 2;

        if (isDataAttribute) {
            defEntry.cdcType("");
            String daName = parts[parts.length - 1];
            if (parts.length == 3) {
                defEntry.definition(buildDaDefinition(doi.getDais(), daName));
            } else {
                String sdiName = parts[2];
                SclIED.SclSDI sdi = findSdi(doi, sdiName);
                if (sdi == null) {
                    defEntry.definition(buildErrorDef());
                } else {
                    defEntry.definition(buildDaDefinition(sdi.getDais(), daName));
                }
            }
        } else {
            defEntry.cdcType("SPC");
            defEntry.definition(buildDoDefinition(doi));
        }

        return defEntry;
    }

    private CmsDataDefinition buildErrorDef() {
        CmsDataDefinition def = new CmsDataDefinition();
        def.set(CmsDataDefinition.ERROR);
        return def;
    }

    private CmsDataDefinition buildDoDefinition(SclIED.SclDOI doi) {
        List<CmsDataDefinition.StructureEntry> entries = new ArrayList<>();
        for (SclIED.SclDAI dai : doi.getDais()) {
            entries.add(new CmsDataDefinition.StructureEntry(
                    dai.getName(), CmsDataDefinition.ofBoolean()));
        }
        for (SclIED.SclSDI sdi : doi.getSdis()) {
            entries.add(new CmsDataDefinition.StructureEntry(
                    sdi.getName(), CmsDataDefinition.ofBoolean()));
        }
        return CmsDataDefinition.ofStructure(entries);
    }

    private CmsDataDefinition buildDaDefinition(java.util.List<SclIED.SclDAI> dais, String daName) {
        for (SclIED.SclDAI dai : dais) {
            if (dai.getName().equals(daName)) {
                return CmsDataDefinition.ofBoolean();
            }
        }
        return buildErrorDef();
    }

    private SclIED.SclSDI findSdi(SclIED.SclDOI doi, String sdiName) {
        for (SclIED.SclSDI sdi : doi.getSdis()) {
            if (sdi.getName().equals(sdiName)) return sdi;
        }
        return null;
    }

    private SclIED.SclDOI findDoiInDevice(SclIED.SclLDevice device, String lnName, String doName) {
        if (doName == null) return null;
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                for (SclIED.SclDOI doi : device.getLn0().getDois()) {
                    if (doi.getName().equals(doName)) return doi;
                }
                return null;
            }
        }
        for (SclIED.SclLN ln : device.getLns()) {
            String curLnName = ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                for (SclIED.SclDOI doi : ln.getDois()) {
                    if (doi.getName().equals(doName)) return doi;
                }
                return null;
            }
        }
        return null;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice device : server.getLDevices()) {
            if (device.getInst().equals(ldName)) return device;
        }
        return null;
    }
}
