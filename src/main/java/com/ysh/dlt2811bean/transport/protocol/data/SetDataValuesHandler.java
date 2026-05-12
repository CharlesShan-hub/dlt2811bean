package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDO;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDAI;
import com.ysh.dlt2811bean.scl.model.SclIED.SclDOI;
import com.ysh.dlt2811bean.scl.model.SclIED.SclSDI;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsSetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsSetDataValuesEntry;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDA;

public class SetDataValuesHandler extends AbstractCmsServiceHandler<CmsSetDataValues> {

    public SetDataValuesHandler() {
        super(ServiceName.SET_DATA_VALUES, CmsSetDataValues::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsSetDataValues asdu = (CmsSetDataValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return null;
        }

        SclIED.SclServer server = accessPoint.getServer();
        SclDataTypeTemplates templates = serverSession.getSclDataTypeTemplates();

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new).capacity(asdu.data.size());
        boolean allSuccess = true;

        for (CmsSetDataValuesEntry entry : asdu.data) {
            String ref = entry.reference.get();
            int error = validateAndSetValue(server, templates, ref, entry);
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

    private int validateAndSetValue(SclIED.SclServer server, SclDataTypeTemplates templates,
                                      String ref, CmsSetDataValuesEntry entry) {
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
        if (doi == null && templates != null) {
            // Try to create virtual DOI from type templates
            doi = createVirtualDoi(server, device, templates, ldName, lnName, doName);
        }
        if (doi == null) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        String daName = parts[parts.length - 1];
        SclDAI dai = null;
        if (parts.length == 4) {
            String sdiName = parts[2];
            SclSDI sdi = findSdi(doi, sdiName);
            if (sdi != null) {
                dai = findDaiByName(sdi.getDais(), daName);
            }
            if (dai == null && templates != null) {
                // Create virtual DAI in SDI
                sdi = findOrCreateSdi(doi, sdiName);
                dai = findOrCreateDai(sdi.getDais(), daName);
            }
        } else if (parts.length == 3) {
            dai = findDaiByName(doi.getDais(), daName);
            if (dai == null && templates != null) {
                dai = findOrCreateDai(doi.getDais(), daName);
            }
        } else {
            // parts.length == 2: DO-level set — store value on first DA from type template
            if (templates != null) {
                String firstDaName = findFirstDaName(server, templates, ldName, lnName, doName);
                if (firstDaName != null) {
                    dai = findOrCreateDai(doi.getDais(), firstDaName);
                }
            }
            if (dai == null) {
                // Fallback: create a default stVal DAI
                dai = findOrCreateDai(doi.getDais(), "stVal");
            }
        }
        if (dai == null) {
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

    /**
     * Finds the first DA name for a DO from type templates.
     */
    private String findFirstDaName(SclIED.SclServer server, SclDataTypeTemplates templates,
                                    String ldName, String lnName, String doName) {
        java.util.List<SclDA> das = SclTypeResolver.listDasFromType(server, templates, ldName, lnName, doName);
        if (das != null && !das.isEmpty()) {
            return das.get(0).getName();
        }
        return null;
    }

    /**
     * Creates a virtual DOI from type templates when no instance DOI exists.
     */
    private SclDOI createVirtualDoi(SclIED.SclServer server, SclIED.SclLDevice device,
                                     SclDataTypeTemplates templates,
                                     String ldName, String lnName, String doName) {
        SclDO doObj = SclTypeResolver.findDoInType(server, templates, ldName, lnName, doName);
        if (doObj == null) return null;

        // Find the LN to add the DOI to
        SclIED.SclLN ln = SclTypeResolver.findLnInDevice(device, lnName);
        if (ln == null) return null;

        SclDOI doi = new SclDOI();
        doi.setName(doName);
        ln.getDois().add(doi);
        log.debug("[Server] Created virtual DOI: {}", doName);
        return doi;
    }

    /**
     * Creates a virtual DAI in the given list if not found.
     */
    private SclDAI findOrCreateDai(java.util.List<SclDAI> dais, String daName) {
        SclDAI existing = findDaiByName(dais, daName);
        if (existing != null) return existing;
        SclDAI dai = new SclDAI();
        dai.setName(daName);
        dais.add(dai);
        log.debug("[Server] Created virtual DAI: {}", daName);
        return dai;
    }

    /**
     * Finds or creates an SDI in the given DOI.
     */
    private SclSDI findOrCreateSdi(SclDOI doi, String sdiName) {
        SclSDI existing = findSdi(doi, sdiName);
        if (existing != null) return existing;
        SclSDI sdi = new SclSDI();
        sdi.setName(sdiName);
        doi.getSdis().add(sdi);
        log.debug("[Server] Created virtual SDI: {}", sdiName);
        return sdi;
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

    private SclSDI findSdi(SclDOI doi, String sdiName) {
        for (SclSDI sdi : doi.getSdis()) {
            if (sdi.getName().equals(sdiName)) {
                return sdi;
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
