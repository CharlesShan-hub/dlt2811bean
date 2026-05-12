package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDirectory;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDirectoryEntry;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.List;

public class GetDataDirectoryHandler extends AbstractCmsServiceHandler<CmsGetDataDirectory> {

    public GetDataDirectoryHandler() {
        super(ServiceName.GET_DATA_DIRECTORY, CmsGetDataDirectory::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetDataDirectory asdu = (CmsGetDataDirectory) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ref = asdu.dataReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");

        SclIED.SclLDevice device = findLDevice(accessPoint.getServer(), ldName);
        if (device == null) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (parts.length < 1) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String lnName = parts[0];
        String doName = parts.length > 1 ? parts[1] : null;

        String afterRef = asdu.referenceAfter.get();
        boolean skipUntilAfter = afterRef != null && !afterRef.isEmpty();

        List<CmsGetDataDirectoryEntry> allEntries;
        if (doName == null) {
            // LN level: list all DOIs under this LN
            List<SclIED.SclDOI> dois = findDoisInLn(device, lnName);
            if (dois == null) {
                return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            allEntries = new ArrayList<>();
            for (SclIED.SclDOI doi : dois) {
                allEntries.addAll(buildDirectoryEntries(doi, ""));
            }
        } else {
            // DO level: find specific DOI and list its DAIs/SDIs
            SclIED.SclDOI doi = findDoiInDevice(device, lnName, doName);
            if (doi == null) {
                return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            allEntries = buildDirectoryEntries(doi, "");
        }
        if (skipUntilAfter) {
            allEntries = filterAfter(allEntries, afterRef);
            if (allEntries == null) {
                return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }

        CmsArray<CmsGetDataDirectoryEntry> responseEntries = new CmsArray<>(CmsGetDataDirectoryEntry::new);
        for (CmsGetDataDirectoryEntry entry : allEntries) {
            responseEntries.add(entry);
        }

        CmsGetDataDirectory response = new CmsGetDataDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.dataAttribute = responseEntries;
        response.moreFollows.set(false);

        log.debug("[Server] GetDataDirectory: '{}' -> {} entries", ref, responseEntries.size());
        return new CmsApdu(response);
    }

    private List<CmsGetDataDirectoryEntry> buildDirectoryEntries(SclIED.SclDOI doi, String prefix) {
        List<CmsGetDataDirectoryEntry> entries = new ArrayList<>();

        for (SclIED.SclDAI dai : doi.getDais()) {
            CmsGetDataDirectoryEntry entry = new CmsGetDataDirectoryEntry();
            String entryRef = prefix + dai.getName();
            entry.reference.set(entryRef);
            entries.add(entry);
        }

        for (SclIED.SclSDI sdi : doi.getSdis()) {
            String sdiPrefix = prefix + sdi.getName() + ".";
            entries.addAll(buildDirectoryEntriesFromSdi(sdi, sdiPrefix));
        }

        return entries;
    }

    private List<CmsGetDataDirectoryEntry> buildDirectoryEntriesFromSdi(SclIED.SclSDI sdi, String prefix) {
        List<CmsGetDataDirectoryEntry> entries = new ArrayList<>();

        for (SclIED.SclDAI dai : sdi.getDais()) {
            CmsGetDataDirectoryEntry entry = new CmsGetDataDirectoryEntry();
            String entryRef = prefix + dai.getName();
            entry.reference.set(entryRef);
            entries.add(entry);
        }

        return entries;
    }

    private List<CmsGetDataDirectoryEntry> filterAfter(List<CmsGetDataDirectoryEntry> all, String afterRef) {
        boolean found = false;
        List<CmsGetDataDirectoryEntry> result = new ArrayList<>();
        for (CmsGetDataDirectoryEntry entry : all) {
            String ref = entry.reference.get();
            if (!found) {
                if (ref != null && ref.equals(afterRef)) {
                    found = true;
                }
                continue;
            }
            result.add(entry);
        }
        return found ? result : null;
    }

    private List<SclIED.SclDOI> findDoisInLn(SclIED.SclLDevice device, String lnName) {
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                return device.getLn0().getDois();
            }
        }
        for (SclIED.SclLN ln : device.getLns()) {
            String curLnName = ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                return ln.getDois();
            }
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
            if (device.getInst().equals(ldName)) {
                return device;
            }
        }
        return null;
    }
}
